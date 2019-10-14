package com.zanfolin.pokedex.feature.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zanfolin.pokedex.R
import com.zanfolin.pokedex.base.util.Region
import com.zanfolin.pokedex.databinding.FragmentRegionalDexBinding
import com.zanfolin.pokedex.feature.details.PkmnDetailsActivity
import com.zanfolin.pokedex.feature.list.util.RecyclerViewScrollDirection.Companion.DOWN
import com.zanfolin.pokedex.feature.list.viewmodel.PokemonListViewModel
import kotlinx.android.synthetic.main.fragment_regional_dex.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@SuppressLint("ValidFragment")
class RegionalDexFragment private constructor(private val region: Region) : Fragment() {

    val viewModel: PokemonListViewModel by inject { parametersOf(region) }

    private val listAdapter = PokemonListAdapter(R.drawable.blur_missigno) { pkmn -> PkmnDetailsActivity.start(context, pkmn) }

    private val listLayoutManager = LinearLayoutManager(activity)

    companion object {
        fun newInstance(region: Region) = RegionalDexFragment(region)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupList()

        viewModel.pokemons.observe(this, Observer(listAdapter::update))
        viewModel.getMorePokemons(howMany = 10)

        return inflater.inflate(R.layout.fragment_regional_dex, container, false)
    }

    private fun setupList() = lstPokemons.apply {
        layoutManager = listLayoutManager
        adapter = listAdapter
        addItemDecoration(DividerItemDecoration(activity, listLayoutManager.orientation))
        itemAnimator = DefaultItemAnimator()

        addOnScrollListener(PokedexScrollListener(viewModel))
    }

}