package com.zanfolin.pokedex.base.model

import okhttp3.HttpUrl

abstract class API(private val config: APIConfiguration) {

    abstract fun getHost(): HttpUrl

    fun getService() = config.setup(getHost())

}