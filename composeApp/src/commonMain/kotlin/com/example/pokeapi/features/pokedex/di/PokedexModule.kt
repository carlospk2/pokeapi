package com.example.pokeapi.features.pokedex.di

import com.example.pokeapi.features.pokedex.data.repository.PokemonRepositoryImpl
import com.example.pokeapi.features.pokedex.domain.repository.PokemonRepository
import com.example.pokeapi.features.pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.pokeapi.features.pokedex.domain.usecase.GetPokemonListUseCase
import com.example.pokeapi.features.pokedex.presentation.PokedexViewModel
import com.example.pokeapi.features.pokedex.presentation.PokemonDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val pokedexModule = module {
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }
    viewModelOf(::PokedexViewModel)
    viewModel { params -> PokemonDetailViewModel(get(), params.get()) }
}
