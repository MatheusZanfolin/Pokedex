package com.zanfolin.pokedex.base.model

import okhttp3.HttpUrl

enum class Hosts(private val url: String) {

    POKE_API("https://pokeapi.co/api/v2/");

    fun getHost() = HttpUrl.get(url)

}