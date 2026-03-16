package com.example.pokeapi.shared.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.pokeapi.shared.database.entity.MoveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoveDao {

    @Upsert
    suspend fun upsert(move: MoveEntity)

    @Upsert
    suspend fun upsertAll(moves: List<MoveEntity>)

    @Query("SELECT * FROM moves WHERE id = :id")
    suspend fun getById(id: Int): MoveEntity?

    @Query("SELECT * FROM moves WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): MoveEntity?

    @Query("""
        SELECT m.* FROM moves m
        INNER JOIN pokemon_moves pm ON m.id = pm.moveId
        WHERE pm.pokemonId = :pokemonId
        ORDER BY m.name ASC
    """)
    fun getMovesByPokemon(pokemonId: Int): Flow<List<MoveEntity>>

    @Query("SELECT * FROM moves ORDER BY name ASC")
    fun getAll(): Flow<List<MoveEntity>>
}
