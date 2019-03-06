package com.example.pokedex.task

import android.os.AsyncTask
import android.widget.Toast
import com.example.pokedex.MainActivity
import com.example.pokedex.api.ApiUtil
import com.example.pokedex.api.PokemonService
import com.example.pokedex.model.Pokemon
import java.lang.ref.WeakReference

class SearchPokemonTask(val searchType: MainActivity.PokemonSearchType, val activity: WeakReference<MainActivity>): AsyncTask<String, Void, Pokemon>() {
    val NO_ID_ERROR_CODE = -1
    val NO_NAME_ERROR_CODE = "No name provided"

    val POKEMON_NOT_FOUND_ID = 0

    override fun doInBackground(vararg params: String?): Pokemon {
        val api = ApiUtil.retrofit.create(PokemonService::class.java)

        val call = when (searchType) {
            MainActivity.PokemonSearchType.BY_ID -> api.getById(getIdFromParam(params[0]))
            MainActivity.PokemonSearchType.BY_NAME -> api.getByName(getNameFromParam(params[0]))
        }

        val response = call.execute()

        return response.body() ?: Pokemon()
    }

    override fun onPostExecute(result: Pokemon?) {
        activity.get()?.pokemonSearchResult = result

        if (result?.id == POKEMON_NOT_FOUND_ID) {
            Toast.makeText(activity.get(), "Esse pokémon não existe!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity.get(), "Achou $result", Toast.LENGTH_SHORT).show()
        }
    }

    fun getIdFromParam(param: String?): Int {
        return param?.toInt() ?: NO_ID_ERROR_CODE
    }

    fun getNameFromParam(param: String?): String {
        return param?.toLowerCase() ?: NO_NAME_ERROR_CODE
    }
}