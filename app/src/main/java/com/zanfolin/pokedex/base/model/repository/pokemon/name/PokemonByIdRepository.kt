package com.zanfolin.pokedex.base.model.repository.pokemon.name

import com.zanfolin.pokedex.base.domain.Pokemon
import com.zanfolin.pokedex.base.model.API
import com.zanfolin.pokedex.base.model.Repository
import com.zanfolin.pokedex.base.util.takeBodyAndSendToMainThread
import io.reactivex.Observable

class PokemonByIdRepository(api: API) : Repository<PokemonByIdEndpoint>(api) {

    fun getPokemonById(id: Int): Observable<Pokemon> {
        return endpoint
            .getById(id)
            .takeBodyAndSendToMainThread()
    }

}