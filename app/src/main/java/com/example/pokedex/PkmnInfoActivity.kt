package com.example.pokedex

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.example.pokedex.databinding.ActivityPkmnInfoBinding
import com.example.pokedex.model.PokemonSpriteList
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

    private fun setUpScreen() {
        val extras = intent.extras

        binding.txtInfoName.setText(extras?.getString(MainActivity.POKEMON_NAME_KEY)?.capitalize())

        val spriteList: PokemonSpriteList = extras?.getSerializable(MainActivity.POKEMON_SPRITES_KEY) as PokemonSpriteList

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
}
