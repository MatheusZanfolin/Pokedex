package com.zanfolin.pokedex.feature.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import com.zanfolin.pokedex.R
import com.zanfolin.pokedex.feature.details.utils.format
import com.zanfolin.pokedex.databinding.ActivityPkmnInfoBinding
import com.zanfolin.pokedex.base.domain.ApiPokemonAbility
import com.zanfolin.pokedex.base.domain.ApiPokemonType
import com.zanfolin.pokedex.base.domain.Pokemon
import com.zanfolin.pokedex.base.domain.PokemonSpriteList

class PkmnDetailsActivity : AppCompatActivity() {

    companion object {
        val DISPLAYED_POKEMON = "DISPLAYED_POKEMON"

        fun start(context: Context?, pkmn: Pokemon) {
            context?.startActivity(
                Intent(context, PkmnDetailsActivity::class.java)
                    .putExtra(DISPLAYED_POKEMON, pkmn))
        }
    }

    lateinit var binding: ActivityPkmnInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pkmn_info)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        setUpScreen()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpScreen() {
        val pkmn = intent.extras?.getSerializable(DISPLAYED_POKEMON) as Pokemon

        binding.txtInfoName.text = pkmn.name.capitalize()
        binding.txtHeight.text = pkmn.height.toString() + " cm"
        binding.txtWeight.text = getWeightToText(pkmn.weightInKilograms)
        binding.txtTypes.text = getTypesToText(pkmn.types)
        binding.txtAbilities.text = getAbilitiesToText(pkmn.abilities)

        val spriteList: PokemonSpriteList = pkmn.sprites // TODO Fetch other sprites
    }

    private fun getWeightToText(weight: Double?): String {
        return weight?.format(2) + " Kg"
    }

    private fun getAbilitiesToText(abilities: List<ApiPokemonAbility>): CharSequence? {
        var abilitiesToString = ""

        for (ability in abilities) {
            val formattedAbility = ability.ability.name.replace("-", " ").capitalize()

            abilitiesToString += "$formattedAbility \n"
        }

        return abilitiesToString
    }

    private fun getTypesToText(types: List<ApiPokemonType>): CharSequence? {
        var typesToString: String = types[0].type.name.capitalize()

        if (types.size > 1)
            typesToString += ", ${types[1].type.name.capitalize()}"

        return typesToString
    }
}
