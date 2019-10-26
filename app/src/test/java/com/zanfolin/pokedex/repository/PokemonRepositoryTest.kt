package com.zanfolin.pokedex.repository

import com.zanfolin.pokedex.base.model.pokemon.PokemonRepository
import com.zanfolin.pokedex.base.service.pokemon.DefaultConfiguration
import org.junit.Test

class PokemonRepositoryTest {

    private val repository = PokemonRepository(MockedAPI(DefaultConfiguration()))

    @Test
    fun test() {

    }

}