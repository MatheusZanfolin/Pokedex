package com.zanfolin.pokedex.feature.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zanfolin.pokedex.base.domain.Pokemon
import com.zanfolin.pokedex.base.model.repository.pokemon.name.PokemonByIdRepository
import com.zanfolin.pokedex.base.util.PokemonNationalDexIdProvider
import com.zanfolin.pokedex.base.util.Region

class PokemonListViewModel(region: Region, val repository: PokemonByIdRepository) : ViewModel() {

    private var pageStart = PokemonNationalDexIdProvider.getFirstByRegion(region)

    private val data = MutableLiveData<List<Pokemon>>()

    private val isLoading = false

    val pokemons = data as LiveData<List<Pokemon>>

    /* TODO Search for a single request that can return a list of pokemons or use RX's .zip method
    COnsider also looking at concat map

    */
    fun getMorePokemons(howMany: Int) {
        val buffer = makeBuffer(howMany)
        val nextPageStart = pageStart + howMany

        for (currentId in pageStart until nextPageStart) {
            getPokemon(currentId)
                .subscribe({ pkmn -> buffer[getBufferIndex(pkmn.id)] }, {  })
        }

        pageStart = nextPageStart
        data.value = data.value?.toMutableList()?.apply { addAll(buffer) }
    }

    private fun makeBuffer(size: Int) = Array(size) { Pokemon() }

    private fun getBufferIndex(id: Int) = id - pageStart

    private fun getPokemon(id: Int) = repository
            .getPokemonById(id)

}