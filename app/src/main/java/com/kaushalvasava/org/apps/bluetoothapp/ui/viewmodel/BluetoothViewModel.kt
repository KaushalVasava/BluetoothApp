package com.kaushalvasava.org.apps.bluetoothapp.ui.viewmodel

import androidx.lifecycle.*
import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import com.kaushalvasava.org.apps.bluetoothapp.repo.BluetoothRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val repository: BluetoothRepository,
) : ViewModel() {

    private val _records = MutableStateFlow<List<Bluetooth>>(emptyList())
    val records = _records.asStateFlow()


    suspend fun getData() {
        viewModelScope.launch {
            repository.getAllRecords().collectLatest {
                _records.value = it
            }
        }
    }

    fun insert(bluetooth: Bluetooth) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertBluetooth(bluetooth)
    }

    fun update(bluetooth: Bluetooth) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateBluetooth(bluetooth)
    }

    fun delete(bluetooth: Bluetooth) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteBluetooth(bluetooth)
    }

    fun deleteAllRecords() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllRecords()
    }
}