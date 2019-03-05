package com.example.pokedex.model

import android.graphics.drawable.Drawable

class Pokemon {
    val id: Int = 0

    val name: String = ""

    val sprites = PokemonSpriteList("")

    var thumbnail: Drawable? = null

    override fun toString(): String {
        return "#$id ${name.capitalize()}"
    }
}

class PokemonSpriteList(val front_default: String)