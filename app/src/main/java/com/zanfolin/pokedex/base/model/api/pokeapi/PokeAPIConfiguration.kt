package com.zanfolin.pokedex.base.model.api.pokeapi

import com.zanfolin.pokedex.base.model.APIConfiguration
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PokeAPIConfiguration : APIConfiguration {

    override fun setup(baseUrl: HttpUrl) = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}