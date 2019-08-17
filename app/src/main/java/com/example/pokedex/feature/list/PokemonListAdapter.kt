package com.example.pokedex.view.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.databinding.PokemonListItemBinding
import com.example.pokedex.model.Pokemon
import java.lang.ref.WeakReference

class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = PokemonListItemBinding.bind(view)

    val txtId: TextView = binding.txtId
    val txtName: TextView = binding.txtName
    val imgThumbnail: ImageView = binding.imgThumbnail

    fun clearAnimation() = binding.root.clearAnimation()

    fun addOnClickListener(pokemon: Pokemon, listener: (Pokemon) -> Unit) = itemView.setOnClickListener {
        listener.invoke(pokemon)
    }
}

class PokemonListAdapter(ctx: Context?, @DrawableRes private val placeholderDrawable: Int, private val itemClickListener: (Pokemon) -> Unit) : RecyclerView.Adapter<PokemonViewHolder>() {

    private val context = WeakReference(ctx)

    init {
        setHasStableIds(true)
    }

    private val pokemons: MutableList<Pokemon> = mutableListOf()

    private var lastAnimatedPosition = -1

    override fun getItemId(position: Int): Long = pokemons[position].id.toLong()

    override fun getItemCount(): Int = pokemons.size

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): PokemonViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.pokemon_list_item, parent, false)

        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemons[position]

        if (pokemon.id != 0) {
            holder.txtId.text = pokemon.id.toString()
            holder.txtName.text = pokemon.name.capitalize()
            context.get()?.let { fetchImage(it, holder.imgThumbnail, pokemon.sprites.front_default) }

            holder.addOnClickListener(pokemon, itemClickListener)
        } else {
            holder.txtId.text = "3̴̧̨͙̝̐̀́̕ͅ6̸̮̹̳̪͛̈́͒?̶͓̗̲̲͂"
            holder.txtName.text = "M̶̻͓͝ì̶̫ŝ̸̩̦̈́ş̸̬̕ï̸̥͘g̷̳̞͆N̴̼͐͗ö̶̲͈́"
            holder.imgThumbnail.setImageResource(placeholderDrawable);
        }

        setAnimation(holder.itemView, position)
    }

    private fun fetchImage(context: Context, imageView: ImageView, spriteUrl: String) = Glide
        .with(context)
        .load(spriteUrl)
        .placeholder(placeholderDrawable)
        .into(imageView)

    override fun onViewDetachedFromWindow(holder: PokemonViewHolder) = holder.clearAnimation()

    fun addPokemon(pkmn: Pokemon) {
        if (pokemons.contains(pkmn))
            return

        pokemons.add(pkmn.id - 1, pkmn)

        notifyItemInserted(pkmn.id - 1)
    }

    fun addMissigno(index: Int) {
        pokemons.add(index, Pokemon())

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