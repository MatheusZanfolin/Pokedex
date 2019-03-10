package com.example.pokedex

import android.annotation.SuppressLint
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.example.pokedex.databinding.ActivityPkmnInfoBinding
import com.example.pokedex.model.ApiPokemonAbility
import com.example.pokedex.model.ApiPokemonType
import com.example.pokedex.model.PokemonSpriteList
import com.example.pokedex.model.PokemonType
import com.example.pokedex.task.GetPokemonSpritesTask
import kotlinx.android.synthetic.main.activity_pkmn_info.*
import java.lang.ref.WeakReference

class PkmnInfoActivity : AppCompatActivity() {

    companion object {
        val POKEMON_ID_KEY = "id"
        val POKEMON_NAME_KEY = "name"
        val POKEMON_HEIGHT_KEY = "height"
        val POKEMON_WEIGHT_KEY = "weight"
        var POKEMON_TYPES_KEY = "type"
        val POKEMON_SPRITES_KEY = "sprites"
        val POKEMON_ABILITIES_KEY = "abilities"
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
        val extras = intent.extras

        binding.txtInfoName.text = extras?.getString(POKEMON_NAME_KEY)?.capitalize()
        binding.txtHeight.text = extras?.getInt(POKEMON_HEIGHT_KEY).toString() + " cm"
        binding.txtWeight.text = getWeightToText(extras?.getDouble(POKEMON_WEIGHT_KEY))
        binding.txtTypes.text = getTypesToText(extras?.getSerializable(POKEMON_TYPES_KEY) as Array<ApiPokemonType>)
        binding.txtAbilities.text = getAbilitiesToText(extras.getSerializable(POKEMON_ABILITIES_KEY) as Array<ApiPokemonAbility>)

        val spriteList: PokemonSpriteList = extras.getSerializable(POKEMON_SPRITES_KEY) as PokemonSpriteList

        GetPokemonSpritesTask(
            WeakReference(imgFrontDefault),
            WeakReference(imgFrontFemale),
            WeakReference(imgFrontShiny)
        ).execute(
            spriteList.front_default,
            spriteList.front_female,
            spriteList.front_shiny
        )
    }

    private fun getWeightToText(weight: Double?): String {
        return weight?.format(2) + " Kg"
    }

    fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

    private fun getAbilitiesToText(abilities: Array<ApiPokemonAbility>): CharSequence? {
        var abilitiesToString = ""

        for (ability in abilities) {
            val formattedAbility = ability.ability.name.replace("-", " ").capitalize()

            abilitiesToString += "$formattedAbility \n"
        }

        return abilitiesToString
    }

    private fun getTypesToText(types: Array<ApiPokemonType>): CharSequence? {
        var typesToString: String = types[0].type.name.capitalize()

        if (types.size > 1)
            typesToString += ", ${types[1].type.name.capitalize()}"

        return typesToString
    }
}
