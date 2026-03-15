# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Kotlin Multiplatform (KMP) + Compose Multiplatform (CMP) project targeting **Android and iOS**.

- **Package:** `com.example.kmm_jcmm_test`
- **Min SDK:** 26 | **Target/Compile SDK:** 36 | **iOS min:** 16
- **Kotlin:** 2.3.10 | **AGP:** 9.1.0 | **Gradle:** 9.4.0 | **CMP:** 1.10.2 | **JVM toolchain:** 11

## Common Commands

```bash
# Build Android APK
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:assembleRelease

# Build shared module (Android)
./gradlew :shared:compileDebugKotlinAndroid

# Unit tests
./gradlew :shared:testDebugUnitTest
./gradlew :composeApp:testDebugUnitTest

# Install on device
./gradlew :composeApp:installDebug

# Full clean build
./gradlew clean :composeApp:assembleDebug
```

## Module Architecture

```
composeApp/   — Compose Multiplatform UI layer (Android app + iOS framework)
shared/       — KMP business logic: database, networking, DI
iosApp/       — Swift iOS entry point (wraps the ComposeApp framework)
```

**`composeApp`** applies `kotlin.multiplatform` + `com.android.application` + `org.jetbrains.compose`. Contains all Compose UI code. Produces an Android APK and an iOS `ComposeApp.framework`.

**`shared`** applies `kotlin.multiplatform` + `com.android.library` + `androidx.room` + `ksp` + `kotlin.serialization`. Business logic only — no UI. Produces the `Shared.framework` consumed by iOS.

## Key Layers in `shared`

| Package | Contents |
|---|---|
| `shared.database` | Room: `AppDatabase`, entities, DAOs. `DatabaseFactory.kt` centraliza `setDriver` + `setQueryCoroutineContext` |
| `shared.network` | `createHttpClient()` — Ktor client factory con ContentNegotiation y Logging |
| `shared.di` | `initKoin()`, `sharedModule`, `databaseModule`, `platformModule` (expect/actual) |

## Koin Initialization

Koin se inicializa una sola vez con guard (`GlobalContext.getOrNull()`):

- **Android:** `KmmApplication.onCreate()` llama `initKoin { androidContext(this) }`
- **iOS:** `MainViewController()` llama `initKoin()` (sin contexto)
- **Agregar módulos de feature:** pasarlos en el bloque de `initKoin` en la Application/MainViewController, o via `loadKoinModules()` en runtime

## Architecture Guidelines (Clean Architecture + UDF)

### Estructura de una Feature

```
features/
└── myfeature/
    ├── domain/
    │   ├── model/          # Modelos de negocio puros (Kotlin only, sin @Entity ni @Serializable)
    │   ├── repository/     # Interfaces de repositorio
    │   └── usecase/        # Un use case = una operación de negocio
    ├── data/
    │   ├── remote/dto/     # DTOs con @Serializable para Ktor
    │   ├── local/entity/   # Entities con @Entity para Room
    │   ├── mapper/         # Funciones de conversión entre capas
    │   ├── remote/         # API Service (Ktor)
    │   └── repository/     # Implementación de las interfaces del domain
    └── presentation/
        ├── MyScreenState.kt   # data class inmutable con todo el estado de UI
        ├── MyScreenEvent.kt   # sealed interface con acciones del usuario
        ├── MyScreenEffect.kt  # sealed interface para eventos one-shot (navegación, snackbar)
        ├── MyViewModel.kt     # ViewModel que orquesta domain + state
        └── MyScreen.kt        # Composable que observa state y emite eventos
```

### Reglas de Dependencia

```
Presentation → Domain ← Data
```

- **Domain:** Kotlin puro. Sin dependencias de frameworks. Define modelos, interfaces de repositorio y use cases.
- **Data:** Implementa las interfaces del domain usando Ktor (API), Room (DB) y mappers.
- **Presentation:** Consume use cases vía ViewModel. Nunca accede directamente a Data.

### Patrones Obligatorios

**Offline-First / Single Source of Truth:**
- La UI siempre observa un `Flow` de Room (nunca datos directos de la API)
- La API se usa solo para `refresh` → guarda en Room con `upsert` → Room emite automáticamente

**Unidirectional Data Flow (UDF):**
```
User Action → Event → ViewModel → (UseCase) → State update → UI recomposes
```

**ViewModel con StateFlow:**
```kotlin
// CORRECTO — update atómico
_state.update { it.copy(isLoading = false, items = items) }

// INCORRECTO — no atómico
_state.value = _state.value.copy(isLoading = false)
_state.value = _state.value.copy(items = items)
```

**UI State / Event / Effect:**
```kotlin
data class MyState(val items: List<Item> = emptyList(), val isLoading: Boolean = false)
sealed interface MyEvent { data object Refresh : MyEvent; data class Delete(val id: Long) : MyEvent }
sealed interface MyEffect { data class ShowSnackbar(val msg: String) : MyEffect }

class MyViewModel(private val getItems: GetItemsUseCase) : ViewModel() {
    private val _state = MutableStateFlow(MyState())
    val state: StateFlow<MyState> = _state.asStateFlow()
    private val _effect = Channel<MyEffect>()
    val effect = _effect.receiveAsFlow()
}
```

