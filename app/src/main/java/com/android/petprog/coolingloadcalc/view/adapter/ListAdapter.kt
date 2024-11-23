/**
 * Created by Taiwo Farinu on 28-Mar-21
 */

package com.android.petprog.coolingloadcalc.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.petprog.coolingloadcalc.databinding.CalculatedDataListItemBinding
import com.android.petprog.coolingloadcalc.model.CalculatedData
import com.android.petprog.coolingloadcalc.view.fragment.CalculatedDataListFragmentDirections
import java.text.SimpleDateFormat

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<CalculatedData>()

    class MyViewHolder(private val binding: CalculatedDataListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: CalculatedData) {
            binding.apply {
                itemPurposeTextView.text = currentItem.purposeOfSpace
                itemLocationTextView.text = currentItem.stateName
                itemSpaceNameTextView.text = currentItem.spaceName
                val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy  HH:mm:ss")
                val formattedDate: String = simpleDateFormat.format(currentItem.date)
                itemDateTextView.text = formattedDate
                itemTotalLoadTextView.text = "${currentItem.totalLoad}KW"

                rowLayout.setOnClickListener {
                    val action = CalculatedDataListFragmentDirections
                        .actionCalculatedDataListFragmentToDetailFragment(currentItem)
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CalculatedDataListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.bind(currentItem)
    }

    fun setData(calculateDataList: List<CalculatedData>) {
        this.userList = calculateDataList
        notifyDataSetChanged()
    }
}