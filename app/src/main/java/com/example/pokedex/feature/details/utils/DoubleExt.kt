package com.example.pokedex.base.util

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)