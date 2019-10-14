package com.zanfolin.pokedex.feature.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zanfolin.pokedex.base.domain.Pokemon
import com.zanfolin.pokedex.base.model.pokemon.PokemonRepository
import com.zanfolin.pokedex.base.util.PokemonNationalDexIdProvider
import com.zanfolin.pokedex.base.util.Region
import java.util.*

class PokemonListViewModel(region: Region, val repository: PokemonRepository) : ViewModel() {

    private var pageStart = PokemonNationalDexIdProvider.getFirstByRegion(region);

    private val data: MutableLiveData<List<Pokemon>> by lazy { MutableLiveData() }

    val pokemons = data as LiveData<List<Pokemon>>

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