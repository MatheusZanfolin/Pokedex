package com.zanfolin.pokedex.feature.details.utils

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)