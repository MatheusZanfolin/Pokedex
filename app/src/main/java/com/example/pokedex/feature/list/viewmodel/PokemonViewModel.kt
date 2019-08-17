package com.example.pokedex.viewmodel

import com.example.pokedex.util.PokemonNationalDexIdProvider
import com.example.pokedex.util.Region
import com.example.pokedex.base.api.PokemonRepository
import com.example.pokedex.base.core.viewmodel.BaseViewModel
import com.example.pokedex.base.model.Pokemon

class PokemonViewModel(private val region: Region): BaseViewModel<PokemonRepository>() {

    private val POKEMONS_PER_REQUEST = 20

    private val PAGE_OFFSET = POKEMONS_PER_REQUEST - 1

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