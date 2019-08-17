package com.example.pokedex.api

import com.example.pokedex.base.repository.BaseRepository
import com.example.pokedex.model.Pokemon
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PokemonRepository : BaseRepository<PokemonEndpoint>() {

    fun getPokemonById(id: Int): Observable<Pokemon> {
        return endpoint
            .getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}