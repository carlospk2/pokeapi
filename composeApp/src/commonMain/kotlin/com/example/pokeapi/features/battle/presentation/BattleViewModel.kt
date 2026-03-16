package com.example.pokeapi.features.battle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.shared.database.dao.BattleRecordDao
import com.example.pokeapi.shared.database.dao.MoveDao
import com.example.pokeapi.shared.database.dao.PlayerStatsDao
import com.example.pokeapi.shared.database.dao.PokemonDao
import com.example.pokeapi.shared.database.dao.TeamMemberDao
import com.example.pokeapi.shared.database.dao.TrainerDao
import com.example.pokeapi.shared.database.dao.TypeEffectivenessDao
import com.example.pokeapi.shared.database.entity.BattleRecordEntity
import com.example.pokeapi.shared.database.entity.PlayerStatsEntity
import com.example.pokeapi.shared.features.battle.engine.BattleAction
import com.example.pokeapi.shared.features.battle.engine.BattleAi
import com.example.pokeapi.shared.features.battle.engine.BattleEngine
import com.example.pokeapi.shared.features.battle.engine.BattleEvent
import com.example.pokeapi.shared.features.battle.engine.BattleMove
import com.example.pokeapi.shared.features.battle.engine.BattleState
import com.example.pokeapi.shared.features.sync.data.repository.TrainerMemberJsonModel
import com.example.pokeapi.shared.platform.currentTimeMillis
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.pokeapi.shared.features.sync.data.repository.decodeTrainerTeamJson

