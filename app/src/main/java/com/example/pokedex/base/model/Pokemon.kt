package com.example.pokedex.model

import android.graphics.drawable.Drawable
import java.io.Serializable

class Pokemon : Serializable {
    val id: Int = 0

    val name: String = ""

    val height: Int = 0
    val weight: Int = 0

    val heightInCentimeters: Int
        get() = height.times(10)

    val weightInKilograms: Double
        get() = weight.times(0.1)

    val types: List<ApiPokemonType> = listOf()
    val abilities: List<ApiPokemonAbility> = listOf()

    val sprites = PokemonSpriteList("", "", "")

    var thumbnail: Drawable? = null
    var frontShiny: Drawable? = null
    var frontFemale: Drawable? = null

    override fun toString(): String {
        return "#$id ${name.capitalize()}"
    }
}

class PokemonSpriteList(val front_default: String, val front_shiny: String, val front_female: String) : Serializable

class ApiPokemonType(val type: PokemonType): Serializable

class PokemonType(val name: String): Serializable

class ApiPokemonAbility(val ability: PokemonAbility): Serializable

class PokemonAbility(val name: String): Serializable