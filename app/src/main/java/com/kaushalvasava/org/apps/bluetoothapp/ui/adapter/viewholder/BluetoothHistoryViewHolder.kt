package com.kaushalvasava.org.apps.bluetoothapp.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.kaushalvasava.org.apps.bluetoothapp.databinding.BluetoothHistoryItemBinding
import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth

class BluetoothHistoryViewHolder(
    private val binding: BluetoothHistoryItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Bluetooth) {
        binding.tvName.text = item.name
        binding.tvTime.text = item.duration.toString()
    }
}