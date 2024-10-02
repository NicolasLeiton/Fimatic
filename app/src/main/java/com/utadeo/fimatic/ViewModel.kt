package com.utadeo.fimatic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _sharedData = MutableLiveData<String>()
    val sharedData: LiveData<String> get() = _sharedData

    // Método para actualizar los datos compartidos
    fun setData(data: String) {
        _sharedData.value = data
    }
}
