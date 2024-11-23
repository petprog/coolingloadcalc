/**
 * Created by Taiwo Farinu on 27-Mar-21
 */

package com.android.petprog.coolingloadcalc.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.petprog.coolingloadcalc.dao.CalculatedDataDao
import com.android.petprog.coolingloadcalc.model.CalculatedData
import com.android.petprog.coolingloadcalc.model.converter.DateConverters

@Database(entities = [CalculatedData::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class CalculateDataDatabase : RoomDatabase() {

    abstract fun calculatedDataDao(): CalculatedDataDao

}