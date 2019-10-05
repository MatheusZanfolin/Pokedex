package com.zanfolin.pokedex

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val POKEMON_SERVICE_URL = "https://pokeapi.co/api/v2/"

// private const val MOCK_URL = "localhost:2526//my.mock.domain.here"

val POKEMON_SERVICE = Retrofit.Builder()
    .baseUrl(POKEMON_SERVICE_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()