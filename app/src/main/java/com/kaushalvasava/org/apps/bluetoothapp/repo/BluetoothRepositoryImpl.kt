package com.kaushalvasava.org.apps.bluetoothapp.repo

import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import com.kaushalvasava.org.apps.bluetoothapp.db.BluetoothDao
import kotlinx.coroutines.flow.Flow

class BluetoothRepositoryImpl(
    private val dao: BluetoothDao,
) : BluetoothRepository {

    override suspend fun insertTodo(bluetooth: Bluetooth) {
        dao.insert(bluetooth)
    }

    override suspend fun deleteTodo(bluetooth: Bluetooth) {
        dao.delete(bluetooth)
    }

    override suspend fun updateTodo(bluetooth: Bluetooth) {
        dao.update(bluetooth)
    }

    override fun getAllRecords(
     ): Flow<List<Bluetooth>> {
        return dao.getAllRecords()
    }
}