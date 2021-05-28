/**
 * Created by Taiwo Farinu on 27-Mar-21
 */

package com.android.petprog.coolingloadcalc.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.petprog.coolingloadcalc.model.CalculatedData

@Dao
interface CalculatedDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCalculatedData(data: CalculatedData)

    @Query("SELECT * FROM calculated_data ORDER BY id ASC")
    suspend fun readAllData(): List<CalculatedData>
}