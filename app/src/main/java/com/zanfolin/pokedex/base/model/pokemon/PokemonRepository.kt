package com.zanfolin.pokedex.base.model.pokemon

import com.zanfolin.pokedex.base.domain.Pokemon
import com.zanfolin.pokedex.base.model.Repository
import com.zanfolin.pokedex.base.util.takeBodyAndSendToMainThread
import io.reactivex.Observable
import retrofit2.Retrofit

class PokemonRepository(service: Retrofit) : Repository<PokemonEndpoint>(service) {

    fun getPokemonById(id: Int): Observable<Pokemon> {
        return endpoint
            .getById(id)
            .takeBodyAndSendToMainThread()
    }

    fun getPokemonByName(name: String): Observable<Pokemon> {
        return endpoint
            .getByName(name)
            .takeBodyAndSendToMainThread()
    }

}