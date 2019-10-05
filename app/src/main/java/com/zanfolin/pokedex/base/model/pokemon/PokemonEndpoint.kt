package com.zanfolin.pokedex.base.model.pokemon

import com.zanfolin.pokedex.base.domain.Pokemon
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonEndpoint {
    @GET("pokemon/{id}/")
    fun getById(@Path("id") id: Int): Observable<Response<Pokemon>>

    @GET("pokemon/{name}/")
    fun getByName(@Path("name") name: String): Observable<Response<Pokemon>>
}