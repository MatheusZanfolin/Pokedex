package com.example.pokedex.model

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.pokedex.R
import com.example.pokedex.databinding.PokemonListItemBinding

class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {
    init {
        setHasStableIds(true)
    }

    private var pokemons: List<Pokemon> = listOf()

    private var lastPosition = -1

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PokemonListItemBinding.bind(view)

        val txtId: TextView = binding.txtId
        val txtName: TextView = binding.txtName
        val imgThumbnail: ImageView = binding.imgThumbnail

        fun clearAnimation() {
            binding.root.clearAnimation()
        }
    }

    override fun getItemId(position: Int): Long {
        return pokemons[position].id.toLong()
    }

    fun addPokemon(pokemon: Pokemon) {
        val list = pokemons.toMutableList()

        if (list.contains(pokemon))
            return

        list.add(pokemon)

        pokemons = list

        notifyItemInserted(pokemon.id - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): PokemonViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.pokemon_list_item, parent, false)

        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]

        holder.txtId.setText(pokemon.id.toString())
        holder.txtName.setText(pokemon.name.capitalize())
        holder.imgThumbnail.setImageDrawable(pokemon.thumbnail)

        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun onViewDetachedFromWindow(holder: PokemonViewHolder) {
        holder.clearAnimation()
    }

    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left)

            view.startAnimation(animation)

            lastPosition = position
        }
    }
}