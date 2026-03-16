package com.example.pokeapi.shared.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.pokeapi.shared.database.entity.PokemonMoveEntity

@Dao
interface PokemonMoveDao {

    @Upsert
    suspend fun upsert(pokemonMove: PokemonMoveEntity)

    @Upsert
    suspend fun upsertAll(pokemonMoves: List<PokemonMoveEntity>)

    @Query("SELECT * FROM pokemon_moves WHERE pokemonId = :pokemonId")
    suspend fun getMovesForPokemon(pokemonId: Int): List<PokemonMoveEntity>
}
