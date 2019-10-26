package com.zanfolin.pokedex.repository

import com.zanfolin.pokedex.base.model.repository.pokemon.name.PokemonByIdRepository
import com.zanfolin.pokedex.base.model.api.pokeapi.PokeAPIConfiguration
import com.zanfolin.pokedex.repository.base.MockedAPI
import org.junit.Test

class PokemonByIdRepositoryTest {

    private val repository = PokemonByIdRepository(MockedAPI(PokeAPIConfiguration()))

    @Test
    fun test() {

    }

}