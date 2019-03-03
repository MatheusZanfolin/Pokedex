package com.example.pokedex.task

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.widget.ImageView
import android.widget.TextView
import com.example.pokedex.api.ApiUtil
import com.example.pokedex.api.PokemonService
import com.example.pokedex.model.Pokemon
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL

class GetPokemonTask(val txtNameReference: WeakReference<TextView>, val imgThumbReference: WeakReference<ImageView>) : AsyncTask<Void, Void, Pokemon>() {
    override fun doInBackground(vararg params: Void?): Pokemon {
        val pkmnService = ApiUtil.retrofit.create(PokemonService::class.java)

        val pkmnCall = pkmnService.getById(1)

        val pkmn = pkmnCall.execute().body()

        pkmn?.sprite_front_default = getDrawableFromURL(pkmn?.sprites?.front_default ?: "")

        return pkmn ?: Pokemon()
    }

    override fun onPostExecute(pokemon: Pokemon?) {
        txtNameReference.get()?.setText(pokemon?.name)

        imgThumbReference.get()?.setImageDrawable(pokemon?.sprite_front_default)
    }

    private fun getDrawableFromURL(url: String): Drawable? {
        val input = URL(url).content as InputStream

        return Drawable.createFromStream(input, "src")
    }
}