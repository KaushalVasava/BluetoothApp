package com.kaushalvasava.org.apps.bluetoothapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kaushalvasava.org.apps.bluetoothapp.App
import com.kaushalvasava.org.apps.bluetoothapp.R
import com.kaushalvasava.org.apps.bluetoothapp.databinding.FragmentHomeBinding
import com.kaushalvasava.org.apps.bluetoothapp.ui.adapter.BluetoothDeviceAdapter
import com.kaushalvasava.org.apps.bluetoothapp.ui.viewmodel.BluetoothViewModel
import com.kaushalvasava.org.apps.bluetoothapp.util.AppUtil
import com.kaushalvasava.org.apps.bluetoothapp.util.ItemClickListener
import com.kaushalvasava.org.apps.bluetoothapp.util.toBluetooth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.OutputStream
import java.util.*

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class HomeFragment : Fragment(R.layout.fragment_home), ItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private val btAdapter by lazy {
        BluetoothDeviceAdapter(this)
    }
    private val bluetoothManager by lazy {
        App.appContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        if (Build.VERSION.SDK_INT >= 31) {
            bluetoothManager?.adapter
        } else {
            @Suppress("deprecation")
            BluetoothAdapter.getDefaultAdapter();
        }
    }
    private var isDeviceConnected = false
    private val viewModel: BluetoothViewModel by viewModels()
    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* Not needed */ }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms[Manifest.permission.BLUETOOTH_CONNECT] == true
        } else true

        if (canEnableBluetooth) {
            enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
    }
    private val bluetoothStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    //Do something you need here
                    isDeviceConnected = true
                    startTime = System.currentTimeMillis()
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    isDeviceConnected = false
                    if (connectedPosition != -1) {
                        val duration = System.currentTimeMillis() - startTime
                        val data = btAdapter.currentList[connectedPosition]
                        viewModel.insert(data.toBluetooth(duration = duration))
                    }
                    startTime = 0
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.bluetooth_disconnect),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> println("Default")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = btAdapter
        }
        addClickListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            if (connectedPosition != -1) {
                val item = btAdapter.currentList[connectedPosition]
                val batteryPercentage = AppUtil.getBatteryLevel(item)
                if (batteryPercentage != -1 && batteryPercentage < BATTERY_LOW_PERCENTAGE) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.battery_low_info),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        context?.registerReceiver(bluetoothStateReceiver, filter)
    }

    private fun addClickListeners() {
        binding.btSwitch.setOnCheckedChangeListener { _, isChecked ->
            bluetoothAdapter?.let {
                isBluetoothEnable = if (!isChecked) {
                    it.disable()
                    setRecyclerViewVisibility(false)
                    binding.progressBar.isVisible = false
                    false
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT,
                            )
                        )
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.BLUETOOTH,
                            )
                        )
                    }
                    binding.progressBar.isVisible = true
                    getDeviceList(it)
                    true
                }
            }
        }
        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }
    }

    private fun getDeviceList(bluetoothAdapter: BluetoothAdapter) {
        lifecycleScope.launch {
            val list = mutableListOf<BluetoothDevice>()
            var pairedDevices = bluetoothAdapter.bondedDevices
            withContext(Dispatchers.IO) {
                while (!bluetoothAdapter.isDiscovering) {
                    pairedDevices = bluetoothAdapter.bondedDevices
                    if (pairedDevices.isNotEmpty() || bluetoothAdapter.isDiscovering) {
                        bluetoothAdapter.cancelDiscovery()
                        break
                    }
                }
            }
            if (pairedDevices.size > 0) {
                for (device in pairedDevices) {
                    list.add(device)
                }
            }
            binding.progressBar.isVisible = false
            setRecyclerViewVisibility(true)
            btAdapter.submitList(list)
        }
    }

    private fun setRecyclerViewVisibility(isVisible: Boolean) {
        binding.recyclerView.isVisible = isVisible
    }

    override fun onItemClicked(item: BluetoothDevice, position: Int) {
        connectDevice(item, position)
    }

    private fun connectDevice(device: BluetoothDevice, position: Int) {
        val uuid = UUID.randomUUID()
        val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
        try {
            socket.connect()
            connectedPosition = position
            connectedAddress = device.address
            val outputStream: OutputStream = socket.outputStream
            outputStream.write(PAIRING_PIN.toByteArray())
        } catch (e: IOException) {
            Toast.makeText(
                requireContext(),
                "Error connecting to device ${device.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            isBluetoothEnable = it.getBoolean(BLUETOOTH_ENABLE_BUNDLE_KEY, false)
            connectedPosition = it.getInt(CONNECTED_POSITION_BUNDLE_KEY, -1)
            connectedAddress = it.getString(CONNECTED_ADDRESS_BUNDLE_KEY, "")
            startTime = it.getLong(START_TIME_BUNDLE_KEY, 0L)
        }
        if (isBluetoothEnable) {
            bluetoothAdapter?.enable()
            binding.progressBar.isVisible = true
            binding.recyclerView.isVisible = true
            bluetoothAdapter?.let {
                getDeviceList(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BLUETOOTH_ENABLE_BUNDLE_KEY, isBluetoothEnable)
        outState.putInt(CONNECTED_POSITION_BUNDLE_KEY, connectedPosition)
        outState.putString(CONNECTED_ADDRESS_BUNDLE_KEY, connectedAddress)
        outState.putLong(START_TIME_BUNDLE_KEY, startTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        context?.unregisterReceiver(bluetoothStateReceiver)
    }

    private var isBluetoothEnable = false
    private var connectedPosition = -1
    private var connectedAddress = ""
    private var startTime: Long = 0

    companion object {
        private const val BATTERY_LOW_PERCENTAGE = 15
        private const val PAIRING_PIN = "1234"
        private const val BLUETOOTH_ENABLE_BUNDLE_KEY = "bluetooth_enable_bundle_key"
        private const val CONNECTED_POSITION_BUNDLE_KEY = "connected_position_bundle_key"
        private const val CONNECTED_ADDRESS_BUNDLE_KEY = "connected_address_bundle_key"
        private const val START_TIME_BUNDLE_KEY = "start_time_bundle_key"
    }
}