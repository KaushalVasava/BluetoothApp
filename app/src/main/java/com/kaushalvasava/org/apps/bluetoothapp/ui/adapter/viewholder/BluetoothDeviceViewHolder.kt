package com.kaushalvasava.org.apps.bluetoothapp.ui.adapter.viewholder

import android.bluetooth.BluetoothDevice
import androidx.recyclerview.widget.RecyclerView
import com.kaushalvasava.org.apps.bluetoothapp.databinding.BluetoothItemBinding
import com.kaushalvasava.org.apps.bluetoothapp.util.ItemClickListener

class BluetoothDeviceViewHolder(
    private val binding: BluetoothItemBinding,
    private val listener: ItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BluetoothDevice) {
        binding.tvName.text = item.name?:""
        binding.root.setOnClickListener {
            listener.onItemClicked(item)
        }
    }
}