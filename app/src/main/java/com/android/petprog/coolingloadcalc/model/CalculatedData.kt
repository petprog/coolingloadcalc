/**
 * Created by Taiwo Farinu on 01-Mar-21
 */

package com.android.petprog.coolingloadcalc.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.petprog.coolingloadcalc.model.converter.DateConverters
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "calculated_data")
data class CalculatedData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @TypeConverters(DateConverters::class)
    val date: Date,
    val spaceName: String,
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
    val listOfEquipments: String,
    val equipmentLoad: Double,
    val numberOfPeople: Int,
    val peopleLoad: Double,
    val totalLoad: Double
//    val totalEquipmentsLoad: Double
) : Parcelable {

//    @IgnoredOnParcel
//    val description: String = "space area $spaceArea"
}