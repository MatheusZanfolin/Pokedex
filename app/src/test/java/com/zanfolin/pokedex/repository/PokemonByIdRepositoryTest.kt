package com.zanfolin.pokedex.repository

import com.zanfolin.pokedex.base.model.api.pokeapi.PokeAPIConfiguration
import com.zanfolin.pokedex.base.model.repository.pokemon.name.PokemonByIdRepository
import com.zanfolin.pokedex.base.util.FileUtils
import com.zanfolin.pokedex.base.util.FileUtils.readJsonForEndpoint
import com.zanfolin.pokedex.repository.base.BaseRepositoryTest
import com.zanfolin.pokedex.repository.base.MockedAPI
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection.HTTP_OK
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.plugins.RxAndroidPlugins.setInitMainThreadSchedulerHandler


private const val TEST_ID = 1

class PokemonByIdRepositoryTest : BaseRepositoryTest() {

    private val POKEMON_BY_ID_ENDPOINT = "pokemon/$TEST_ID"

    private val repository = PokemonByIdRepository(MockedAPI(PokeAPIConfiguration(), mockServer))

    @Test
    fun `when requesting pokemon by ID should return Pokemon`() {
        mock(POKEMON_BY_ID_ENDPOINT, HTTP_OK)

        val pkmn = repository.getPokemonById(TEST_ID).blockingGet()

        with (pkmn) {
            assertEquals(1, id)
            assertEquals("bulbasaur", name)
            assertEquals(7, height)
            assertEquals(69, weight)
        }
    }

}