package com.kaushalvasava.org.apps.bluetoothapp.util

import android.bluetooth.BluetoothDevice

interface ItemClickListener {
    fun onItemClicked(item: BluetoothDevice)
}