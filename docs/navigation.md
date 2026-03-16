# Navegación — Árbol de Pantallas

## Tecnología

Navigation 3 (`navigation3-ui 1.1.0-alpha04`) con back stack como `mutableStateListOf<Screen>`.

---

## Sealed Interface Screen

```kotlin
// composeApp/src/commonMain/.../navigation/Screen.kt

sealed interface Screen {

    // Primera pantalla si la DB no tiene datos sincronizados
    data object Sync : Screen

    // Menú principal — destino raíz tras sync exitoso
    data object MainMenu : Screen

    // --- Pokédex ---
    data object Pokedex : Screen
    data class PokemonDetail(val pokemonId: Int) : Screen

    // --- Equipos ---
    data object TeamList : Screen
    data class TeamEdit(val teamId: Long? = null) : Screen    // null = crear nuevo
    // Selector de Pokémon para un slot del equipo (reutiliza Pokédex en modo selección)
    data class PokemonSelector(val teamId: Long, val slot: Int) : Screen
    // Selector de movimientos para un Pokémon de un equipo
    data class MoveSelector(
        val teamId: Long,
        val slot: Int,
        val pokemonId: Int
    ) : Screen

    // --- Batalla ---
    data object TrainerSelect : Screen
    // Selección de equipo antes de la batalla
    data class TeamSelectForBattle(val trainerId: Int) : Screen
    // Pantalla de VS (animación de entrada)
    data class BattleVS(val trainerId: Int, val playerTeamId: Long) : Screen
    // Batalla activa
    data class Battle(val trainerId: Int, val playerTeamId: Long) : Screen
    // Resultado final
    data class BattleResult(val battleRecordId: Long) : Screen

    // --- Récords ---
    data object Records : Screen
    // Detalle de historial de un entrenador específico
    data class TrainerRecords(val trainerId: Int) : Screen
}
```

---

## Árbol de Navegación

```
App start
 └── Sync (si DB vacía)
      └── MainMenu ←──────────────────────────────┐
           ├── Pokédex                             │
           │    └── PokemonDetail(id)              │
           │                                       │
           ├── TeamList                            │
           │    ├── TeamEdit(null)  [nuevo equipo]  │
           │    └── TeamEdit(id)   [editar]        │
           │         ├── PokemonSelector(teamId, slot)  │
           │         │    └── [selecciona → vuelve a TeamEdit]
           │         └── MoveSelector(teamId, slot, pokemonId)
           │              └── [selecciona → vuelve a TeamEdit]
           │                                       │
           ├── TrainerSelect                       │
           │    └── TeamSelectForBattle(trainerId) │
           │         └── BattleVS(trainerId, teamId)
           │              └── Battle(trainerId, teamId)
           │                   └── BattleResult(recordId)
           │                        └── ──────────┘ (vuelve a MainMenu)
           │
           └── Records
                └── TrainerRecords(trainerId)
```

---

## Implementación en App.kt

