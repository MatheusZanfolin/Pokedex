package com.example.pokedex.model

import android.graphics.drawable.Drawable

class Pokemon {
    val id: Int = 0

    val name: String = ""

    val sprites = PokemonSpriteList("")

    var thumbnail: Drawable? = null
}

class PokemonSpriteList(val front_default: String)