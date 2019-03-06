package com.example.pokedex.api

import com.example.pokedex.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {
    @GET("pokemon/{id}/")
    fun getById(@Path("id") id: Int): Call<Pokemon>

    @GET("pokemon/{name}/")
    fun getByName(@Path("name") name: String): Call<Pokemon>
}