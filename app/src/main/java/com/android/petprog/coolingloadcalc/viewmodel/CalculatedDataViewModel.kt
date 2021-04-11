/**
 * Created by Taiwo Farinu on 27-Mar-21
 */

package com.android.petprog.coolingloadcalc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.petprog.coolingloadcalc.model.CalculatedData
import com.android.petprog.coolingloadcalc.model.CalculatedDataRepository
import com.android.petprog.coolingloadcalc.model.database.CalculateDataDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalculatedDataViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<CalculatedData>>
    private val repository: CalculatedDataRepository

    init {
        val calculatedDao = CalculateDataDatabase.getDatabase(application).calculatedDataDao()
        repository = CalculatedDataRepository(calculatedDao)
        readAllData =  repository.readAllData
    }

    fun addCalculatedData(calculatedData: CalculatedData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCalculatedData(calculatedData)
        }
    }


}