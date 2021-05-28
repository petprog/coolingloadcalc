/**
 * Created by Taiwo Farinu on 27-Mar-21
 */

package com.android.petprog.coolingloadcalc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.petprog.coolingloadcalc.model.CalculatedData
import com.android.petprog.coolingloadcalc.model.CalculatedDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatedDataViewModel @Inject constructor(private val repository: CalculatedDataRepository) :
    ViewModel() {

    private val _data = MutableLiveData<List<CalculatedData>>()
    val calculatedListData: LiveData<List<CalculatedData>>
        get() = _data

    init {
        readAllData()
    }

    private fun readAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            _data.postValue(repository.readAllData())
        }
    }

//    val readAllData: LiveData<List<CalculatedData>> = repository.readAllData

    fun addCalculatedData(calculatedData: CalculatedData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCalculatedData(calculatedData)
        }
    }


}