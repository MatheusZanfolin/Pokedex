package com.example.pokedex

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonListAdapter
import com.example.pokedex.task.GetPokemonTask
import com.example.pokedex.viewmodel.PokemonListViewModel
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    val POKEMONS_BY_QUERY = 2
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

        enableContinuousLoading()
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

        getPokemonsUntil(INITIAL_POKEMON_COUNT)
    }

    private fun enableContinuousLoading() {
        if (isOnline()) {
            Thread {
                while (nextPokemonIdToGet <= LAST_POKEMON_ID) {
                    do { } while (GetPokemonTask.isLoading)

                    getPokemonsUntil(nextPokemonIdToGet + POKEMONS_BY_QUERY)
                }
            }.start()
        } else {
            showNoInternetDialog()
        }
    }

    private fun showNoInternetDialog() {
        if (isUiThread()) {
            AlertDialog.Builder(this)
                .setTitle("Sem internet!")
                .setMessage("Por favor, conecte-se à internet e reinicie sua Pokédex!")
                .setPositiveButton("REINICIAR", getRestartButtonListener())
                .setNegativeButton("SAIR", getExitButtonListener())
                .create()
                .show()
        }
    }

    private fun isUiThread(): Boolean {
        return Looper.getMainLooper().thread == Thread.currentThread()
    }

    private fun getDecoration(layoutManager: LinearLayoutManager): RecyclerView.ItemDecoration {
        return DividerItemDecoration(this, layoutManager.orientation)
    }

    private fun getLayoutManager(): LinearLayoutManager {
        return binding.lstPokemons.layoutManager as LinearLayoutManager
    }

    @SuppressLint("SetTextI18n")
    private fun getPokemonsUntil(lastId: Int) {
        if (isOnline()) {
            GetPokemonTask(nextPokemonIdToGet, lastId, model, WeakReference(binding.loadingScreen)).execute()

            nextPokemonIdToGet = lastId + 1
        } else {
            showNoInternetDialog()
        }
    }

    private fun getExitButtonListener(): DialogInterface.OnClickListener? {
        return DialogInterface.OnClickListener { dialog, which ->
            finish()
        }
    }

    private fun getRestartButtonListener(): DialogInterface.OnClickListener? {
        return DialogInterface.OnClickListener { dialog, which ->
            finish()
            startActivity(intent)
        }
    }

    private fun isOnline(): Boolean {
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val netInfo = connManager.activeNetworkInfo

        return netInfo?.isConnected ?: false
    }
}
