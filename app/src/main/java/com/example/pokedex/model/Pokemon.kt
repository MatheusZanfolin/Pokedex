package com.example.pokedex.model

import android.graphics.drawable.Drawable
import java.io.Serializable

class Pokemon : Serializable {
    val id: Int = 0

    val name: String = ""

    val sprites = PokemonSpriteList("", "", "")

    var thumbnail: Drawable? = null

    var frontShiny: Drawable? = null
    var frontFemale: Drawable? = null

    override fun toString(): String {
        return "#$id ${name.capitalize()}"
    }
}

class PokemonSpriteList(val front_default: String, val front_shiny: String, val front_female: String) : Serializable