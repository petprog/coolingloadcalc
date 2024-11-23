package com.android.petprog.coolingloadcalc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.petprog.coolingloadcalc.R
import com.android.petprog.coolingloadcalc.view.adapter.ListAdapter
import com.android.petprog.coolingloadcalc.viewmodel.CalculatedDataViewModel
import com.android.petprog.coolingloadcalc.databinding.FragmentCalculatedDataListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculatedDataListFragment : Fragment() {

    private val calculatedDataViewModel: CalculatedDataViewModel by viewModels()
    private var _binding: FragmentCalculatedDataListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalculatedDataListBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = ListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager(requireContext()).orientation
        )
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        calculatedDataViewModel.calculatedListData.observe(viewLifecycleOwner, Observer { calculateDataList ->
            adapter.setData(calculateDataList)
        })

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_calculatedDataListFragment_to_CoolingLoadCalculatorFragment)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding when the view is destroyed
    }
}
