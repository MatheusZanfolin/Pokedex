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

        binding.txtInfoName.text = extras?.getString(MainActivity.POKEMON_NAME_KEY)?.capitalize()
        binding.txtHeight.text = toCentimeters(extras?.getInt(MainActivity.POKEMON_HEIGHT_KEY)).toString() + " cm"
        binding.txtWeight.text = toKilograms(extras?.getInt(MainActivity.POKEMON_WEIGHT_KEY)).toString() + " Kg"
        binding.txtTypes.text = getTypesToText(extras?.getSerializable(MainActivity.POKEMON_TYPES_KEY) as Array<ApiPokemonType>)
        binding.txtAbilities.text = getAbilitiesToText(extras?.getSerializable(MainActivity.POKEMON_ABILITIES_KEY) as Array<ApiPokemonAbility>)

        val spriteList: PokemonSpriteList = extras.getSerializable(MainActivity.POKEMON_SPRITES_KEY) as PokemonSpriteList

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

    private fun toKilograms(weightInHectograms: Int?): Double {
        return weightInHectograms?.times(0.1) ?: 0.0
    }

    private fun toCentimeters(heightInDecimeters: Int?): Int {
        return heightInDecimeters?.times(10) ?: 0
    }
}
