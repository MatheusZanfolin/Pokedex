package com.zanfolin.pokedex.feature.list

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.zanfolin.pokedex.R
import com.zanfolin.pokedex.databinding.DlgSearchPkmnBinding
import com.zanfolin.pokedex.feature.list.util.PokemonSearchCriteria
import com.zanfolin.pokedex.feature.list.util.PokemonSearchCriteria.*
import kotlinx.android.synthetic.main.activity_pkmn_dex.*

class PkmnListActivity : AppCompatActivity() {

    private var pokedexAdapter: PokedexPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pkmn_dex)

        pokedexAdapter = PokedexPagerAdapter(supportFragmentManager)

        container.adapter = pokedexAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_pkmn_list_acitivity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> {
                showSearchTypeSelectionDialog()

                true
            }
            else -> false
        }
    }

    private fun showSearchTypeSelectionDialog() = AlertDialog.Builder(this)
        .setTitle("Buscar pokémon")
        .setItems(R.array.searchTypes, ::onSearchTypeSelected)
        .create()
        .show()

    private fun onSearchTypeSelected(dialog: DialogInterface, which: Int) {
        when (which) {
            0 -> showSearchDialog("Nome do pokémon", BY_NAME)
            1 -> showSearchDialog("ID do pokémon", BY_ID)
        }
    }

    private fun showSearchDialog(hint: String, searchType: PokemonSearchCriteria) {
        val searchView: View = LayoutInflater.from(this).inflate(R.layout.dlg_search_pkmn, null, false)

        val binding = DlgSearchPkmnBinding.bind(searchView)

        binding.edSearch.hint = hint
        binding.edSearch.inputType = when (searchType) {
            BY_ID -> InputType.TYPE_CLASS_NUMBER
            BY_NAME -> InputType.TYPE_CLASS_TEXT
        }

        AlertDialog.Builder(this)
            .setTitle("Buscar pokémon")
            .setView(searchView)
            .setPositiveButton("PESQUISAR") { _, _ -> searchPokemon(searchType)}
            .create()
            .show()
    }

    private fun searchPokemon(searchType: PokemonSearchCriteria) { // TODO

    }

}
