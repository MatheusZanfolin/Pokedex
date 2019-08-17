package com.example.pokedex.view.list

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
import com.example.pokedex.R
import com.example.pokedex.base.util.Region
import com.example.pokedex.base.util.ThreadIdentifier
import com.example.pokedex.databinding.FragmentPkmnListAcitivityBinding
import com.example.pokedex.databinding.FragmentPkmnListAcitivityBindingImpl
import com.example.pokedex.view.details.PkmnDetailsActivity
import com.example.pokedex.view.list.util.RecyclerViewScrollDirection.Companion.DOWN
import com.example.pokedex.viewmodel.PokemonViewModel

class RegionalDexFragment: Fragment() {

    lateinit var binding: FragmentPkmnListAcitivityBinding

    lateinit var viewModel: PokemonViewModel

    private val listAdapter: PokemonListAdapter
        get() = binding.lstPokemons.adapter as PokemonListAdapter

    private val listLayoutManager: LinearLayoutManager
        get() = binding.lstPokemons.layoutManager as LinearLayoutManager

    private val listDecoration: DividerItemDecoration
        get() = DividerItemDecoration(activity, listLayoutManager.orientation)

    companion object {
        private const val CURRENT_REGION = "CURRENT_REGION"

        fun newInstance(region: Region): RegionalDexFragment {
            val fragment = RegionalDexFragment()
            val args = Bundle()

            args.putSerializable(CURRENT_REGION, region)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_pkmn_list_acitivity, container, false)
        binding = FragmentPkmnListAcitivityBindingImpl.bind(rootView)
        viewModel = PokemonViewModel(arguments?.getSerializable(CURRENT_REGION) as Region)

        setUpList()

        loadMorePokemons()

        return rootView
    }

    private fun setUpList() {
        binding.lstPokemons.layoutManager = LinearLayoutManager(activity)
        binding.lstPokemons.adapter = PokemonListAdapter(context, R.drawable.blur_missigno) { pkmn -> PkmnDetailsActivity.start(context, pkmn) }
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
        viewModel.getMorePokemons({ pkmn -> listAdapter.addPokemon(pkmn) }, { id, _ -> listAdapter.addMissigno(id - 1) })

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