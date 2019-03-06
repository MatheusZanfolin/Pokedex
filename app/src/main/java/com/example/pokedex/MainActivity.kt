package com.example.pokedex

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.databinding.DlgSearchPkmnBinding
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonListAdapter
import com.example.pokedex.task.GetPokemonMethod
import com.example.pokedex.task.GetPokemonTask
import com.example.pokedex.task.SearchPokemonTask
import com.example.pokedex.viewmodel.PokemonListViewModel
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    // TODO Separate list into tabs, each containing a generation

    val INITIAL_POKEMON_COUNT = 9

    lateinit var binding: ActivityMainBinding

    var pokemonSearchResult: Pokemon? = null

    companion object {
        val POKEMON_ID_KEY = "id"
        val POKEMON_NAME_KEY = "name"
        val POKEMON_HEIGHT_KEY = "height"
        val POKEMON_WEIGHT_KEY = "weight"
        var POKEMON_TYPES_KEY = "type"
        val POKEMON_SPRITES_KEY = "sprites"
        val POKEMON_ABILITIES_KEY = "abilities"

        var stopAllThreads: Boolean = false

        fun startPkmnInfoActivity(context: Context, pokemon: Pokemon?) {
            val pokemonInfoIntent = Intent(context, PkmnInfoActivity::class.java)

            pokemonInfoIntent.putExtras(getPkmnInfoExtras(pokemon))

            context.startActivity(pokemonInfoIntent)
        }

        private fun getPkmnInfoExtras(pokemon: Pokemon?): Bundle {
            val extras = Bundle()

            extras.putInt(POKEMON_ID_KEY, pokemon?.id ?: 0)
            extras.putString(POKEMON_NAME_KEY, pokemon?.name)
            extras.putInt(POKEMON_HEIGHT_KEY, pokemon?.height ?: 0)
            extras.putInt(POKEMON_WEIGHT_KEY, pokemon?.weight ?: 0)
            extras.putSerializable(POKEMON_SPRITES_KEY, pokemon?.sprites)
            extras.putSerializable(POKEMON_TYPES_KEY, pokemon?.types?.toTypedArray())
            extras.putSerializable(POKEMON_ABILITIES_KEY, pokemon?.abilities?.toTypedArray())

            return extras
        }
    }

    enum class PokemonSearchType {
        BY_ID,
        BY_NAME
    }

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

        startContinuousLoading()
    }

    override fun onResume() {
        super.onResume()

        stopAllThreads = false
    }

    override fun onPause() {
        super.onPause()

        stopAllThreads = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.btnSearch -> {
                AlertDialog.Builder(this)
                    .setTitle("Buscar pokémon")
                    .setItems(R.array.searchTypes, getSearchTypeListener())
                    .create()
                    .show()

                return true
            }
        }

        return false
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun getSearchTypeListener(): DialogInterface.OnClickListener? {
        return DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> showSearchDialog("Nome do pokémon", PokemonSearchType.BY_NAME)
                1 -> showSearchDialog("ID do pokémon", PokemonSearchType.BY_ID)
            }
        }
    }

    private fun showSearchDialog(hint: String, searchType: PokemonSearchType) {
        val searchView: View = LayoutInflater.from(this).inflate(R.layout.dlg_search_pkmn, null, false)

        val binding = DlgSearchPkmnBinding.bind(searchView)

        binding.edSearch.hint = hint
        binding.edSearch.inputType = if (searchType == PokemonSearchType.BY_ID) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle("Buscar pokémon")
            .setView(searchView)
            .setPositiveButton("PESQUISAR", getSearchLisneter(binding, searchType))
            .create()
            .show()
    }

    private fun getSearchLisneter(binding: DlgSearchPkmnBinding, searchType: PokemonSearchType): DialogInterface.OnClickListener? {
        return DialogInterface.OnClickListener { dialog, which ->
            SearchPokemonTask(searchType, WeakReference(this)).execute(binding.edSearch.text.toString())
        }
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
        binding.lstPokemons.adapter = PokemonListAdapter { pkmn -> startPkmnInfoActivity(this, pkmn) }
        binding.lstPokemons.layoutManager = LinearLayoutManager(this)
        binding.lstPokemons.addItemDecoration(getDecoration(getLayoutManager()))
        binding.lstPokemons.itemAnimator = DefaultItemAnimator()
    }

    private fun startContinuousLoading() {
        if (isOnline()) {
            GetPokemonTask(GetPokemonMethod.ASYNCHRONOUS, 1, INITIAL_POKEMON_COUNT, model, WeakReference(binding.loadingScreen)).execute()
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
