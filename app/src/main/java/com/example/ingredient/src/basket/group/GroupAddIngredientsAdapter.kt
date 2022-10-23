package com.example.ingredient.src.basket.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityGroupAddIngredientsBinding
import com.example.ingredient.databinding.ItemGroupAddBinding

class GroupAddIngredientsAdapter: RecyclerView.Adapter<GroupAddIngredientsAdapter.ViewHolder>() {
    private var selectCheck = arrayListOf<Int>()
    private var data = arrayListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupAddIngredientsAdapter.ViewHolder {
        val binding = ItemGroupAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ViewHolder internal constructor(private val binding: ItemGroupAddBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : Int) {
            binding.addgroupRadioBtn.apply {
                text = data[item]

                isChecked = selectCheck[item] == 1
                setOnClickListener {
                    for(i in selectCheck.indices) {
                        if (i == item)
                            selectCheck[i] = 1
                        else
                            selectCheck[i] = 0
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }
    override fun getItemCount() = data.size

    fun submitList(data: ArrayList<String>) {
        this.data = data
        selectCheck.clear()
        for (i in data.indices) {
            if(i == 0 )
                selectCheck.add(1)
            else
                selectCheck.add(0)
        }
        notifyDataSetChanged()
    }

    fun selecCheck() : ArrayList<Int> {
        return selectCheck
    }
}