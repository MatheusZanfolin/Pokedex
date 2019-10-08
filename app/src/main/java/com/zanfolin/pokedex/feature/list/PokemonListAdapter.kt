package com.zanfolin.pokedex.feature.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zanfolin.pokedex.R
import com.zanfolin.pokedex.databinding.PokemonListItemBinding
import com.zanfolin.pokedex.base.domain.Pokemon

class PokemonViewHolder(view: View, @DrawableRes private val placeholderDrawable: Int) : RecyclerView.ViewHolder(view) {

    val binding = PokemonListItemBinding.bind(view)

    val txtId: TextView = binding.txtId
    val txtName: TextView = binding.txtName
    val imgThumbnail: ImageView = binding.imgThumbnail

    fun clearAnimation() = binding.root.clearAnimation()

    fun bind(pokemon: Pokemon, listener: (Pokemon) -> Unit) {
        txtId.text = pokemon.id.toString()
        txtName.text = pokemon.name.capitalize()

        Glide
            .with(itemView.context)
            .load(pokemon.sprites.front_default)
            .placeholder(placeholderDrawable)
            .into(imgThumbnail)

        addOnClickListener(pokemon, listener)
    }

    fun bindErrorView() {
        txtId.text = "3̴̧̨͙̝̐̀́̕ͅ6̸̮̹̳̪͛̈́͒?̶͓̗̲̲͂"
        txtName.text = "M̶̻͓͝ì̶̫ŝ̸̩̦̈́ş̸̬̕ï̸̥͘g̷̳̞͆N̴̼͐͗ö̶̲͈́"
        imgThumbnail.setImageResource(placeholderDrawable);
    }

    private fun addOnClickListener(pokemon: Pokemon, listener: (Pokemon) -> Unit) = itemView.setOnClickListener {
        listener.invoke(pokemon)
    }

}

class PokemonListAdapter(@DrawableRes private val placeholderDrawable: Int, private val itemClickListener: (Pokemon) -> Unit) : RecyclerView.Adapter<PokemonViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private var pokemons = Array(156) { Pokemon() }

    private var lastAnimatedPosition = -1

    override fun getItemId(position: Int): Long = pokemons[position].id.toLong()

    override fun getItemCount(): Int = pokemons.size

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): PokemonViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.pokemon_list_item, parent, false)

        return PokemonViewHolder(view, placeholderDrawable)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]

        if (pokemon.id != 0)
            bindItem(holder, pokemon)
        else
            bindErrorItem(holder)

        setAnimation(holder.itemView, position)
    }

    private fun bindItem(holder: PokemonViewHolder, pokemon: Pokemon) = holder.bind(pokemon, itemClickListener)

    private fun bindErrorItem(holder: PokemonViewHolder) = holder.bindErrorView()

    override fun onViewDetachedFromWindow(holder: PokemonViewHolder) = holder.clearAnimation()

    fun addPokemon(pkmn: Pokemon) {
        if (pokemons.contains(pkmn))
            return

        val index = pkmn.id - 1
        if (index > pokemons.size) {
            val extended = Array(index + 1) { i -> if (i < pokemons.size) pokemons[i] else Pokemon() }

            pokemons = extended
        }

        pokemons[index] = pkmn

        notifyItemInserted(index)
    }

    fun addPlaceholder(index: Int) {
        pokemons[index] = Pokemon()

        notifyItemInserted(index)
    }

    private fun setAnimation(view: View, position: Int) {
        if (position > lastAnimatedPosition) {
            val animation = AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left)

            view.startAnimation(animation)

            lastAnimatedPosition = position
        }
    }
}