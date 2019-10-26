package com.zanfolin.pokedex.base.model.api.pokeapi

import com.zanfolin.pokedex.base.model.API
import com.zanfolin.pokedex.base.model.APIConfiguration
import com.zanfolin.pokedex.base.model.Hosts.POKE_API

class PokeAPI(config: APIConfiguration) : API(config) {

    override fun getHost() = POKE_API.getHost()

}