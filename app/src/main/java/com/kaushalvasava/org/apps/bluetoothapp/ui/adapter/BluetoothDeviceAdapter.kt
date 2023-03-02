package com.kaushalvasava.org.apps.bluetoothapp.ui.adapter

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kaushalvasava.org.apps.bluetoothapp.databinding.BluetoothItemBinding
import com.kaushalvasava.org.apps.bluetoothapp.ui.adapter.viewholder.BluetoothDeviceViewHolder
import com.kaushalvasava.org.apps.bluetoothapp.util.ItemClickListener

class BluetoothDeviceAdapter(private val listener: ItemClickListener) :
    ListAdapter<BluetoothDevice, BluetoothDeviceViewHolder>(BluetoothDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder {
        val binding =
            BluetoothItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BluetoothDeviceViewHolder(
            binding = binding,
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: BluetoothDeviceViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class BluetoothDiffCallback : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice) =
            oldItem.address == newItem.address

        override fun areContentsTheSame(
            oldItem: BluetoothDevice,
            newItem: BluetoothDevice
        ): Boolean = oldItem == newItem && oldItem == newItem
    }
}
