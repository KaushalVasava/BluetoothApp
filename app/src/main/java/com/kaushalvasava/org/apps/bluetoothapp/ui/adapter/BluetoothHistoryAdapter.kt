package com.kaushalvasava.org.apps.bluetoothapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kaushalvasava.org.apps.bluetoothapp.databinding.BluetoothHistoryItemBinding
import com.kaushalvasava.org.apps.bluetoothapp.databinding.BluetoothItemBinding
import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import com.kaushalvasava.org.apps.bluetoothapp.ui.adapter.viewholder.BluetoothHistoryViewHolder

class BluetoothHistoryAdapter :
    ListAdapter<Bluetooth, BluetoothHistoryViewHolder>(BluetoothHistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothHistoryViewHolder {
        val binding =
            BluetoothHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BluetoothHistoryViewHolder(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: BluetoothHistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class BluetoothHistoryDiffCallback : DiffUtil.ItemCallback<Bluetooth>() {
        override fun areItemsTheSame(oldItem: Bluetooth, newItem: Bluetooth) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Bluetooth,
            newItem: Bluetooth
        ): Boolean = oldItem == newItem && oldItem == newItem
    }
}