package com.zanfolin.pokedex.repository

import com.zanfolin.pokedex.base.model.api.pokeapi.PokeAPIConfiguration
import com.zanfolin.pokedex.base.model.repository.pokemon.name.PokemonByIdRepository
import com.zanfolin.pokedex.repository.base.MockedAPI
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection.HTTP_OK

class PokemonByIdRepositoryTest {

    private val mockServer = MockWebServer()

    private val repository = PokemonByIdRepository(MockedAPI(PokeAPIConfiguration(), mockServer))

    @Before
    fun setup() = mockServer.start()

    @After
    fun teardown() = mockServer.shutdown()

    @Test
    fun `when requesting pokemon by ID should return Pokemon`() {
        mockServer.enqueue(MockResponse().apply {
            setResponseCode(HTTP_OK)
            setBody("") // TODO Get body from file
        })

        val pkmn = repository.getPokemonById(1).blockingGet() // TODO Add mock for Bulbasaur

        // TODO Perform assertions
    }

}