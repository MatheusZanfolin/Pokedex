package com.example.pokedex.task

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_pkmn_info.*
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL

class GetPokemonSpritesTask(val imgFrontDefault: WeakReference<ImageView>, val imgFrontFemale: WeakReference<ImageView>, val imgFrontShiny: WeakReference<ImageView>): AsyncTask<String, Void, Map<Int, Drawable?>>() {
    override fun doInBackground(vararg params: String?): MutableMap<Int, Drawable?> {
        val drawables = mutableMapOf<Int, Drawable?>()

        drawables.put(imgFrontDefault.get()?.id ?: 0, getImageFromURL(params[0]))
        drawables.put(imgFrontFemale.get()?.id ?: 0, getImageFromURL(params[1]))
        drawables.put(imgFrontShiny.get()?.id ?: 0, getImageFromURL(params[2]))

        return drawables
    }

    override fun onPostExecute(result: Map<Int, Drawable?>?) {
        val frontDefault = result?.get(imgFrontDefault.get()?.id)
        val frontFemale = result?.get(imgFrontFemale.get()?.id)
        val frontShiny = result?.get(imgFrontShiny.get()?.id)

        setDrawable(frontDefault, imgFrontDefault)
        setDrawable(frontFemale, imgFrontFemale)
        setDrawable(frontShiny, imgFrontShiny)
    }

    private fun setDrawable(drawable: Drawable?, imageView: WeakReference<ImageView>) {
        if (drawable != null) {
            imageView.get()?.setImageDrawable(drawable)
        } else {
            imageView.get()?.setImageResource(android.R.drawable.ic_delete)

            imageView.get()?.setPadding(16, 16, 16, 16)
        }
    }

    fun getImageFromURL(url: String?): Drawable? {
        if (url == null)
            return null

        return Drawable.createFromStream(URL(url).content as InputStream, "src")
    }
}