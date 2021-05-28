/**
 * Created by Taiwo Farinu on 27-Mar-21
 */

package com.android.petprog.coolingloadcalc.model

import androidx.lifecycle.LiveData
import com.android.petprog.coolingloadcalc.dao.CalculatedDataDao
import javax.inject.Inject

class CalculatedDataRepository @Inject constructor(private val calculatedDataDao: CalculatedDataDao) {
    suspend fun readAllData(): List<CalculatedData> = calculatedDataDao.readAllData()

    suspend fun addCalculatedData(data: CalculatedData) = calculatedDataDao.addCalculatedData(data)
}