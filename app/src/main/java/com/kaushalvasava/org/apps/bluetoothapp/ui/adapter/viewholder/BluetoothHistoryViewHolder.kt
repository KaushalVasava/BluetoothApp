package com.kaushalvasava.org.apps.bluetoothapp.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.kaushalvasava.org.apps.bluetoothapp.R
import com.kaushalvasava.org.apps.bluetoothapp.databinding.BluetoothHistoryItemBinding
import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import com.kaushalvasava.org.apps.bluetoothapp.util.AppUtil

class BluetoothHistoryViewHolder(
    private val binding: BluetoothHistoryItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Bluetooth) {
        binding.tvName.text = item.name
        binding.tvTime.text =
            binding.root.context.getString(R.string.minutes, AppUtil.getTime(item.duration))
    }
}