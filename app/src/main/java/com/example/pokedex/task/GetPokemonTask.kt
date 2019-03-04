package com.example.pokedex.task

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import com.example.pokedex.api.ApiUtil
import com.example.pokedex.api.PokemonService
import com.example.pokedex.model.Pokemon
import com.example.pokedex.viewmodel.PokemonListViewModel
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL

class GetPokemonTask(val firstPokemonId: Int, val lastPokemonId: Int, val model: PokemonListViewModel, val loadingScreen: WeakReference<ConstraintLayout>) : AsyncTask<Void, Void, List<Pokemon>>() {
    companion object {
        var isLoading = false
    }

    override fun doInBackground(vararg params: Void?): List<Pokemon> {
        isLoading = true

        val pokemonList = mutableListOf<Pokemon>()
        for (id in firstPokemonId..lastPokemonId) {
            val pkmnService = ApiUtil.retrofit.create(PokemonService::class.java)

            val pkmnCall = pkmnService.getById(id)

            val response = pkmnCall.execute()

            val pkmn: Pokemon? = response.body()

            pkmn?.let {
                pkmn.thumbnail = getDrawableFromURL(pkmn.sprites.front_default)

                pokemonList.add(it)
            }
        }

        return pokemonList
    }

    private fun getIndexFromId(id: Int): Int {
        return id - 1
    }

    override fun onPostExecute(pokemons: List<Pokemon>?) {
        pokemons?.let {
            for (pokemon in pokemons) {
                model.onPokemonAdded(pokemon, getIndexFromId(pokemon.id))
            }

            loadingScreen.get()?.visibility = View.INVISIBLE
        }

        isLoading = false
    }

    private fun getDrawableFromURL(url: String): Drawable? {
        val input = URL(url).content as InputStream

        return Drawable.createFromStream(input, "src")
    }
}