```kotlin
@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()

    // Determinar pantalla inicial
    val syncCompleted by syncViewModel.isSyncCompleted.collectAsStateWithLifecycle()
    val initialScreen = if (syncCompleted) Screen.MainMenu else Screen.Sync

    val backStack = remember(initialScreen) {
        mutableStateListOf<Screen>(initialScreen)
    }

    PokeapiTheme {
        NavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() }
        ) { screen ->
            when (screen) {
                Screen.Sync -> NavEntry(screen) {
                    SyncScreen(onSyncComplete = {
                        backStack.clear()
                        backStack.add(Screen.MainMenu)
                    })
                }
                Screen.MainMenu -> NavEntry(screen) {
                    MainMenuScreen(
                        onPokedex = { backStack.add(Screen.Pokedex) },
                        onTeams = { backStack.add(Screen.TeamList) },
                        onBattle = { backStack.add(Screen.TrainerSelect) },
                        onRecords = { backStack.add(Screen.Records) }
                    )
                }
                Screen.Pokedex -> NavEntry(screen) {
                    PokedexScreen(
                        onPokemonClick = { id -> backStack.add(Screen.PokemonDetail(id)) }
                    )
                }
                is Screen.PokemonDetail -> NavEntry(screen) {
                    PokemonDetailScreen(pokemonId = screen.pokemonId)
                }
                Screen.TeamList -> NavEntry(screen) {
                    TeamListScreen(
                        onNewTeam = { backStack.add(Screen.TeamEdit(null)) },
                        onEditTeam = { id -> backStack.add(Screen.TeamEdit(id)) }
                    )
                }
                is Screen.TeamEdit -> NavEntry(screen) {
                    TeamEditScreen(
                        teamId = screen.teamId,
                        onSelectPokemon = { teamId, slot ->
                            backStack.add(Screen.PokemonSelector(teamId, slot))
                        },
                        onSelectMoves = { teamId, slot, pokemonId ->
                            backStack.add(Screen.MoveSelector(teamId, slot, pokemonId))
                        },
                        onSaved = { backStack.removeLastOrNull() }
                    )
                }
                is Screen.PokemonSelector -> NavEntry(screen) {
                    PokemonSelectorScreen(
                        teamId = screen.teamId,
                        slot = screen.slot,
                        onSelected = { backStack.removeLastOrNull() }
                    )
                }
                is Screen.MoveSelector -> NavEntry(screen) {
                    MoveSelectorScreen(
                        teamId = screen.teamId,
                        slot = screen.slot,
                        pokemonId = screen.pokemonId,
                        onSaved = { backStack.removeLastOrNull() }
                    )
                }
                Screen.TrainerSelect -> NavEntry(screen) {
                    TrainerSelectScreen(
                        onSelectTrainer = { trainerId ->
                            backStack.add(Screen.TeamSelectForBattle(trainerId))
                        }
                    )
                }
                is Screen.TeamSelectForBattle -> NavEntry(screen) {
                    TeamSelectForBattleScreen(
                        trainerId = screen.trainerId,
                        onTeamSelected = { teamId ->
                            backStack.add(Screen.BattleVS(screen.trainerId, teamId))
                        }
                    )
                }
                is Screen.BattleVS -> NavEntry(screen) {
                    BattleVSScreen(
                        trainerId = screen.trainerId,
                        playerTeamId = screen.playerTeamId,
                        onVSComplete = {
                            // Reemplazar VS con Battle (no queremos back a VS)
                            backStack.removeLastOrNull()
                            backStack.add(Screen.Battle(screen.trainerId, screen.playerTeamId))
                        }
                    )
                }
                is Screen.Battle -> NavEntry(screen) {
                    BattleScreen(
                        trainerId = screen.trainerId,
                        playerTeamId = screen.playerTeamId,
                        onBattleEnd = { recordId ->
                            backStack.removeLastOrNull()
                            backStack.add(Screen.BattleResult(recordId))
                        }
                    )
                }
                is Screen.BattleResult -> NavEntry(screen) {
                    BattleResultScreen(
                        battleRecordId = screen.battleRecordId,
                        onReturnToMenu = {
                            // Limpiar hasta MainMenu
                            while (backStack.size > 1) backStack.removeLastOrNull()
                        }
                    )
                }
                Screen.Records -> NavEntry(screen) {
                    RecordsScreen(
                        onTrainerDetail = { trainerId ->
                            backStack.add(Screen.TrainerRecords(trainerId))
                        }
                    )
                }
                is Screen.TrainerRecords -> NavEntry(screen) {
                    TrainerRecordsScreen(trainerId = screen.trainerId)
                }
            }
        }
    }
}
```

---

## Back Navigation

- **Back físico / gesto:** `onBack = { if (backStack.size > 1) backStack.removeLastOrNull() }`
- **Desde BattleResult:** Solo se puede volver al MainMenu (back stack limpiado)
- **Desde BattleVS:** Se reemplaza en el stack para que Back desde Battle no vuelva a VS
- **Desde PokemonSelector / MoveSelector:** `removeLastOrNull()` vuelve a TeamEdit
