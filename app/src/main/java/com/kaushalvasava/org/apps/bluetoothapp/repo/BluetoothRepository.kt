package com.kaushalvasava.org.apps.bluetoothapp.repo

import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {

    suspend fun insertBluetooth(bluetooth: Bluetooth)

    suspend fun deleteBluetooth(bluetooth: Bluetooth)

    suspend fun updateBluetooth(bluetooth: Bluetooth)

    fun getAllRecords(): Flow<List<Bluetooth>>
    suspend fun deleteAllRecords()
}