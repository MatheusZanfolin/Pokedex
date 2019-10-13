package com.zanfolin.pokedex

import com.zanfolin.pokedex.base.model.pokemon.PokemonRepository
import com.zanfolin.pokedex.base.util.Region
import com.zanfolin.pokedex.feature.list.viewmodel.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pokemonModule = module {
    single { PokemonRepository(POKEMON_SERVICE) }
    viewModel { (region: Region) -> PokemonListViewModel(region, get()) }
}