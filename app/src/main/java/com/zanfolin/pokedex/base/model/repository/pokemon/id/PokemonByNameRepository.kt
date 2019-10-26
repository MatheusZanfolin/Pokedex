package com.zanfolin.pokedex.base.model.repository.pokemon.id

import com.zanfolin.pokedex.base.domain.Pokemon
import com.zanfolin.pokedex.base.model.API
import com.zanfolin.pokedex.base.model.Repository
import com.zanfolin.pokedex.base.util.takeBodyAndSendToMainThread
import io.reactivex.Single

class PokemonByNameRepository(api: API) : Repository<PokemonByNameEndpoint>(api) {

    fun getPokemonByName(name: String): Single<Pokemon> {
        return endpoint
            .getByName(name)
            .takeBodyAndSendToMainThread()
    }

}