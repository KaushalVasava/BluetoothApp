package com.kaushalvasava.org.apps.bluetoothapp.util

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.util.Log

object BluetoothUtil {
     fun makeDiscoverable(context: Context) {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        context.startActivity(discoverableIntent)
        Log.i("Log", "Discoverable ")
    }
}