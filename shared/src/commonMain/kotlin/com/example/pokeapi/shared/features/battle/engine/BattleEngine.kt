package com.example.pokeapi.shared.features.battle.engine

import kotlin.random.Random

class BattleEngine(
    private val typeChart: Map<Pair<String, String>, Float>,
    private val random: (min: Double, max: Double) -> Double = { min, max -> Random.nextDouble(min, max) }
) {
    companion object {
        val STRUGGLE = BattleMove(
            id = -1,
            name = "struggle",
            type = "normal",
            category = "physical",
            power = 50,
            accuracy = null,
            pp = Int.MAX_VALUE,
            priority = 0
        )
    }

    fun processPlayerAction(state: BattleState, playerAction: BattleAction): Pair<BattleState, List<BattleEvent>> {
        // Validate action
        return when (playerAction) {
            is BattleAction.UseMove -> {
                val move = getMove(state.playerActive, playerAction.moveId)
                if (move == null || state.playerActive.ppRemaining.getOrDefault(move.id, move.pp) <= 0) {
                    Pair(state, emptyList())
                } else {
                    Pair(state, emptyList())
                }
            }
            is BattleAction.SwitchPokemon -> Pair(state, emptyList())
        }
    }

    /**
     * Resolve a full turn given player and opponent actions.
     * Returns updated BattleState and list of events.
     */
    fun resolveTurn(
        state: BattleState,
        playerAction: BattleAction,
        opponentAction: BattleAction
    ): Pair<BattleState, TurnResult> {
        val events = mutableListOf<BattleEvent>()
        var currentState = state.copy()

        // Initialize PP if needed
        for (team in listOf(currentState.playerTeam, currentState.opponentTeam)) {
            for (pokemon in team) {
                for (move in pokemon.moves) {
                    if (!pokemon.ppRemaining.containsKey(move.id)) {
                        pokemon.ppRemaining[move.id] = move.pp
                    }
                }
            }
        }

        // Handle switches first
        if (playerAction is BattleAction.SwitchPokemon) {
            val newIndex = playerAction.pokemonIndex
            if (newIndex in currentState.playerTeam.indices && !currentState.playerTeam[newIndex].isFainted) {
                currentState = currentState.copy(playerActiveIndex = newIndex)
                events.add(BattleEvent.PokemonSwitched("You sent out", currentState.playerTeam[newIndex].name))
            }
        }
        if (opponentAction is BattleAction.SwitchPokemon) {
            val newIndex = opponentAction.pokemonIndex
            if (newIndex in currentState.opponentTeam.indices && !currentState.opponentTeam[newIndex].isFainted) {
                currentState = currentState.copy(opponentActiveIndex = newIndex)
                events.add(BattleEvent.PokemonSwitched("Opponent sent out", currentState.opponentTeam[newIndex].name))
            }
        }

        // Determine move order
        val playerMove = if (playerAction is BattleAction.UseMove) getMoveOrStruggle(currentState.playerActive, playerAction.moveId) else null
        val opponentMove = if (opponentAction is BattleAction.UseMove) getMoveOrStruggle(currentState.opponentActive, opponentAction.moveId) else null

        val playerGoesFirst = determineOrder(currentState.playerActive, playerMove, currentState.opponentActive, opponentMove)

        // Execute moves in order
        if (playerGoesFirst) {
            if (playerMove != null && playerAction is BattleAction.UseMove && !currentState.playerActive.isFainted) {
                val (newState, moveEvents) = executeMove(currentState, isPlayer = true, move = playerMove)
                currentState = newState
                events.addAll(moveEvents)
            }
            if (!currentState.isFinished && opponentMove != null && opponentAction is BattleAction.UseMove && !currentState.opponentActive.isFainted) {
                val (newState, moveEvents) = executeMove(currentState, isPlayer = false, move = opponentMove)
                currentState = newState
                events.addAll(moveEvents)
            }
        } else {
            if (opponentMove != null && opponentAction is BattleAction.UseMove && !currentState.opponentActive.isFainted) {
                val (newState, moveEvents) = executeMove(currentState, isPlayer = false, move = opponentMove)
                currentState = newState
                events.addAll(moveEvents)
            }
            if (!currentState.isFinished && playerMove != null && playerAction is BattleAction.UseMove && !currentState.playerActive.isFainted) {
                val (newState, moveEvents) = executeMove(currentState, isPlayer = true, move = playerMove)
                currentState = newState
                events.addAll(moveEvents)
            }
        }

        // Check battle end
        if (!currentState.isFinished) {
            when {
                !currentState.playerHasLiving() -> {
                    currentState = currentState.copy(isFinished = true, playerWon = false)
                    events.add(BattleEvent.BattleEnded(playerWon = false))
                }
                !currentState.opponentHasLiving() -> {
                    currentState = currentState.copy(isFinished = true, playerWon = true)
                    events.add(BattleEvent.BattleEnded(playerWon = true))
                }
            }
        }

        val turnResult = TurnResult(
            turn = currentState.turn,
            playerAction = playerAction,
            opponentAction = opponentAction,
            events = events
        )

        return Pair(currentState.copy(turn = currentState.turn + 1), turnResult)
    }

    private fun determineOrder(
        player: BattlePokemon,
        playerMove: BattleMove?,
        opponent: BattlePokemon,
        opponentMove: BattleMove?
    ): Boolean {
        val playerPriority = playerMove?.priority ?: 0
        val opponentPriority = opponentMove?.priority ?: 0

        return when {
            playerPriority > opponentPriority -> true
            opponentPriority > playerPriority -> false
            player.speed > opponent.speed -> true
            opponent.speed > player.speed -> false
            else -> random(0.0, 1.0) < 0.5
        }
    }

    private fun executeMove(
        state: BattleState,
        isPlayer: Boolean,
        move: BattleMove
    ): Pair<BattleState, List<BattleEvent>> {
        val events = mutableListOf<BattleEvent>()
        val attacker = if (isPlayer) state.playerActive else state.opponentActive
        val defender = if (isPlayer) state.opponentActive else state.playerActive

        events.add(BattleEvent.MoveUsed(attacker.name, move.name))

        // Decrement PP (not for struggle)
        if (move.id != STRUGGLE.id) {
            val currentPp = attacker.ppRemaining.getOrDefault(move.id, move.pp)
            attacker.ppRemaining[move.id] = (currentPp - 1).coerceAtLeast(0)
        }

        // Accuracy check
        if (move.accuracy != null) {
            val roll = random(0.0, 100.0)
            if (roll >= move.accuracy) {
                events.add(BattleEvent.MoveMissed(attacker.name, move.name))
                return Pair(state, events)
            }
        }

        // Calculate damage
        val stab = move.type in attacker.types
        val typeEff = TypeEffectivenessCalculator.calculate(
            moveType = move.type,
            defenderTypes = defender.types,
            chart = typeChart
        )

        val attackStat = if (move.category == "physical") attacker.attack else attacker.spAttack
        val defenseStat = if (move.category == "physical") defender.defense else defender.spDefense

        val randomFactor = random(0.85, 1.0).toFloat()
        val damage = DamageCalculator.calculate(
            level = attacker.level,
            power = move.power,
            attack = attackStat,
            defense = defenseStat,
            stab = stab,
            typeEffectiveness = typeEff,
            random = randomFactor
        )

        // Apply damage
        val actualDamage = damage.coerceAtLeast(1)
        defender.currentHp = (defender.currentHp - actualDamage).coerceAtLeast(0)
        events.add(BattleEvent.DamageDealt(defender.name, actualDamage, typeEff))

        // Check faint
        if (defender.isFainted) {
            events.add(BattleEvent.PokemonFainted(defender.name))
        }

        // Update state with faint handling
        var newState = state
        if (defender.isFainted) {
            if (isPlayer) {
                // Opponent's pokemon fainted
                val nextIndex = state.opponentTeam.indexOfFirst { !it.isFainted }
                if (nextIndex >= 0) {
                    newState = state.copy(opponentActiveIndex = nextIndex)
                }
            } else {
                // Player's pokemon fainted
                val nextIndex = state.playerTeam.indexOfFirst { !it.isFainted }
                if (nextIndex >= 0) {
                    newState = state.copy(playerActiveIndex = nextIndex)
                }
            }
        }

        return Pair(newState, events)
    }

    private fun getMove(pokemon: BattlePokemon, moveId: Int): BattleMove? {
        return pokemon.moves.find { it.id == moveId }
    }

    private fun getMoveOrStruggle(pokemon: BattlePokemon, moveId: Int): BattleMove {
        val move = pokemon.moves.find { it.id == moveId }
        if (move != null) {
            val pp = pokemon.ppRemaining.getOrDefault(move.id, move.pp)
            if (pp > 0) return move
        }
        return STRUGGLE
    }

    /**
     * Get available moves for a pokemon (with PP > 0 or struggle if none)
     */
    fun getAvailableMoves(pokemon: BattlePokemon): List<BattleMove> {
        val available = pokemon.moves.filter { move ->
            (pokemon.ppRemaining.getOrDefault(move.id, move.pp)) > 0
        }
        return available.ifEmpty { listOf(STRUGGLE) }
    }

    fun createBattlePokemon(
        id: Int,
        name: String,
        typePrimary: String,
        typeSecondary: String?,
        level: Int,
        baseHp: Int,
        baseAtk: Int,
        baseDef: Int,
        baseSpAtk: Int,
        baseSpDef: Int,
        baseSpd: Int,
        moves: List<BattleMove>
    ): BattlePokemon {
        val hp = StatsCalculator.calcHp(baseHp, level)
        val atk = StatsCalculator.calcStat(baseAtk, level)
        val def = StatsCalculator.calcStat(baseDef, level)
        val spAtk = StatsCalculator.calcStat(baseSpAtk, level)
        val spDef = StatsCalculator.calcStat(baseSpDef, level)
        val spd = StatsCalculator.calcStat(baseSpd, level)

        val ppMap = mutableMapOf<Int, Int>()
        for (move in moves) {
            ppMap[move.id] = move.pp
        }

        return BattlePokemon(
            id = id,
            name = name,
            typePrimary = typePrimary,
            typeSecondary = typeSecondary,
            level = level,
            maxHp = hp,
            currentHp = hp,
            attack = atk,
            defense = def,
            spAttack = spAtk,
            spDefense = spDef,
            speed = spd,
            moves = moves,
            ppRemaining = ppMap
        )
    }
}
