package com.example.pokedex.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUtil {
    companion object {
        val BASE_URL = "https://pokeapi.co/api/v2/"

        val retrofit: Retrofit
            get() {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
    }
}