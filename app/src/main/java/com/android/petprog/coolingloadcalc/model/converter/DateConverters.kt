/**
 * Created by Taiwo Farinu on 02-Apr-21
 */

package com.android.petprog.coolingloadcalc.model.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

}