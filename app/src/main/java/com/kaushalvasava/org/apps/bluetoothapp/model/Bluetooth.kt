package com.kaushalvasava.org.apps.bluetoothapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bluetooth_table")
data class Bluetooth(
    @PrimaryKey val id: String,
    val name: String,
    val duration: Int,
    val isConnected: Boolean = false
)