**Composable:**
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.effect.collect { /* handle */ } }
    MyContent(state = state, onEvent = viewModel::onEvent)
}
```

### Koin Module por Feature

```kotlin
val myFeatureModule = module {
    // Data
    single { MyApiService(get()) }
    single<MyRepository> { MyRepositoryImpl(get(), get()) }
    // Domain
    factory { GetItemsUseCase(get()) }
    // Presentation
    viewModelOf(::MyViewModel)
}
```

Registrar en `initKoin { modules(..., myFeatureModule) }`.

### Use Cases

```kotlin
class GetItemsUseCase(private val repo: ItemRepository) {
    operator fun invoke(): Flow<List<Item>> = repo.getItems()
}
```

Usar `operator fun invoke()` para llamarlos como función: `getItemsUseCase()`.

### Mappers

Siempre convertir entre capas con funciones de extensión en un archivo `Mapper.kt`:
```kotlin
fun ItemDto.toEntity(): ItemEntity = ItemEntity(id = id, name = name)
fun ItemEntity.toDomain(): Item = Item(id = id, name = name)
```

## Navigation

Este proyecto usa **Navigation 3** (`navigation3-ui 1.1.0-alpha04`) en lugar de Jetpack Navigation Compose. Navigation 3 es el enfoque futuro de CMP con back stack como lista mutable:

```kotlin
val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }

NavDisplay(
    backStack = backStack,
    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() }
) { screen ->
    when (screen) {
        Screen.Home -> NavEntry(screen) { HomeScreen() }
        is Screen.Detail -> NavEntry(screen) { DetailScreen(screen.id) }
    }
}
```

Agregar nuevas rutas en `composeApp/src/commonMain/.../navigation/Screen.kt`.

> **Nota:** La guía de referencia del proyecto menciona Jetpack Navigation Compose, pero se optó por Navigation 3 por ser el sucesor oficial en CMP 1.10.x. El patrón UDF/ViewModel/State aplica igual con ambas.

## expect/actual Pattern

- `Platform.kt` — `expect fun getPlatform(): Platform`
- `AppDatabaseConstructor` — `expect object` con `@Suppress("NO_ACTUAL_FOR_EXPECT")`, el `actual` lo **genera KSP** (no escribirlo manualmente)
- `platformModule()` — `expect fun platformModule(): Module` para proveer el DB builder con contexto por plataforma
- `colorScheme()` — Material You en Android, colores estáticos en iOS
- cinterop calls (ej: `NSFileManager`) requieren `@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)`

## Dependency Management

Todas las versiones en **`gradle/libs.versions.toml`** — nunca inline. Quirks importantes:

- **CMP 1.10.x**: `compose.*` shorthand aliases están deprecados (error level). Usar siempre entradas explícitas del catálogo (`libs.compose.runtime`, etc.)
- **`compose-material3`** versión independiente del plugin CMP: plugin = `1.10.2`, artifact = `1.9.0`
- **Room KSP**: declarar por target en el bloque `dependencies {}` del módulo (fuera de `kotlin {}`): `add("kspAndroid", ...)`, `add("kspIosX64", ...)`, etc.
- **`navigation3-ui`** no tiene artefacto `iosX64` desde alpha02 — `composeApp` solo usa `iosArm64` + `iosSimulatorArm64`

## AGP 9 + KMP Compatibility Flags (gradle.properties)

```properties
android.builtInKotlin=false   # evita conflicto entre KMP plugin y AGP built-in Kotlin
android.newDsl=false          # requerido para com.android.library + kotlin.multiplatform
```

Estos flags son **obligatorios**. Eliminarlos rompe el build.

## iOS Development

**Run on simulator:** Abrir `iosApp/iosApp/iosApp.xcodeproj` en Xcode y correr. Xcode llama a Gradle automáticamente para compilar `ComposeApp.framework`.

**Swift entry point chain:** `iosAppApp.swift` → `ContentView.swift` → `ComposeView` → `MainViewControllerKt.MainViewController()` (Kotlin/CMP).

**iOS frameworks son estáticos** (`isStatic = true`). `shared` requiere `linkerOpts.add("-lsqlite3")` para Room.

## Commit Convention

Todos los commits siguen [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/):

```
<type>[optional scope]: <description>
```

Tipos: `feat`, `fix`, `chore`, `refactor`, `docs`, `test`. Ejemplo: `feat(auth): add login use case`.

## Adding New Dependencies

1. Versión en `[versions]` de `libs.versions.toml`
2. Entrada en `[libraries]`
3. Agregar al sourceSet correcto en el `build.gradle.kts` del módulo
4. Si requiere plugin Gradle: agregar a `[plugins]` y en el root `build.gradle.kts` con `apply false`
