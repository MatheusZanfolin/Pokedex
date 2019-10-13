package com.zanfolin.pokedex.feature.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zanfolin.pokedex.base.model.pokemon.PokemonRepository
import com.zanfolin.pokedex.base.domain.Pokemon
import com.zanfolin.pokedex.base.util.PokemonNationalDexIdProvider
import com.zanfolin.pokedex.base.util.Region

private const val POKEMONS_PER_REQUEST = 20
private const val PAGE_OFFSET = POKEMONS_PER_REQUEST - 1

class PokemonListViewModel(region: Region, val repository: PokemonRepository) : ViewModel() {

    private var nextPokemonId = PokemonNationalDexIdProvider.getFirstByRegion(region)

    private val data: MutableLiveData<List<Pokemon>> by lazy { MutableLiveData() }

    fun getMorePokemons(onResult: (Pokemon) -> Unit, onError: (Int, Throwable) -> Unit) {
        val lastIdToSearch = nextPokemonId + PAGE_OFFSET
        for (currentId in nextPokemonId..lastIdToSearch) {
            getPokemon(currentId, onResult, onError)
        }
        nextPokemonId = lastIdToSearch + 1
    }

    private fun getPokemon(id: Int, onResult: (Pokemon) -> Unit, onError: (Int, Throwable) -> Unit) {
        repository
            .getPokemonById(id)
            .subscribe({ pkmn -> onResult(pkmn) } , {t -> onError(id, t)})
    }

}