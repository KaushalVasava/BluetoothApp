package com.kaushalvasava.org.apps.bluetoothapp.util

import android.bluetooth.BluetoothDevice
import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth

fun BluetoothDevice.toBluetooth(duration: Long): Bluetooth {
    return Bluetooth(address, name, duration)
}