package com.example.pokeapi.features.pokedex.domain.usecase

import com.example.pokeapi.features.pokedex.domain.model.Pokemon
import com.example.pokeapi.features.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

class GetPokemonListUseCase(private val repository: PokemonRepository) {
    operator fun invoke(query: String = "", type: String? = null, generation: Int? = null): Flow<List<Pokemon>> {
        return when {
            query.isNotBlank() -> repository.searchByName(query)
            type != null -> repository.getByType(type)
            generation != null -> repository.getByGeneration(generation)
            else -> repository.getAll()
        }
    }
}
