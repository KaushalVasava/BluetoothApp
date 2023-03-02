package com.kaushalvasava.org.apps.bluetoothapp.repo

import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import com.kaushalvasava.org.apps.bluetoothapp.db.BluetoothDao
import kotlinx.coroutines.flow.Flow

class BluetoothRepositoryImpl(
    private val dao: BluetoothDao,
) : BluetoothRepository {

    override suspend fun insertBluetooth(bluetooth: Bluetooth) {
        dao.insert(bluetooth)
    }

    override suspend fun deleteBluetooth(bluetooth: Bluetooth) {
        dao.delete(bluetooth)
    }

    override suspend fun updateBluetooth(bluetooth: Bluetooth) {
        dao.update(bluetooth)
    }

    override fun getAllRecords(
    ): Flow<List<Bluetooth>> {
        return dao.getAllRecords()
    }

    override suspend fun deleteAllRecords() {
        dao.deleteAll()
    }
}