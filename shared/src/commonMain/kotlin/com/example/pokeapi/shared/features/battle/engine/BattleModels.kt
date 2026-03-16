package com.example.pokeapi.shared.features.battle.engine

data class BattleMove(
    val id: Int,
    val name: String,
    val type: String,
    val category: String,
    val power: Int,
    val accuracy: Int?,
    val pp: Int,
    val priority: Int = 0
)

data class BattlePokemon(
    val id: Int,
    val name: String,
    val typePrimary: String,
    val typeSecondary: String?,
    val level: Int,
    val maxHp: Int,
    var currentHp: Int,
    val attack: Int,
    val defense: Int,
    val spAttack: Int,
    val spDefense: Int,
    val speed: Int,
    val moves: List<BattleMove>,
    val ppRemaining: MutableMap<Int, Int> = mutableMapOf() // moveId -> pp remaining
) {
    val isFainted: Boolean get() = currentHp <= 0
    val types: List<String> get() = listOfNotNull(typePrimary, typeSecondary)
}

sealed interface BattleAction {
    data class UseMove(val moveId: Int) : BattleAction
    data class SwitchPokemon(val pokemonIndex: Int) : BattleAction
}

data class TurnResult(
    val turn: Int,
    val playerAction: BattleAction,
    val opponentAction: BattleAction,
    val events: List<BattleEvent>
)

sealed interface BattleEvent {
    data class MoveUsed(val attackerName: String, val moveName: String) : BattleEvent
    data class DamageDealt(val targetName: String, val damage: Int, val effectiveness: Float) : BattleEvent
    data class MoveMissed(val attackerName: String, val moveName: String) : BattleEvent
    data class PokemonFainted(val pokemonName: String) : BattleEvent
    data class PokemonSwitched(val trainerLabel: String, val pokemonName: String) : BattleEvent
    data class BattleEnded(val playerWon: Boolean) : BattleEvent
}

data class BattleState(
    val playerTeam: MutableList<BattlePokemon>,
    val opponentTeam: MutableList<BattlePokemon>,
    val playerActiveIndex: Int = 0,
    val opponentActiveIndex: Int = 0,
    val turn: Int = 1,
    val turnHistory: List<TurnResult> = emptyList(),
    val isFinished: Boolean = false,
    val playerWon: Boolean? = null
) {
    val playerActive: BattlePokemon get() = playerTeam[playerActiveIndex]
    val opponentActive: BattlePokemon get() = opponentTeam[opponentActiveIndex]

    fun hasLivingPokemon(team: List<BattlePokemon>): Boolean = team.any { !it.isFainted }
    fun playerHasLiving(): Boolean = hasLivingPokemon(playerTeam)
    fun opponentHasLiving(): Boolean = hasLivingPokemon(opponentTeam)
}
