package com.zanfolin.pokedex.base.model.repository.pokemon.name

import com.zanfolin.pokedex.base.domain.Pokemon
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonByIdEndpoint {

    @GET("pokemon/{id}/")
    fun getById(@Path("id") id: Int): Observable<Response<Pokemon>>

}