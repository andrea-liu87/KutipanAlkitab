package com.andreasgift.kutipanalkitab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.andreasgift.kutipanalkitab.data.AyatViewModel
import com.andreasgift.kutipanalkitab.data.BackgroundViewModel
import com.andreasgift.kutipanalkitab.databinding.FragmentKutipanBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KutipanFragment : Fragment() {
    private var _binding: FragmentKutipanBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AyatViewModel>()
    private val backgroundviewmodel by activityViewModels<BackgroundViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKutipanBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ayatviewmodel = viewModel
        binding.backgroundviewmodel = backgroundviewmodel

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.downloadNewData()
            backgroundviewmodel.downloadNewBG()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}