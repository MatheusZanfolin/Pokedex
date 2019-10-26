package com.zanfolin.pokedex.base.model.pokemon

import com.zanfolin.pokedex.base.service.API
import com.zanfolin.pokedex.base.service.APIConfiguration
import com.zanfolin.pokedex.base.service.Hosts.POKE_API

class PokemonAPI(config: APIConfiguration) : API(config) {

    override fun getHost() = POKE_API.getHost()

}