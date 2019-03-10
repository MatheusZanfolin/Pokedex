package com.example.pokedex.task

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.constraint.ConstraintLayout
import android.view.View
import com.example.pokedex.PkmnListAcitivity
import com.example.pokedex.api.ApiUtil
import com.example.pokedex.api.PokemonService
import com.example.pokedex.model.Pokemon
import com.example.pokedex.viewmodel.PokemonListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL

class GetPokemonTask(private val method: GetPokemonMethod, private val firstPokemonId: Int, private val lastPokemonId: Int, private val model: PokemonListViewModel, val loadingScreen: WeakReference<ConstraintLayout>) : AsyncTask<Int, Void, List<Pokemon>>() {
    var isLoading: Boolean = false
        private set(value) {
            field = value
        }

    override fun doInBackground(vararg params: Int?): List<Pokemon> {
        if (isLoading)
            cancel(true)

        isLoading = true

        val desiredSize = (lastPokemonId - firstPokemonId) + 1

        val pokemonArray = Array<Pokemon?>(desiredSize) { null }
        when (method) {
            GetPokemonMethod.SYNCHRONOUS -> {
                for (id in firstPokemonId..lastPokemonId) {
                    val pkmnService = ApiUtil.retrofit.create(PokemonService::class.java)

                    val pkmnCall = pkmnService.getById(id)

                    val response = pkmnCall.execute()

                    val pkmn = response.body()

                    pkmn?.let {
                        setDrawables(pkmn)


                        val index = pkmn.id - firstPokemonId

                        pokemonArray[index] = pkmn
                    }
                }

                return pokemonArray.toList() as List<Pokemon>
            }

            GetPokemonMethod.ASYNCHRONOUS -> {
                for (id in firstPokemonId..lastPokemonId) {
                    val pkmnService = ApiUtil.retrofit.create(PokemonService::class.java)

                    val pkmnCall = pkmnService.getById(id)

                    pkmnCall.enqueue(getRequestCallback(pokemonArray))
                }

                do { } while (storedPokemons(pokemonArray) < desiredSize)

                return pokemonArray.toList() as List<Pokemon>
            }
        }
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

    private fun setDrawables(pkmn: Pokemon) {
        pkmn.thumbnail = getDrawableFromURL(pkmn.sprites.front_default)

        pkmn.frontShiny = getDrawableFromURL(pkmn.sprites.front_shiny)
        pkmn.frontFemale = getDrawableFromURL(pkmn.sprites.front_female)
    }

    private fun storedPokemons(pokemonArray: Array<Pokemon?>): Int {
        var stored = 0
        pokemonArray.forEach {
            it?.let {
                stored += 1
            }
        }

        return stored
    }

    private fun getRequestCallback(pokemonArray: Array<Pokemon?>): Callback<Pokemon> {
        return object : Callback<Pokemon> {
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                val pkmn = response.body()

                Thread {
                    pkmn?.let {
                        it.thumbnail = getDrawableFromURL(it.sprites.front_default)

                        val pokemonIndex = it.id - firstPokemonId

                        if (!pokemonArray.contains(it))
                            pokemonArray[pokemonIndex] = it
                    }
                }.start()
            }
        }
    }

    private fun getIndexFromId(id: Int): Int {
        return id - 1
    }

    private fun getDrawableFromURL(url: String): Drawable? {
        val input = URL(url).content as InputStream

        return Drawable.createFromStream(input, "src")
    }
}