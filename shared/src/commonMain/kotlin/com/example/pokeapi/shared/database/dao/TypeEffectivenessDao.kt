package com.example.pokeapi.shared.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.pokeapi.shared.database.entity.TypeEffectivenessEntity

@Dao
interface TypeEffectivenessDao {

    @Upsert
    suspend fun upsert(typeEffectiveness: TypeEffectivenessEntity)

    @Upsert
    suspend fun upsertAll(typeEffectiveness: List<TypeEffectivenessEntity>)

    @Query("SELECT multiplier FROM type_effectiveness WHERE attackingType = :attackingType AND defendingType = :defendingType LIMIT 1")
    suspend fun getMultiplier(attackingType: String, defendingType: String): Float?

    @Query("SELECT * FROM type_effectiveness")
    suspend fun getAll(): List<TypeEffectivenessEntity>
}
