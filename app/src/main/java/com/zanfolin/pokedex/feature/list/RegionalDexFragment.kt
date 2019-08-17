package com.zanfolin.pokedex.feature.list

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanfolin.pokedex.R
import com.zanfolin.pokedex.base.model.Pokemon
import com.zanfolin.pokedex.base.util.Region
import com.zanfolin.pokedex.base.util.ThreadIdentifier
import com.zanfolin.pokedex.databinding.FragmentRegionalDexBinding
import com.zanfolin.pokedex.feature.details.PkmnDetailsActivity
import com.zanfolin.pokedex.feature.list.util.RecyclerViewScrollDirection.Companion.DOWN
import com.zanfolin.pokedex.feature.list.viewmodel.PokemonViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@SuppressLint("ValidFragment")
class RegionalDexFragment private constructor(val region: Region) : Fragment() {

    lateinit var binding: FragmentRegionalDexBinding

    val viewModel: PokemonViewModel by inject { parametersOf(region) }

    private val listAdapter: PokemonListAdapter
        get() = binding.lstPokemons.adapter as PokemonListAdapter

    private val listLayoutManager: LinearLayoutManager
        get() = binding.lstPokemons.layoutManager as LinearLayoutManager

    private val listDecoration: DividerItemDecoration
        get() = DividerItemDecoration(activity, listLayoutManager.orientation)

    companion object {
        fun newInstance(region: Region) = RegionalDexFragment(region)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_regional_dex, container, false)
        binding = FragmentRegionalDexBinding.bind(rootView)

        setUpList()

        loadMorePokemons()

        return rootView
    }

    private fun setUpList() {
        binding.lstPokemons.layoutManager = LinearLayoutManager(activity)
        binding.lstPokemons.adapter = PokemonListAdapter(R.drawable.blur_missigno) { pkmn -> PkmnDetailsActivity.start(context, pkmn) }
        binding.lstPokemons.addItemDecoration(listDecoration)
        binding.lstPokemons.itemAnimator = DefaultItemAnimator()

        binding.lstPokemons.addOnScrollListener(listScrollListener)
    }

    private val listScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy == 0)
                    return

                if (!recyclerView.canScrollVertically(DOWN))
                    loadMorePokemons()
            }
        }

    private fun loadMorePokemons() =
        viewModel.getMorePokemons(::onPokemonAcquired) { id, _ -> listAdapter.addPlaceholder(id - 1) }

    private fun onPokemonAcquired(pkmn: Pokemon) {
        listAdapter.addPokemon(pkmn)

        binding.imgLoading.visibility = View.GONE
        binding.txtLoading.visibility = View.GONE
    }

    private fun showNoInternetDialog() {
        if (ThreadIdentifier.isUiThread()) {
            val context = activity

            if (context?.isFinishing == true)
                return

            context?.let {
                AlertDialog.Builder(context)
                    .setTitle("Sem internet!")
                    .setMessage("Por favor, conecte-se à internet e reinicie sua Pokédex!")
                    .setPositiveButton("REINICIAR", ::onRestartButtonPressed)
                    .setNegativeButton("SAIR") { _, _ -> activity?.finish() }
                    .setCancelable(false)
                    .create()
                    .show()
            }
        }
    }

    private fun onRestartButtonPressed(dialog: DialogInterface, which: Int) {
        activity?.finish()

        startActivity(activity?.intent)
    }

}