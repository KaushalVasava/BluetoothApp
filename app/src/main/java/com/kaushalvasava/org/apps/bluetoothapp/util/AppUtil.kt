package com.kaushalvasava.org.apps.bluetoothapp.util

import android.bluetooth.BluetoothDevice
import com.kaushalvasava.org.apps.bluetoothapp.util.AppConstant.TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

object AppUtil {
    fun getTime(timeInMillis: Long): String {
        val sdf = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return sdf.format(Date(timeInMillis))
    }

    fun getBatteryLevel(pairedDevice: BluetoothDevice?): Int {
        return pairedDevice?.let { bluetoothDevice ->
            (bluetoothDevice.javaClass.getMethod("getBatteryLevel"))
                .invoke(pairedDevice) as Int
        } ?: -1
    }
}