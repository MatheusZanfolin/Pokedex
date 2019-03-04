package com.example.pokedex

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonListAdapter
import com.example.pokedex.task.GetPokemonTask
import com.example.pokedex.viewmodel.PokemonListViewModel
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    val POKEMONS_BY_QUERY = 3
    val INITIAL_POKEMON_COUNT = 10

    val LAST_POKEMON_ID = 807

    var nextPokemonIdToGet = 1

    lateinit var binding: ActivityMainBinding

    val model: PokemonListViewModel by lazy {
        ViewModelProviders.of(this).get(PokemonListViewModel::class.java)
    }

    val listAdapter: PokemonListAdapter
        get() = binding.lstPokemons.adapter as PokemonListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpViewModel()

        setUpList()
    }

    private fun setUpViewModel() {
        val pokemonObserver = Observer<Pokemon> { newPokemon ->
            newPokemon?.let {
                listAdapter.addPokemon(newPokemon)
            }
        }

        for (data in model.pokemonsListData)
            data.observe(this, pokemonObserver)
    }

    private fun setUpList() {
        binding.lstPokemons.adapter = PokemonListAdapter()
        binding.lstPokemons.layoutManager = LinearLayoutManager(this)
        binding.lstPokemons.addItemDecoration(getDecoration(getLayoutManager()))
        binding.lstPokemons.itemAnimator = DefaultItemAnimator()
        binding.lstPokemons.addOnScrollListener(getScrollListener(getLayoutManager()))

        getPokemonsUntil(INITIAL_POKEMON_COUNT)
    }

    private fun getScrollListener(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastIdToGet = nextPokemonIdToGet + POKEMONS_BY_QUERY
                if (lastIdToGet <= LAST_POKEMON_ID)
                    getPokemonsUntil(lastIdToGet)
            }
        }
    }

    private fun getDecoration(layoutManager: LinearLayoutManager): RecyclerView.ItemDecoration {
        return DividerItemDecoration(this, layoutManager.orientation)
    }

    private fun getLayoutManager(): LinearLayoutManager {
        return binding.lstPokemons.layoutManager as LinearLayoutManager
    }

    private fun getPokemonsUntil(lastId: Int) {
        GetPokemonTask(nextPokemonIdToGet, lastId, model, WeakReference(binding.loadingScreen)).execute()

        nextPokemonIdToGet = lastId + 1
    }
}
