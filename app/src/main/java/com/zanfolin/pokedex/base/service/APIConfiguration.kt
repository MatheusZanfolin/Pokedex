package com.zanfolin.pokedex.base.service

import okhttp3.HttpUrl
import retrofit2.Retrofit

interface APIConfiguration {

    fun setup(baseUrl: HttpUrl): Retrofit

}