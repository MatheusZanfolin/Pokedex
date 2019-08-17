package com.zanfolin.pokedex

import com.zanfolin.pokedex.base.api.PokemonEndpoint
import com.zanfolin.pokedex.base.api.PokemonRepository
import com.zanfolin.pokedex.base.util.Region
import com.zanfolin.pokedex.feature.list.viewmodel.PokemonViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://pokeapi.co/api/v2/"

private val RETROFIT = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val pokemonModule = module {
    single { PokemonRepository(RETROFIT.create(PokemonEndpoint::class.java)) }
    viewModel { (region: Region) -> PokemonViewModel(region, get()) }
}