/**
 * Created by Taiwo Farinu on 27-Mar-21
 */

package com.android.petprog.coolingloadcalc.model

import androidx.lifecycle.LiveData
import com.android.petprog.coolingloadcalc.dao.CalculatedDataDao

class CalculatedDataRepository(private val calculatedDataDao: CalculatedDataDao) {
    val readAllData: LiveData<List<CalculatedData>> = calculatedDataDao.readAllData()

    suspend fun addCalculatedData(data: CalculatedData) {
        calculatedDataDao.addCalculatedData(data)
    }
}