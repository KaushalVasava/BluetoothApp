package com.kaushalvasava.org.apps.bluetoothapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kaushalvasava.org.apps.bluetoothapp.R
import com.kaushalvasava.org.apps.bluetoothapp.databinding.FragmentHistoryBinding
import com.kaushalvasava.org.apps.bluetoothapp.ui.adapter.BluetoothHistoryAdapter
import com.kaushalvasava.org.apps.bluetoothapp.ui.viewmodel.BluetoothViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {
    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding
        get() = _binding!!

    private val bluetoothHistoryAdapter by lazy {
        BluetoothHistoryAdapter()
    }
    private val viewModel: BluetoothViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHistoryBinding.bind(view)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = bluetoothHistoryAdapter
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getData()
            viewModel.records.collectLatest {
                bluetoothHistoryAdapter.submitList(it)
            }
        }
        binding.btnClearHistory.setOnClickListener {
            viewModel.deleteAllRecords()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}