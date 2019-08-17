package com.zanfolin.pokedex.base.util

import com.zanfolin.pokedex.base.util.Region.*

class PokemonNationalDexIdProvider private constructor() {

    companion object {
        @JvmStatic
        fun getFirstByRegion(region: Region): Int {
            return when (region) {
                KANTO -> 1
                else -> getLastByRegion(
                    Region.getPrecedessor(
                        region
                    )
                ) + 1
            }
        }

        @JvmStatic
        fun getLastByRegion(region: Region): Int {
            return when (region) {
                KANTO -> 151
                JOHTO -> 251
                HOENN -> 386
                SINNOH -> 494
                UNOVA -> 649
                KALOS -> 721
                ALOLA -> 809
                GALAR -> 0
            }
        }

    }

}