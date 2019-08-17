package com.zanfolin.pokedex.base.util

import java.io.Serializable

enum class Region(val title: String) : Serializable {

    KANTO("Kanto"),
    JOHTO("Johto"),
    HOENN("Hoenn"),
    SINNOH("Sinnoh"),
    UNOVA("Unova"),
    KALOS("Kalos"),
    ALOLA("Alola"),
    GALAR("Galar");

    companion object {
        @JvmStatic
        fun getPrecedessor(region: Region): Region {
            return values()[region.ordinal.minus(1)]
        }
    }

}