package com.zanfolin.pokedex.base.model

import com.zanfolin.pokedex.base.util.takeBodyAndSendToMainThread
import com.zanfolin.pokedex.base.domain.Pokemon
import io.reactivex.Observable

class PokemonRepository(val endpoint: PokemonEndpoint) {

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