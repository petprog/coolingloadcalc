/**
 * Created by Taiwo Farinu on 01-Mar-21
 */

package com.android.petprog.coolingloadcalc.model

data class StateTempDetail(
    val stateName: String,
    val maxTemp: Int,
    val dailyRangeTemp: Int,
    val meanTemp: Double = maxTemp.toDouble() - (dailyRangeTemp.toDouble() / 2)
)