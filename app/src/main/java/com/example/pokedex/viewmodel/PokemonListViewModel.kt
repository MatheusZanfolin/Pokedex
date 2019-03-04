package com.example.pokedex.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.pokedex.model.Pokemon

class PokemonListViewModel : ViewModel() {
    val pokemonsListData: Array<MutableLiveData<Pokemon>> = Array(807) { MutableLiveData<Pokemon>() }

    fun onPokemonAdded(pokemon: Pokemon, position: Int) {
        pokemonsListData[position].value = pokemon
    }
}