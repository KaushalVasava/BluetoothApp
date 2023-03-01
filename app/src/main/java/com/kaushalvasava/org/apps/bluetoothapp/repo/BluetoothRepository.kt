package com.kaushalvasava.org.apps.bluetoothapp.repo

import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {

    suspend fun insertTodo(bluetooth: Bluetooth)

    suspend fun deleteTodo(bluetooth: Bluetooth)

    suspend fun updateTodo(bluetooth: Bluetooth)

    fun getAllRecords(): Flow<List<Bluetooth>>
}