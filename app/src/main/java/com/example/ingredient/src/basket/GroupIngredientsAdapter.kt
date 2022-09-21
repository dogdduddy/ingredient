package com.example.ingredient.src.basket

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketGroupIngredient
import com.example.ingredient.src.basket.models.BasketIngredient

class GroupIngredientsAdapter(var basketList: ArrayList<BasketIngredient>) : RecyclerView.Adapter<GroupIngredientsAdapter.ViewHolder>() {
    private var context: Context? = null

    //그룹 (그룹명, 재료 리스트)
    //그룹 속 재료 (아이콘, 카테고리, 재료명, 수량)

    override fun getItemCount(): Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_group_ingredient, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupIngredientsAdapter.ViewHolder, position: Int) {
        val temp = GroupData()
        Log.d("basketTest", "temp Data : ${temp[position].first}")
        holder.groupName.text = temp[position].first
    }

    inner class ViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal var groupName : TextView

        init {
            context = context
            groupName = view.findViewById(R.id.group_name)
        }
    }

    fun GroupData(): MutableList<Pair<String, ArrayList<BasketGroupIngredient>>> {
        var temp = mutableListOf<Pair<String, ArrayList<BasketGroupIngredient>>>()
        basketList.forEach {
            var position = temp.map {it.first}.indexOf(it.groupName)
            if (position != -1) {
                temp[position].second.add(BasketGroupIngredient(it.ingredientIcon, it.ingredientIdx, it.categoryName, it.ingredientName, it.quantity))
            } else {
                temp.add(it.groupName to arrayListOf(BasketGroupIngredient(it.ingredientIcon, it.ingredientIdx, it.categoryName, it.ingredientName, it.quantity)))
            }
        }
        Log.d("basketText", "adpater : ${temp}")
        return temp
    }
    fun testmethod() {
        Log.d("basketTest", "testmethod")
        notifyDataSetChanged()
    }

}