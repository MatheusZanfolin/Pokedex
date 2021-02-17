package com.zanfolin.pokedex

import android.app.Application
import com.zanfolin.pokedex.pokemonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokedexApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PokedexApplication)

            modules(listOf(pokemonModule))
        }
    }

}