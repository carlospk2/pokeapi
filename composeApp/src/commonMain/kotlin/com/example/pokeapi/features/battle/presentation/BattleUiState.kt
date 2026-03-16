package com.example.pokeapi.features.battle.presentation

import com.example.pokeapi.shared.features.battle.engine.BattleState

data class BattleUiState(
    val battleState: BattleState? = null,
    val isLoading: Boolean = true,
    val narrativeText: String = "",
    val phase: BattlePhase = BattlePhase.LOADING,
    val pendingEvents: List<String> = emptyList(),
    val isWaitingForInput: Boolean = false,
    val error: String? = null
)

enum class BattlePhase {
    LOADING,
    PLAYER_TURN,
    SHOWING_MOVES,
    SHOWING_TEAM,
    ANIMATING,
    SHOWING_NARRATIVE,
    FORCED_SWITCH,
    BATTLE_END
}

sealed interface BattleUiEvent {
    data class SelectMove(val moveId: Int) : BattleUiEvent
    data class SwitchPokemon(val index: Int) : BattleUiEvent
    data object AdvanceText : BattleUiEvent
    data object ShowMoves : BattleUiEvent
    data object ShowTeam : BattleUiEvent
    data object BackToActions : BattleUiEvent
}

sealed interface BattleEffect {
    data class BattleEnded(val battleRecordId: Long) : BattleEffect
    data class ShowSnackbar(val message: String) : BattleEffect
}
