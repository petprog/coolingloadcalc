/**
 * Created by Taiwo Farinu on 28-Mar-21
 */

package com.android.petprog.coolingloadcalc.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.petprog.coolingloadcalc.R
import com.android.petprog.coolingloadcalc.model.CalculatedData
import com.android.petprog.coolingloadcalc.view.fragment.CalculatedDataListFragmentDirections
import kotlinx.android.synthetic.main.calculated_data_list_item.view.*
import java.text.SimpleDateFormat

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<CalculatedData>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calculated_data_list_item, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.itemView.itemPurposeTextView.text = currentItem.purposeOfSpace
        holder.itemView.itemLocationTextView.text = currentItem.stateName
        holder.itemView.itemSpaceNameTextView.text = currentItem.spaceName
        val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy  HH:mm:ss")
        val formattedDate: String = simpleDateFormat.format(currentItem.date)
        holder.itemView.itemDateTextView.text = formattedDate
        holder.itemView.itemTotalLoadTextView.text = currentItem.totalLoad.toString() + "KW"

        holder.itemView.row_layout.setOnClickListener {
            val action = CalculatedDataListFragmentDirections.actionCalculatedDataListFragmentToDetailFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(calculateDataList: List<CalculatedData>) {
        this.userList = calculateDataList
        notifyDataSetChanged()
    }
}