package com.example.pokedex.api

import com.example.pokedex.base.repository.Endpoint
import com.example.pokedex.model.Pokemon
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonEndpoint : Endpoint {
    @GET("pokemon/{id}/")
    fun getById(@Path("id") id: Int): Observable<Pokemon>

    @GET("pokemon/{name}/")
    fun getByName(@Path("name") name: String): Observable<Pokemon>
}