class BattleViewModel(
    private val trainerId: Int,
    private val playerTeamId: Long,
    private val trainerDao: TrainerDao,
    private val teamMemberDao: TeamMemberDao,
    private val pokemonDao: PokemonDao,
    private val moveDao: MoveDao,
    private val typeEffectivenessDao: TypeEffectivenessDao,
    private val battleRecordDao: BattleRecordDao,
    private val playerStatsDao: PlayerStatsDao
) : ViewModel() {

    private val _state = MutableStateFlow(BattleUiState())
    val state: StateFlow<BattleUiState> = _state.asStateFlow()

    private val _effect = Channel<BattleEffect>()
    val effect = _effect.receiveAsFlow()

    private lateinit var engine: BattleEngine
    private lateinit var ai: BattleAi
    private lateinit var battleState: BattleState

    init {
        viewModelScope.launch {
            try {
                setupBattle()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private suspend fun setupBattle() {
        // Load type chart
        val typeEntities = typeEffectivenessDao.getAll()
        val typeChart = typeEntities.associate { (it.attackingType to it.defendingType) to it.multiplier }

        engine = BattleEngine(typeChart)

        // Load trainer
        val trainer = trainerDao.getById(trainerId) ?: throw Exception("Trainer not found")
        val trainerTeamData: List<TrainerMemberJsonModel> = decodeTrainerTeamJson(trainer.teamJson)

        // Build trainer pokemon
        val opponentTeam = trainerTeamData.mapNotNull { member ->
            val pokemon = pokemonDao.getById(member.pokemonId) ?: return@mapNotNull null
            val moves = member.moveNames.mapNotNull { name -> moveDao.getByName(name) }.map { m ->
                BattleMove(id = m.id, name = m.name, type = m.type, category = m.category,
                    power = m.power ?: 0, accuracy = m.accuracy, pp = m.pp, priority = m.priority)
            }.ifEmpty { listOf(BattleEngine.STRUGGLE) }
            engine.createBattlePokemon(
                id = pokemon.id, name = pokemon.name,
                typePrimary = pokemon.typePrimary, typeSecondary = pokemon.typeSecondary,
                level = member.level,
                baseHp = pokemon.baseHp, baseAtk = pokemon.baseAttack, baseDef = pokemon.baseDefense,
                baseSpAtk = pokemon.baseSpAttack, baseSpDef = pokemon.baseSpDefense, baseSpd = pokemon.baseSpeed,
                moves = moves
            )
        }.toMutableList()

        // Build player team
        val playerMembers = teamMemberDao.getMembersForTeam(playerTeamId)
        val playerTeam = playerMembers.mapNotNull { member ->
            val pokemon = pokemonDao.getById(member.pokemonId) ?: return@mapNotNull null
            val moveIds = listOfNotNull(
                member.move1Id.takeIf { it != 0 }, member.move2Id, member.move3Id, member.move4Id
            )
            val moves = moveIds.mapNotNull { id -> moveDao.getById(id) }.map { m ->
                BattleMove(id = m.id, name = m.name, type = m.type, category = m.category,
                    power = m.power ?: 0, accuracy = m.accuracy, pp = m.pp, priority = m.priority)
            }.ifEmpty { listOf(BattleEngine.STRUGGLE) }
            engine.createBattlePokemon(
                id = pokemon.id, name = pokemon.name,
                typePrimary = pokemon.typePrimary, typeSecondary = pokemon.typeSecondary,
                level = member.level,
                baseHp = pokemon.baseHp, baseAtk = pokemon.baseAttack, baseDef = pokemon.baseDefense,
                baseSpAtk = pokemon.baseSpAttack, baseSpDef = pokemon.baseSpDefense, baseSpd = pokemon.baseSpeed,
                moves = moves
            )
        }.toMutableList()

        if (playerTeam.isEmpty() || opponentTeam.isEmpty()) {
            throw Exception("Team is empty. Add pokemon to your team first!")
        }

        battleState = BattleState(
            playerTeam = playerTeam,
            opponentTeam = opponentTeam
        )

        ai = BattleAi(strategy = trainer.aiStrategy, typeChart = typeChart)

        _state.update {
            it.copy(
                battleState = battleState,
                isLoading = false,
                phase = BattlePhase.PLAYER_TURN,
                narrativeText = "What will ${battleState.playerActive.name.uppercase()} do?"
            )
        }
    }

    fun onEvent(event: BattleUiEvent) {
        when (event) {
            BattleUiEvent.ShowMoves -> _state.update { it.copy(phase = BattlePhase.SHOWING_MOVES) }
            BattleUiEvent.ShowTeam -> _state.update { it.copy(phase = BattlePhase.SHOWING_TEAM) }
            BattleUiEvent.BackToActions -> _state.update { it.copy(phase = BattlePhase.PLAYER_TURN) }

            is BattleUiEvent.SelectMove -> {
                viewModelScope.launch {
                    executeTurn(BattleAction.UseMove(event.moveId))
                }
            }

            is BattleUiEvent.SwitchPokemon -> {
                viewModelScope.launch {
                    executeTurn(BattleAction.SwitchPokemon(event.index))
                }
            }

            BattleUiEvent.AdvanceText -> {
                val pendingEvents = _state.value.pendingEvents
                if (pendingEvents.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            narrativeText = pendingEvents.first(),
                            pendingEvents = pendingEvents.drop(1)
                        )
                    }
                } else {
                    val currentState = _state.value
                    if (currentState.phase == BattlePhase.BATTLE_END) return
                    if (!::battleState.isInitialized) return
                    if (battleState.isFinished) return

                    // Check if player pokemon fainted
                    if (battleState.playerActive.isFainted && battleState.playerHasLiving()) {
                        _state.update { it.copy(phase = BattlePhase.FORCED_SWITCH) }
                    } else {
                        _state.update {
                            it.copy(
                                phase = BattlePhase.PLAYER_TURN,
                                narrativeText = "What will ${battleState.playerActive.name.uppercase()} do?"
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun executeTurn(playerAction: BattleAction) {
        if (!::battleState.isInitialized) return
        _state.update { it.copy(phase = BattlePhase.ANIMATING) }

        val opponentAction = ai.decideAction(battleState, engine)
        val (newState, turnResult) = engine.resolveTurn(battleState, playerAction, opponentAction)
        battleState = newState

        // Build narrative events
        val narrativeMessages = buildNarrative(turnResult.events)

        // Add delay for AI "thinking"
        delay(300)

        if (newState.isFinished) {
            val won = newState.playerWon == true
            saveBattleRecord(won, newState.turn - 1, newState.playerTeam.count { !it.isFainted })
            _state.update {
                it.copy(
                    battleState = newState,
                    phase = BattlePhase.BATTLE_END,
                    narrativeText = narrativeMessages.firstOrNull() ?: "",
                    pendingEvents = if (narrativeMessages.size > 1) narrativeMessages.drop(1) else emptyList()
                )
            }
        } else {
            _state.update {
                it.copy(
                    battleState = newState,
                    phase = BattlePhase.SHOWING_NARRATIVE,
                    narrativeText = narrativeMessages.firstOrNull() ?: "",
                    pendingEvents = if (narrativeMessages.size > 1) narrativeMessages.drop(1) else emptyList()
                )
            }
        }
    }

    private fun buildNarrative(events: List<BattleEvent>): List<String> {
        return events.map { event ->
            when (event) {
                is BattleEvent.MoveUsed -> "${event.attackerName.uppercase()} used ${event.moveName.uppercase().replace("-", " ")}!"
                is BattleEvent.DamageDealt -> when {
                    event.effectiveness >= 4.0f -> "It's SUPER effective!"
                    event.effectiveness >= 2.0f -> "It's super effective!"
                    event.effectiveness < 1.0f && event.effectiveness > 0f -> "It's not very effective..."
                    event.effectiveness == 0.0f -> "It doesn't affect ${event.targetName.uppercase()}!"
                    else -> ""
                }.let { effText ->
                    if (effText.isNotEmpty()) effText else "${event.targetName.uppercase()} took ${event.damage} damage!"
                }
                is BattleEvent.MoveMissed -> "${event.attackerName.uppercase()}'s attack missed!"
                is BattleEvent.PokemonFainted -> "${event.pokemonName.uppercase()} fainted!"
                is BattleEvent.PokemonSwitched -> "${event.trainerLabel} ${event.pokemonName.uppercase()}!"
                is BattleEvent.BattleEnded -> if (event.playerWon) "You won!" else "You lost..."
            }
        }.filter { it.isNotEmpty() }
    }

    private suspend fun saveBattleRecord(won: Boolean, turns: Int, pokemonRemaining: Int) {
        val now = currentTimeMillis()
        val recordId = battleRecordDao.insert(
            BattleRecordEntity(
                trainerId = trainerId,
                playerTeamId = playerTeamId,
                result = if (won) "win" else "loss",
                turnsCount = turns,
                date = now,
                pokemonRemaining = pokemonRemaining
            )
        )

        // Update player stats
        val currentStats = playerStatsDao.get() ?: PlayerStatsEntity()
        val newStreak = if (won) currentStats.currentStreak + 1 else 0
        playerStatsDao.upsert(currentStats.copy(
            totalWins = if (won) currentStats.totalWins + 1 else currentStats.totalWins,
            totalLosses = if (!won) currentStats.totalLosses + 1 else currentStats.totalLosses,
            currentStreak = newStreak,
            bestStreak = maxOf(currentStats.bestStreak, newStreak),
            totalBattles = currentStats.totalBattles + 1
        ))

        _effect.send(BattleEffect.BattleEnded(recordId))
    }
}
