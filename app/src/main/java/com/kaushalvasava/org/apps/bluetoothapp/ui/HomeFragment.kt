package com.kaushalvasava.org.apps.bluetoothapp.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kaushalvasava.org.apps.bluetoothapp.R
import com.kaushalvasava.org.apps.bluetoothapp.databinding.FragmentHomeBinding
import com.kaushalvasava.org.apps.bluetoothapp.model.Bluetooth
import com.kaushalvasava.org.apps.bluetoothapp.ui.adapter.BluetoothDeviceAdapter
import com.kaushalvasava.org.apps.bluetoothapp.ui.viewmodel.BluetoothViewModel
import com.kaushalvasava.org.apps.bluetoothapp.util.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), ItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private val btAdapter by lazy {
        BluetoothDeviceAdapter(this)
    }
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var mPermissionResultLauncher: ActivityResultLauncher<Array<String>>
    private val viewModel: BluetoothViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        val bluetoothManager =
            requireActivity().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        initPermission()
         checkPermission(requireContext(), mPermissionResultLauncher)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = btAdapter
        }
        addClickListeners()
    }

    private fun addClickListeners() {
        binding.btSwitch.setOnCheckedChangeListener { _, _ ->
            isBluetoothEnable = if (bluetoothAdapter.isEnabled) {
                bluetoothAdapter.disable()
                setRecyclerViewVisibility(false)
                binding.progressBar.isVisible = false
                false
            } else {
                bluetoothAdapter.enable()
                binding.progressBar.isVisible = true
                getDeviceList()
                true
            }
        }
        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }
    }

    private fun checkPermission(context: Context, launcher: ActivityResultLauncher<Array<String>>) {
        val permissions = if (Build.VERSION_CODES.R <= Build.VERSION.SDK_INT) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        val permission = ContextCompat.checkSelfPermission(
            context,
            permissions.toString()
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(permissions)
        }
    }

    private fun getDeviceList() {
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

    private fun initPermission() {
        mPermissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // Handle Permission granted/rejected
            var isAllow = false
            permissions.entries.forEach {
                isAllow = it.value
            }
            if (!isAllow) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.please_grant_permission),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.grant)) {
                    checkPermission(requireContext(), mPermissionResultLauncher)
                }.show()
            }
        }
    }

    private fun setRecyclerViewVisibility(isVisible: Boolean) {
        binding.recyclerView.isVisible = isVisible
    }

    override fun onItemClicked(item: BluetoothDevice) {
        item.createBond()
        val bluetooth = Bluetooth(item.address, item.name, 0, true)
        viewModel.insert(bluetooth)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            isBluetoothEnable = it.getBoolean(BLUETOOTH_ENABLE_BUNDLE_KEY, false)
        }
        if(isBluetoothEnable){
            bluetoothAdapter.enable()
            binding.progressBar.isVisible = true
            getDeviceList()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BLUETOOTH_ENABLE_BUNDLE_KEY, isBluetoothEnable)
    }

    private var isBluetoothEnable = false

    companion object {
        private const val BLUETOOTH_ENABLE_BUNDLE_KEY = "bluetooth_enable_bundle_key"
    }
}