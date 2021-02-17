package com.zanfolin.pokedex.base.model.repository.pokemon.id

import com.zanfolin.pokedex.base.domain.Pokemon
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonByNameEndpoint {

    @GET("pokemon/{name}")
    fun getByName(@Path("name") name: String): Single<Response<Pokemon>>

}