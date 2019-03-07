package com.example.pokedex

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.databinding.DlgSearchPkmnBinding
import com.example.pokedex.databinding.FragmentPkmnListAcitivityBinding
import com.example.pokedex.databinding.FragmentPkmnListAcitivityBindingImpl
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonListAdapter
import com.example.pokedex.task.GetPokemonMethod
import com.example.pokedex.task.GetPokemonTask
import com.example.pokedex.task.SearchPokemonTask
import com.example.pokedex.viewmodel.PokemonListViewModel

import kotlinx.android.synthetic.main.activity_pkmn_list_acitivity.*
import java.lang.ref.WeakReference

class PkmnListAcitivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    var pokemonSearchResult: Pokemon? = null

    enum class PokemonSearchType {
        BY_ID,
        BY_NAME
    }

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
            extras.putInt(POKEMON_HEIGHT_KEY, pokemon?.heightInCentimeters ?: 0)
            extras.putDouble(POKEMON_WEIGHT_KEY, pokemon?.weightInKilograms ?: 0.0)
            extras.putSerializable(POKEMON_SPRITES_KEY, pokemon?.sprites)
            extras.putSerializable(POKEMON_TYPES_KEY, pokemon?.types?.toTypedArray())
            extras.putSerializable(POKEMON_ABILITIES_KEY, pokemon?.abilities?.toTypedArray())

            return extras
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pkmn_list_acitivity)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
    }

    override fun onResume() {
        super.onResume()

        stopAllThreads = false
    }

    override fun onPause() {
        super.onPause()

        stopAllThreads = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_pkmn_list_acitivity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
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

    private fun getSearchTypeListener(): DialogInterface.OnClickListener? {
        return DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> showSearchDialog("Nome do pokémon", PkmnListAcitivity.PokemonSearchType.BY_NAME)
                1 -> showSearchDialog("ID do pokémon", PkmnListAcitivity.PokemonSearchType.BY_ID)
            }
        }
    }

    private fun showSearchDialog(hint: String, searchType: PkmnListAcitivity.PokemonSearchType) {
        val searchView: View = LayoutInflater.from(this).inflate(R.layout.dlg_search_pkmn, null, false)

        val binding = DlgSearchPkmnBinding.bind(searchView)

        binding.edSearch.hint = hint
        binding.edSearch.inputType = if (searchType == PkmnListAcitivity.PokemonSearchType.BY_ID) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle("Buscar pokémon")
            .setView(searchView)
            .setPositiveButton("PESQUISAR", getSearchLisneter(binding, searchType))
            .create()
            .show()
    }

    private fun getSearchLisneter(binding: DlgSearchPkmnBinding, searchType: PkmnListAcitivity.PokemonSearchType): DialogInterface.OnClickListener? {
        return DialogInterface.OnClickListener { dialog, which ->
            SearchPokemonTask(searchType, WeakReference(this)).execute(binding.edSearch.text.toString())
        }
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            return PokedexFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            return 7
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Kanto"
                1 -> "Johto"
                2 -> "Hoenn"
                3 -> "Sinnoh"
                4 -> "Unova"
                5 -> "Kalos"
                6 -> "Alola"
                else -> "Galar"
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PokedexFragment: Fragment() {

        val INITIAL_POKEMON_COUNT = 9

        lateinit var binding: FragmentPkmnListAcitivityBinding

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PokedexFragment {
                val fragment = PokedexFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }

        val model: PokemonListViewModel by lazy {
            ViewModelProviders.of(this).get(PokemonListViewModel::class.java)
        }

        val listAdapter: PokemonListAdapter
            get() = binding.lstPokemons.adapter as PokemonListAdapter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_pkmn_list_acitivity, container, false)
            binding = FragmentPkmnListAcitivityBindingImpl.bind(rootView)

            setUpViewModel()

            setUpList()

            startContinuousLoading(arguments?.getInt(ARG_SECTION_NUMBER), rootView)

            return rootView
        }

        private fun startContinuousLoading(sectionNumber: Int?, rootView: View) {
            if (isOnline(rootView)) {
                GetPokemonTask(GetPokemonMethod.ASYNCHRONOUS, getFirstBySectionNumber(sectionNumber), getFirstBySectionNumber(sectionNumber) + INITIAL_POKEMON_COUNT - 1, model, WeakReference(binding.loadingScreen)).execute(getLastBySectionNumber(sectionNumber))
            } else {
                showNoInternetDialog(rootView)
            }
        }

        private fun getFirstBySectionNumber(sectionNumber: Int?): Int {
            return when (sectionNumber) {
                1 -> 1
                else -> getLastBySectionNumber(sectionNumber?.minus(1)) + 1
            }
        }

        private fun getLastBySectionNumber(sectionNumber: Int?): Int {
            return when (sectionNumber) {
                1 -> 151
                2 -> 251
                3 -> 386
                4 -> 494
                5 -> 649
                6 -> 721
                7 -> 809
                else -> 0
            }
        }

        private fun isOnline(rootView: View): Boolean {
            val connManager = rootView.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val netInfo = connManager.activeNetworkInfo

            return netInfo?.isConnected ?: false
        }

        private fun showNoInternetDialog(rootView: View) {
            if (isUiThread()) {
                AlertDialog.Builder(rootView.context)
                    .setTitle("Sem internet!")
                    .setMessage("Por favor, conecte-se à internet e reinicie sua Pokédex!")
                    .setPositiveButton("REINICIAR", getRestartButtonListener(rootView))
                    .setNegativeButton("SAIR", getExitButtonListener())
                    .create()
                    .show()
            }
        }

        private fun getExitButtonListener(): DialogInterface.OnClickListener? {
            return DialogInterface.OnClickListener { dialog, which ->
                activity?.finish()
            }
        }

        private fun getRestartButtonListener(rootView: View): DialogInterface.OnClickListener? {
            return DialogInterface.OnClickListener { dialog, which ->
                activity?.finish()

                startActivity(activity?.intent)
            }
        }

        private fun isUiThread(): Boolean {
            return Looper.getMainLooper().thread == Thread.currentThread()
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
            binding.lstPokemons.layoutManager = LinearLayoutManager(activity)
            binding.lstPokemons.adapter = PokemonListAdapter { pkmn -> startPkmnInfoActivity(activity!!, pkmn) }
            binding.lstPokemons.addItemDecoration(getDecoration(getLayoutManager()))
            binding.lstPokemons.itemAnimator = DefaultItemAnimator()
        }

        private fun getDecoration(layoutManager: LinearLayoutManager): RecyclerView.ItemDecoration {
            return DividerItemDecoration(activity, layoutManager.orientation)
        }

        private fun getLayoutManager(): LinearLayoutManager {
            return binding.lstPokemons.layoutManager as LinearLayoutManager
        }
    }
}
