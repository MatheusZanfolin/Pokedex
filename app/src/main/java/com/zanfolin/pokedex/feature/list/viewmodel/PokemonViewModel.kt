package com.zanfolin.pokedex.feature.list.viewmodel

import androidx.lifecycle.ViewModel
import com.zanfolin.pokedex.base.api.PokemonRepository
import com.zanfolin.pokedex.base.model.Pokemon
import com.zanfolin.pokedex.base.util.PokemonNationalDexIdProvider
import com.zanfolin.pokedex.base.util.Region

private const val POKEMONS_PER_REQUEST = 20
private const val PAGE_OFFSET = POKEMONS_PER_REQUEST - 1

class PokemonViewModel(private val region: Region, val repository: PokemonRepository) : ViewModel() {

    private var nextPokemonId = PokemonNationalDexIdProvider.getFirstByRegion(region)

    fun getMorePokemons(onResult: (Pokemon) -> Unit, onError: (Int, Throwable) -> Unit) {
        val lastIdToSearch = nextPokemonId + PAGE_OFFSET
        for (currentId in nextPokemonId..lastIdToSearch) {
            getPokemon(currentId, onResult, onError)
        }
        nextPokemonId = lastIdToSearch + 1
    }

    fun getPokemon(id: Int, onResult: (Pokemon) -> Unit, onError: (Int, Throwable) -> Unit) {
        repository
            .getPokemonById(id)
            .subscribe({ pkmn -> onResult(pkmn) } , {t -> onError(id, t)})
    }

    fun getPokemon(name: String, onResult: (Pokemon) -> Unit, onError: (Throwable) -> Unit) {
        repository
            .getPokemonByName(name)
            .subscribe({ pkmn -> onResult(pkmn) } , {t -> onError(t)})
    }

}