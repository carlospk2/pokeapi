package com.example.pokeapi.features.battle.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.pokeapi.shared.features.battle.engine.BattlePokemon
import com.example.pokeapi.shared.features.battle.engine.BattleMove
import com.example.pokeapi.ui.components.HpBar
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.components.RetroButton
import com.example.pokeapi.ui.components.TypeBadge
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import com.example.pokeapi.ui.theme.typeColor
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BattleScreen(
    trainerId: Int,
    playerTeamId: Long,
    onBattleEnd: (Long) -> Unit,
    viewModel: BattleViewModel = koinViewModel(parameters = { parametersOf(trainerId, playerTeamId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BattleEffect.BattleEnded -> onBattleEnd(effect.battleRecordId)
                is BattleEffect.ShowSnackbar -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading battle...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                }
            }
            state.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                }
            }
            state.battleState != null -> {
                val battleState = state.battleState!!
                Column(modifier = Modifier.fillMaxSize()) {
                    // Battle arena (top half)
                    BattleArena(
                        playerPokemon = battleState.playerActive,
                        opponentPokemon = battleState.opponentActive,
                        modifier = Modifier.weight(1f)
                    )

                    // Action panel (bottom half)
                    BattleActionPanel(
                        uiState = state,
                        battleState = battleState,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier.height(220.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BattleArena(
    playerPokemon: BattlePokemon,
    opponentPokemon: BattlePokemon,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Background)
            .padding(8.dp)
    ) {
        // Opponent info bar (top)
        RetroBox(
            modifier = Modifier.fillMaxWidth().padding(start = 80.dp),
            backgroundColor = Surface
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        opponentPokemon.name.uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Lv.${opponentPokemon.level}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                HpBar(currentHp = opponentPokemon.currentHp, maxHp = opponentPokemon.maxHp, modifier = Modifier.fillMaxWidth())
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Opponent sprite (top right)
            val opponentSpriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${opponentPokemon.id}.png"
            AsyncImage(
                model = opponentSpriteUrl,
                contentDescription = opponentPokemon.name,
                modifier = Modifier.size(96.dp).align(Alignment.TopEnd),
                filterQuality = FilterQuality.None,
                contentScale = ContentScale.Fit
            )

            // Player sprite (bottom left)
            val playerSpriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${playerPokemon.id}.png"
            AsyncImage(
                model = playerSpriteUrl,
                contentDescription = playerPokemon.name,
                modifier = Modifier.size(96.dp).align(Alignment.BottomStart),
                filterQuality = FilterQuality.None,
                contentScale = ContentScale.Fit
            )

            // Player info bar (bottom right)
            RetroBox(
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 8.dp, bottom = 8.dp).width(180.dp),
                backgroundColor = Surface
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            playerPokemon.name.uppercase(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Lv.${playerPokemon.level}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    HpBar(
                        currentHp = playerPokemon.currentHp,
                        maxHp = playerPokemon.maxHp,
                        showNumbers = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun BattleActionPanel(
    uiState: BattleUiState,
    battleState: com.example.pokeapi.shared.features.battle.engine.BattleState,
    onEvent: (BattleUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    RetroBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Surface
    ) {
        when (uiState.phase) {
            BattlePhase.PLAYER_TURN -> PlayerTurnPanel(onEvent = onEvent)
            BattlePhase.SHOWING_MOVES -> MoveSelectionPanel(
                pokemon = battleState.playerActive,
                onEvent = onEvent
            )
            BattlePhase.SHOWING_TEAM -> TeamPanel(
                team = battleState.playerTeam,
                activeIndex = battleState.playerActiveIndex,
                onEvent = onEvent
            )
            BattlePhase.SHOWING_NARRATIVE, BattlePhase.ANIMATING -> NarrativePanel(
                text = uiState.narrativeText,
                onClick = { onEvent(BattleUiEvent.AdvanceText) }
            )
            BattlePhase.FORCED_SWITCH -> TeamPanel(
                team = battleState.playerTeam,
                activeIndex = battleState.playerActiveIndex,
                onEvent = onEvent,
                forcedSwitch = true
            )
            BattlePhase.BATTLE_END -> BattleEndPanel(
                text = uiState.narrativeText,
                onClick = { onEvent(BattleUiEvent.AdvanceText) }
            )
            BattlePhase.LOADING -> Unit
        }
    }
}

@Composable
private fun PlayerTurnPanel(onEvent: (BattleUiEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(
            "What will you do?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RetroButton(
                text = "Fight",
                onClick = { onEvent(BattleUiEvent.ShowMoves) },
                modifier = Modifier.weight(1f)
            )
            RetroButton(
                text = "Pokemon",
                onClick = { onEvent(BattleUiEvent.ShowTeam) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MoveSelectionPanel(
    pokemon: BattlePokemon,
    onEvent: (BattleUiEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Choose a move:", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            RetroButton(text = "Back", onClick = { onEvent(BattleUiEvent.BackToActions) })
        }
        Spacer(Modifier.height(8.dp))
        // 2x2 grid
        val moves = pokemon.moves
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (row in 0..1) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (col in 0..1) {
                        val idx = row * 2 + col
                        val move = moves.getOrNull(idx)
                        if (move != null) {
                            val pp = pokemon.ppRemaining.getOrDefault(move.id, move.pp)
                            val enabled = pp > 0
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(if (enabled) typeColor(move.type).copy(alpha = 0.3f) else SurfaceVariant)
                                    .clickable(enabled = enabled) { onEvent(BattleUiEvent.SelectMove(move.id)) }
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        move.name.uppercase().replace("-", " "),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        TypeBadge(type = move.type)
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            "PP $pp/${move.pp}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamPanel(
    team: List<BattlePokemon>,
    activeIndex: Int,
    onEvent: (BattleUiEvent) -> Unit,
    forcedSwitch: Boolean = false
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (forcedSwitch) "Choose next Pokemon:" else "Your team:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!forcedSwitch) {
                RetroButton(text = "Back", onClick = { onEvent(BattleUiEvent.BackToActions) })
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(team) { index, pokemon ->
                val isActive = index == activeIndex
                val canSwitch = !pokemon.isFainted && !isActive
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else SurfaceVariant)
                        .clickable(enabled = canSwitch) {
                            if (forcedSwitch) {
                                onEvent(BattleUiEvent.SwitchPokemon(index))
                            } else {
                                onEvent(BattleUiEvent.SwitchPokemon(index))
                            }
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pokemon.name.uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (pokemon.isFainted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "HP:${pokemon.currentHp}/${pokemon.maxHp}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    if (isActive) {
                        Spacer(Modifier.width(4.dp))
                        Text("◄", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun NarrativePanel(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "▼",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth().padding(end = 8.dp)
            )
        }
    }
}

@Composable
private fun BattleEndPanel(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}
