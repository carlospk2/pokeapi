package com.example.pokeapi.shared.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.pokeapi.shared.database.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Upsert
    suspend fun upsert(pokemon: PokemonEntity)

    @Upsert
    suspend fun upsertAll(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAll(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getById(id: Int): PokemonEntity?

    @Query("SELECT * FROM pokemon WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): PokemonEntity?

    @Query("SELECT * FROM pokemon WHERE typePrimary = :type OR typeSecondary = :type ORDER BY id ASC")
    fun getByType(type: String): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE generation = :generation ORDER BY id ASC")
    fun getByGeneration(generation: Int): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%' ORDER BY id ASC")
    fun searchByName(query: String): Flow<List<PokemonEntity>>

    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun count(): Int
}
