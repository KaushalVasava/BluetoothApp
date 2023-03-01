package com.kaushalvasava.org.apps.bluetoothapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth

@Database(
    entities = [Bluetooth::class],
    version = 1
)
abstract class BluetoothDatabase : RoomDatabase() {
    abstract val dao: BluetoothDao
}