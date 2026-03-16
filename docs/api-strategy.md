# Estrategia de API — PokéAPI Sync

## Base URL
`https://pokeapi.co/api/v2/`

---

## Orden de Sync

```
1. Types (18 tipos) → TypeEffectiveness table
2. Moves (colectar IDs únicos de los Pokémon) → Move table
3. Pokémon 1–386 → Pokemon table + sprites filesystem
4. PokemonMoves (relación pokemon ↔ move ya descargados)
```

**Razón del orden:** Los moves deben existir antes de crear PokemonMoves. Los tipos deben existir para typeEffectiveness antes de las batallas.

---

## Endpoints y Modelos

### 1. Tipos
**Endpoint:** `GET /type/{id}` para id 1–18 (skip id 10001, 10002 — tipos shadow/unknown)

**Campos que usar:**
```json
{
  "name": "fire",
  "damage_relations": {
    "double_damage_to": [{"name": "grass"}, {"name": "ice"}, ...],
    "half_damage_to": [{"name": "fire"}, {"name": "water"}, ...],
    "no_damage_to": [{"name": "rock"}, ...]
  }
}
```

**Generación de TypeEffectiveness:**
- `double_damage_to` → multiplier = 2.0
- `half_damage_to` → multiplier = 0.5
- `no_damage_to` → multiplier = 0.0
- Pares no listados → multiplier = 1.0 (no se insertan en DB)

**IDs de tipos válidos (1-18):**
normal=1, fighting=2, flying=3, poison=4, ground=5, rock=6, bug=7, ghost=8,
steel=9, fire=10, water=11, grass=12, electric=13, psychic=14, ice=15,
dragon=16, dark=17, fairy=18

---

### 2. Moves
**Estrategia:** No descargar todos los moves de PokéAPI (hay miles). Solo descargar los moves que aparecen en el learnset de los 386 Pokémon.

**Proceso:**
1. Al descargar cada Pokémon, colectar sus `move.move.url` → extraer IDs únicos
2. Acumular un `Set<Int>` de move IDs
3. Después de descargar los 386 Pokémon, descargar cada move único
4. **Filtro:** Solo persistir moves donde `power != null && power > 0` y `damage_class` in `["physical", "special"]`

**Endpoint:** `GET /move/{id}`

**Campos que usar:**
```json
{
  "id": 53,
  "name": "flamethrower",
  "type": {"name": "fire"},
  "damage_class": {"name": "special"},
  "power": 90,
  "accuracy": 100,
  "pp": 15,
  "priority": 0
}
```

**DTO → Entity mapping:**
- `damage_class.name` → `category` ("physical" | "special" | "status")
- Si `power` es null → no insertar (filtrar status moves)
- `accuracy` puede ser null (moves que siempre aciertan) → guardarlo como null

---

### 3. Pokémon
**Endpoint:** `GET /pokemon/{id}` para id 1–386

**Campos que usar:**
```json
{
  "id": 1,
  "name": "bulbasaur",
  "sprites": {
    "front_default": "https://raw.githubusercontent.com/.../1.png"
  },
  "types": [
    {"slot": 1, "type": {"name": "grass"}},
    {"slot": 2, "type": {"name": "poison"}}
  ],
  "stats": [
    {"base_stat": 45, "stat": {"name": "hp"}},
    {"base_stat": 49, "stat": {"name": "attack"}},
    {"base_stat": 49, "stat": {"name": "defense"}},
    {"base_stat": 65, "stat": {"name": "special-attack"}},
    {"base_stat": 65, "stat": {"name": "special-defense"}},
    {"base_stat": 45, "stat": {"name": "speed"}}
  ],
  "moves": [
    {"move": {"name": "tackle", "url": "https://pokeapi.co/api/v2/move/33/"}}
  ]
}
```

**Generación (gen) por ID:**
- 1–151 → gen 1
- 152–251 → gen 2
- 252–386 → gen 3

**Sprites:** Descargar `front_default` a filesystem. Si `front_default` es null → skip sprite (dejar `spriteLocalPath = null`).

---

## Rate Limiting y Robustez

```
- Delay entre requests: 100ms
- Timeout por request: 10 segundos
- Reintentos: 3 intentos con backoff exponencial (1s, 2s, 4s)
- En caso de fallo definitivo en un Pokémon: loggear, continuar con el siguiente
- SyncStatus.lastSyncedPokemonId se actualiza tras cada Pokémon exitoso
- Al reanudar: empezar desde lastSyncedPokemonId + 1
```

---

## Descarga de Sprites

```
- HTTP GET a la URL de sprite
- Guardar en filesDir/sprites/{id}.png
- Si la descarga falla: dejar spriteLocalPath = null, no cancelar sync
- Coil puede hacer fallback a la URL remota si spriteLocalPath es null
```

---

## Progreso como Flow

```kotlin
sealed interface SyncProgress {
    data object Idle : SyncProgress
    data class InProgress(
        val phase: SyncPhase,
        val current: Int,
        val total: Int
    ) : SyncProgress
    data object Completed : SyncProgress
    data class Error(val message: String) : SyncProgress
}

enum class SyncPhase {
    TYPES, MOVES, POKEMON, SPRITES
}
```

Emitir `InProgress` tras cada item procesado para progreso granular.

---

## Configuración de Ktor Client

```kotlin
HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10_000
        connectTimeoutMillis = 5_000
    }
    install(Logging) {
        level = LogLevel.NONE  // BODY solo en debug si se necesita
    }
}
```
