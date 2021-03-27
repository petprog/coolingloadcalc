/**
 * Created by Taiwo Farinu on 01-Mar-21
 */

package com.android.petprog.coolingloadcalc.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalculatedData(
    var stateName: String,
    var spaceArea: Double,
    var purposeOfSpace: String,
    var roofType: String,
    var roofArea: Double,
    var roofLoad: Double,
    val wallType: String,
    val wallAArea: Double,
    val wallBArea: Double,
    val wallCArea: Double,
    val wallDArea: Double,
    val wallAOrientation: String,
    val wallBOrientation: String,
    val wallCOrientation: String,
    val wallDOrientation: String,
    val wallLoad: Double,
    val floorType: String,
    val floorArea: Double,
    val floorLoad: Double,
    val windowType: String,
    val windowArea: Double,
    val windowLoad: Double,
    val doorArea: Double,
    val doorOrientation: String,
    val doorLoad: Double,
    val ceilingType: String,
    val ceilingArea: Double,
    val ceilingLoad: Double,
    val listOfEquipments: String
//    val totalEquipmentsLoad: Double
) : Parcelable {

    @IgnoredOnParcel
    val description: String = "space area $spaceArea"
}