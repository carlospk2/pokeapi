package com.example.pokeapi.features.pokedex.presentation

sealed interface PokedexEvent {
    data class SearchQueryChanged(val query: String) : PokedexEvent
    data class TypeFilterSelected(val type: String?) : PokedexEvent
    data class GenerationFilterSelected(val generation: Int?) : PokedexEvent
    data class PokemonClicked(val pokemonId: Int) : PokedexEvent
}
