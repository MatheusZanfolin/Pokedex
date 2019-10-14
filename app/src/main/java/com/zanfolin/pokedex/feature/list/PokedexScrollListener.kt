package com.zanfolin.pokedex.feature.list

import androidx.recyclerview.widget.RecyclerView
import com.zanfolin.pokedex.feature.list.util.RecyclerViewScrollDirection
import com.zanfolin.pokedex.feature.list.viewmodel.PokemonListViewModel

class PokedexScrollListener(private val viewModel: PokemonListViewModel) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy == 0)
            return

        if (!recyclerView.canScrollVertically(RecyclerViewScrollDirection.DOWN))
            viewModel.getMorePokemons(howMany = 10)
    }

}