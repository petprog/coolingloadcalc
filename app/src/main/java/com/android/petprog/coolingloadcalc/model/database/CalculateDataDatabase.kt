/**
 * Created by Taiwo Farinu on 27-Mar-21
 */

package com.android.petprog.coolingloadcalc.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.petprog.coolingloadcalc.dao.CalculatedDataDao
import com.android.petprog.coolingloadcalc.model.CalculatedData
import com.android.petprog.coolingloadcalc.model.converter.DateConverters

@Database(entities = [CalculatedData::class], version = 2, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class CalculateDataDatabase : RoomDatabase() {

    abstract fun calculatedDataDao(): CalculatedDataDao

//    companion object {
//        @Volatile
//        private var INSTANCE: CalculateDataDatabase? = null
//
//        fun getDatabase(context: Context): CalculateDataDatabase {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    CalculateDataDatabase::class.java,
//                    "calculated_data_database_4"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
}