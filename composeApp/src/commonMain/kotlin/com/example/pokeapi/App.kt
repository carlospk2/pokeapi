package com.example.pokeapi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.pokeapi.features.battle.presentation.BattleResultScreen
import com.example.pokeapi.features.battle.presentation.BattleScreen
import com.example.pokeapi.features.battle.presentation.BattleVSScreen
import com.example.pokeapi.features.battle.presentation.TeamSelectForBattleScreen
import com.example.pokeapi.features.mainmenu.MainMenuScreen
import com.example.pokeapi.features.pokedex.presentation.PokedexScreen
import com.example.pokeapi.features.pokedex.presentation.PokemonDetailScreen
import com.example.pokeapi.features.records.presentation.RecordsScreen
import com.example.pokeapi.features.records.presentation.TrainerRecordsScreen
import com.example.pokeapi.features.sync.presentation.SyncScreen
import com.example.pokeapi.features.teams.presentation.MoveSelectorScreen
import com.example.pokeapi.features.teams.presentation.PokemonSelectorScreen
import com.example.pokeapi.features.teams.presentation.TeamEditScreen
import com.example.pokeapi.features.teams.presentation.TeamListScreen
import com.example.pokeapi.features.trainers.presentation.TrainerSelectScreen
import com.example.pokeapi.navigation.Screen
import com.example.pokeapi.shared.features.sync.domain.usecase.GetSyncStatusUseCase
import com.example.pokeapi.ui.theme.PokeapiTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App() {
    PokeapiTheme {
        val getSyncStatus: GetSyncStatusUseCase = koinInject()
        var initialScreen by remember { mutableStateOf<Screen?>(null) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            val completed = getSyncStatus()
            initialScreen = if (completed) Screen.MainMenu else Screen.Sync
        }

        val screen = initialScreen
        if (screen != null) {
            val backStack = remember(screen) {
                androidx.compose.runtime.mutableStateListOf<Screen>(screen)
            }

            NavDisplay(
                backStack = backStack,
                onBack = { if (backStack.size > 1) backStack.removeLastOrNull() }
            ) { currentScreen ->
                when (currentScreen) {
                    Screen.Sync -> NavEntry(currentScreen) {
                        SyncScreen(
                            onSyncComplete = {
                                backStack.clear()
                                backStack.add(Screen.MainMenu)
                            }
                        )
                    }
                    Screen.MainMenu -> NavEntry(currentScreen) {
                        MainMenuScreen(
                            onPokedex = { backStack.add(Screen.Pokedex) },
                            onTeams = { backStack.add(Screen.TeamList) },
                            onBattle = { backStack.add(Screen.TrainerSelect) },
                            onRecords = { backStack.add(Screen.Records) }
                        )
                    }
                    Screen.Pokedex -> NavEntry(currentScreen) {
                        PokedexScreen(
                            onPokemonClick = { id -> backStack.add(Screen.PokemonDetail(id)) }
                        )
                    }
                    is Screen.PokemonDetail -> NavEntry(currentScreen) {
                        PokemonDetailScreen(pokemonId = currentScreen.pokemonId)
                    }
                    Screen.TeamList -> NavEntry(currentScreen) {
                        TeamListScreen(
                            onNewTeam = { backStack.add(Screen.TeamEdit(null)) },
                            onEditTeam = { id -> backStack.add(Screen.TeamEdit(id)) }
                        )
                    }
                    is Screen.TeamEdit -> NavEntry(currentScreen) {
                        TeamEditScreen(
                            teamId = currentScreen.teamId,
                            onSelectPokemon = { teamId, slot ->
                                backStack.add(Screen.PokemonSelector(teamId, slot))
                            },
                            onSelectMoves = { teamId, slot, pokemonId ->
                                backStack.add(Screen.MoveSelector(teamId, slot, pokemonId))
                            },
                            onSaved = { backStack.removeLastOrNull() }
                        )
                    }
                    is Screen.PokemonSelector -> NavEntry(currentScreen) {
                        PokemonSelectorScreen(
                            teamId = currentScreen.teamId,
                            slot = currentScreen.slot,
                            onSelected = { backStack.removeLastOrNull() }
                        )
                    }
                    is Screen.MoveSelector -> NavEntry(currentScreen) {
                        MoveSelectorScreen(
                            teamId = currentScreen.teamId,
                            slot = currentScreen.slot,
                            pokemonId = currentScreen.pokemonId,
                            onSaved = { backStack.removeLastOrNull() }
                        )
                    }
                    Screen.TrainerSelect -> NavEntry(currentScreen) {
                        TrainerSelectScreen(
                            onSelectTrainer = { trainerId ->
                                backStack.add(Screen.TeamSelectForBattle(trainerId))
                            }
                        )
                    }
                    is Screen.TeamSelectForBattle -> NavEntry(currentScreen) {
                        TeamSelectForBattleScreen(
                            trainerId = currentScreen.trainerId,
                            onTeamSelected = { teamId ->
                                backStack.add(Screen.BattleVS(currentScreen.trainerId, teamId))
                            }
                        )
                    }
                    is Screen.BattleVS -> NavEntry(currentScreen) {
                        BattleVSScreen(
                            trainerId = currentScreen.trainerId,
                            playerTeamId = currentScreen.playerTeamId,
                            onVSComplete = {
                                backStack.removeLastOrNull()
                                backStack.add(Screen.Battle(currentScreen.trainerId, currentScreen.playerTeamId))
                            }
                        )
                    }
                    is Screen.Battle -> NavEntry(currentScreen) {
                        BattleScreen(
                            trainerId = currentScreen.trainerId,
                            playerTeamId = currentScreen.playerTeamId,
                            onBattleEnd = { recordId ->
                                backStack.removeLastOrNull()
                                backStack.add(Screen.BattleResult(recordId))
                            }
                        )
                    }
                    is Screen.BattleResult -> NavEntry(currentScreen) {
                        BattleResultScreen(
                            battleRecordId = currentScreen.battleRecordId,
                            onReturnToMenu = {
                                while (backStack.size > 1) backStack.removeLastOrNull()
                            }
                        )
                    }
                    Screen.Records -> NavEntry(currentScreen) {
                        RecordsScreen(
                            onTrainerDetail = { trainerId ->
                                backStack.add(Screen.TrainerRecords(trainerId))
                            }
                        )
                    }
                    is Screen.TrainerRecords -> NavEntry(currentScreen) {
                        TrainerRecordsScreen(trainerId = currentScreen.trainerId)
                    }
                }
            }
        }
    }
}
