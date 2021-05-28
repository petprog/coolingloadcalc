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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_calculated_data_list.view.*

@AndroidEntryPoint
class CalculatedDataListFragment : Fragment() {

    private val calculatedDataViewModel: CalculatedDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calculated_data_list, container, false)

        val adapter = ListAdapter()
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager(requireContext()).orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        calculatedDataViewModel.calculatedListData.observe(
            viewLifecycleOwner,
            Observer { calculateDataList ->
                adapter.setData(calculateDataList)
            })

        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_calculatedDataListFragment_to_CoolingLoadCalculatorFragment)
        }

        return view

    }
}