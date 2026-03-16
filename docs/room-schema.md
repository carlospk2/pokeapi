# Room Schema — Definición Exacta

## Versión de base de datos: 1
## Archivo: `shared/.../shared/database/AppDatabase.kt`

---

## Entidades

### PokemonEntity
```kotlin
@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,                  // número de Pokédex (1-386)
    val name: String,                          // lowercase, ej: "bulbasaur"
    val spriteUrl: String,                     // URL original de PokéAPI
    val spriteLocalPath: String? = null,       // ruta relativa en filesystem, ej: "sprites/1.png"
    val typePrimary: String,                   // lowercase, ej: "grass"
    val typeSecondary: String? = null,
    val baseHp: Int,
    val baseAttack: Int,
    val baseDefense: Int,
    val baseSpAttack: Int,
    val baseSpDefense: Int,
    val baseSpeed: Int,
    val generation: Int                        // 1, 2 o 3
)
```
**Índices:** ninguno adicional (PK es suficiente para acceso por ID)

---

### MoveEntity
```kotlin
@Entity(tableName = "moves")
data class MoveEntity(
    @PrimaryKey val id: Int,
    val name: String,                          // lowercase, ej: "flamethrower"
    val type: String,                          // lowercase, ej: "fire"
    val category: String,                      // "physical" | "special" | "status"
    val power: Int? = null,                    // null para status moves
    val accuracy: Int? = null,                 // null para moves que nunca fallan
    val pp: Int,
    val priority: Int                          // default 0
)
```

---

### PokemonMoveEntity
```kotlin
@Entity(
    tableName = "pokemon_moves",
    primaryKeys = ["pokemonId", "moveId"],
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pokemonId"), Index("moveId")]
)
data class PokemonMoveEntity(
    val pokemonId: Int,
    val moveId: Int
)
```

---

### TypeEffectivenessEntity
```kotlin
@Entity(
    tableName = "type_effectiveness",
    primaryKeys = ["attackingType", "defendingType"]
)
data class TypeEffectivenessEntity(
    val attackingType: String,   // lowercase
    val defendingType: String,   // lowercase
    val multiplier: Float        // 0f, 0.5f, 1f, 2f
)
```
**Nota:** Se generan sólo las entradas con multiplier ≠ 1.0. En runtime, si no existe entrada para un par, asumir 1.0.

---

### TeamEntity
```kotlin
@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long,    // epoch millis
    val updatedAt: Long
)
```

---

### TeamMemberEntity
```kotlin
@Entity(
    tableName = "team_members",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("teamId")]
)
data class TeamMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val teamId: Long,
    val pokemonId: Int,
    val slot: Int,           // 1-6
    val level: Int,          // 1-100
    val move1Id: Int,
    val move2Id: Int? = null,
    val move3Id: Int? = null,
    val move4Id: Int? = null
)
```

---

### TrainerEntity
```kotlin
@Entity(tableName = "trainers")
data class TrainerEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val difficultyTier: Int,   // 1-4
    val aiStrategy: String,    // "easy" | "medium" | "hard"
    val teamJson: String       // JSON: List<TrainerMemberJson>
)

// Modelo auxiliar para deserializar teamJson (no es entidad Room):
data class TrainerMemberJson(
    val pokemonId: Int,
    val level: Int,
    val moveNames: List<String>   // nombres lowercase, se resuelven a IDs en runtime
)
```
**Nota:** `teamJson` almacena moveNames (no IDs) para no depender de IDs hardcodeados. Se resuelven contra la tabla `moves` al iniciar la batalla.

---

### BattleRecordEntity
```kotlin
@Entity(tableName = "battle_records")
data class BattleRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trainerId: Int,
    val playerTeamId: Long,
    val result: String,           // "win" | "loss"
    val turnsCount: Int,
    val date: Long,               // epoch millis
    val pokemonRemaining: Int     // Pokémon del jugador con HP > 0 al terminar
)
```

---

### PlayerStatsEntity
```kotlin
@Entity(tableName = "player_stats")
data class PlayerStatsEntity(
    @PrimaryKey val id: Int = 1,    // singleton
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalBattles: Int = 0
)
```

---

### SyncStatusEntity
```kotlin
@Entity(tableName = "sync_status")
data class SyncStatusEntity(
    @PrimaryKey val id: Int = 1,    // singleton
    val isCompleted: Boolean = false,
    val lastSyncedPokemonId: Int = 0,    // último pokemon_id descargado exitosamente
    val lastSyncedMoveId: Int = 0        // último move_id descargado exitosamente
)
```

---

## AppDatabase

```kotlin
@Database(
    entities = [
        PokemonEntity::class,
        MoveEntity::class,
        PokemonMoveEntity::class,
        TypeEffectivenessEntity::class,
        TeamEntity::class,
        TeamMemberEntity::class,
        TrainerEntity::class,
        BattleRecordEntity::class,
        PlayerStatsEntity::class,
        SyncStatusEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun moveDao(): MoveDao
    abstract fun pokemonMoveDao(): PokemonMoveDao
    abstract fun typeEffectivenessDao(): TypeEffectivenessDao
    abstract fun teamDao(): TeamDao
    abstract fun teamMemberDao(): TeamMemberDao
    abstract fun trainerDao(): TrainerDao
    abstract fun battleRecordDao(): BattleRecordDao
    abstract fun playerStatsDao(): PlayerStatsDao
    abstract fun syncStatusDao(): SyncStatusDao
}
```

---

## Estrategia de Migración

- **Versión 1:** Sin migraciones (primera versión).
- **Cambios futuros:** Usar `Migration(old, new)` con SQL explícito. No usar `fallbackToDestructiveMigration()`.

---

## Filesystem de Sprites

- **Directorio base:** `Context.filesDir/sprites/` (Android) / `NSFileManager documentDirectory/sprites/` (iOS)
- **Naming:** `{pokemonId}.png` — ej: `sprites/1.png`, `sprites/25.png`
- **Acceso en Coil:** URI `file:///data/user/0/.../files/sprites/1.png`
  En Compose usar `ImageRequest.Builder` con `data(File(spriteLocalPath))`
- **spriteLocalPath** en DB guarda la ruta absoluta completa al momento de descarga
