package com.example.pokeapi.shared.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.example.pokeapi.shared.database.dao.BattleRecordDao
import com.example.pokeapi.shared.database.dao.MoveDao
import com.example.pokeapi.shared.database.dao.PlayerStatsDao
import com.example.pokeapi.shared.database.dao.PokemonDao
import com.example.pokeapi.shared.database.dao.PokemonMoveDao
import com.example.pokeapi.shared.database.dao.SyncStatusDao
import com.example.pokeapi.shared.database.dao.TeamDao
import com.example.pokeapi.shared.database.dao.TeamMemberDao
import com.example.pokeapi.shared.database.dao.TrainerDao
import com.example.pokeapi.shared.database.dao.TypeEffectivenessDao
import com.example.pokeapi.shared.database.entity.BattleRecordEntity
import com.example.pokeapi.shared.database.entity.MoveEntity
import com.example.pokeapi.shared.database.entity.PlayerStatsEntity
import com.example.pokeapi.shared.database.entity.PokemonEntity
import com.example.pokeapi.shared.database.entity.PokemonMoveEntity
import com.example.pokeapi.shared.database.entity.SyncStatusEntity
import com.example.pokeapi.shared.database.entity.TeamEntity
import com.example.pokeapi.shared.database.entity.TeamMemberEntity
import com.example.pokeapi.shared.database.entity.TrainerEntity
import com.example.pokeapi.shared.database.entity.TypeEffectivenessEntity

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
@ConstructedBy(AppDatabaseConstructor::class)
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

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
