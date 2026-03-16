package com.example.pokeapi.features.pokedex.domain.repository

import com.example.pokeapi.features.pokedex.domain.model.Pokemon
import com.example.pokeapi.features.pokedex.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getAll(): Flow<List<Pokemon>>
    fun searchByName(query: String): Flow<List<Pokemon>>
    fun getByType(type: String): Flow<List<Pokemon>>
    fun getByGeneration(generation: Int): Flow<List<Pokemon>>
    suspend fun getDetail(id: Int): PokemonDetail?
}
