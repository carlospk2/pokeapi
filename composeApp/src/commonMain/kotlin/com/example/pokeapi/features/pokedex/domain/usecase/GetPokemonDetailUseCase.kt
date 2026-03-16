package com.example.pokeapi.features.pokedex.domain.usecase

import com.example.pokeapi.features.pokedex.domain.model.PokemonDetail
import com.example.pokeapi.features.pokedex.domain.repository.PokemonRepository

class GetPokemonDetailUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(id: Int): PokemonDetail? = repository.getDetail(id)
}
