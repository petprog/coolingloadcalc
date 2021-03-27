/**
 * Created by Taiwo Farinu on 27-Feb-21
 */

package com.android.petprog.coolingloadcalc.util

fun Double.convertFahrenheitToCelsius(): Double {
    return this
}

fun Int.convertFahrenheitToCelsius(): Int {
    return 0.0.toInt()
}

fun Double.convertFahrenheitToKelvin(): Double {
    return ((this - 32.0) / 1.8) + 273.4
}

fun Int.convertFahrenheitToKelvin(): Int {
    return (((this - 32.0) / 1.8) + 273.4).toInt()
}

fun Double.convertCelsiusToKelvin(): Double {
    return 0.0
}

fun Int.convertCelsiusToKelvin(): Int {
    return 0.0.toInt()
}