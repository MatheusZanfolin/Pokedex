package com.example.pokedex.model

import android.graphics.drawable.Drawable

class Pokemon {
    val id: Int = 0

    val name: String = ""

    val sprites = PokemonSpriteList("")

    var sprite_front_default: Drawable? = null
}

class PokemonSpriteList(val front_default: String)