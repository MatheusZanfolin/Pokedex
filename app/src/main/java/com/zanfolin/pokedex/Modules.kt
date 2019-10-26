package com.zanfolin.pokedex

import com.zanfolin.pokedex.base.model.api.pokeapi.PokeAPIConfiguration
import com.zanfolin.pokedex.base.model.api.pokeapi.PokeAPI
import com.zanfolin.pokedex.base.model.repository.pokemon.name.PokemonByIdRepository
import com.zanfolin.pokedex.base.util.Region
import com.zanfolin.pokedex.feature.list.viewmodel.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pokemonModule = module {
    single { PokemonByIdRepository(PokeAPI(PokeAPIConfiguration())) }
    viewModel { (region: Region) -> PokemonListViewModel(region, get()) }
}