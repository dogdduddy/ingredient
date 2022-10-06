package com.example.ingredient.src.basket.group

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.src.basket.BasketGroupAdapter
import com.example.ingredient.src.basket.models.BasketGroupIngredient
import com.example.ingredient.src.basket.models.BasketIngredient

class GroupIngredientsAdapter() : RecyclerView.Adapter<GroupIngredientsAdapter.ViewHolder>() {
    private var context: Context? = null
    private var DataList = mutableListOf<Pair<String, ArrayList<BasketGroupIngredient>>>()
    private var click = true
    private var adapter: BasketGroupAdapter? = null
    private var recycler:RecyclerView? = null

    override fun getItemCount(): Int = DataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_group_ingredient, parent, false)
        context = view.context
        recycler = view.findViewById(R.id.group_recyclerview)
        recycler!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = BasketGroupAdapter()
        recycler!!.adapter = adapter

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.groupName.text = DataList[position].first
        adapter!!.submitList(DataList[position].second)
        holder.drawBtn.setOnClickListener {
            if(click) {
                holder.recyclerView.visibility = View.VISIBLE
            }
            else {
                holder.recyclerView.visibility = View.GONE
            }
            click = !click
        }
    }

    inner class ViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal var groupName : TextView
        internal var recyclerView : RecyclerView
        internal var drawBtn : Button

        init {
            context = context
            groupName = view.findViewById(R.id.group_ingredientgroupname)
            recyclerView = view.findViewById(R.id.group_recyclerview)
            drawBtn = view.findViewById(R.id.group_drawBtn)
        }
    }

    // BasketIngredient 리스트를 그룹 묶음 출력물로 변형
    fun GroupData(basketList: ArrayList<BasketIngredient>) : MutableList<Pair<String, ArrayList<BasketGroupIngredient>>>{
        var temp = mutableListOf<Pair<String, ArrayList<BasketGroupIngredient>>>()
        basketList.forEach {
            var position = temp.map {it.first}.indexOf(it.groupName)
            if (position != -1) {
                temp[position].second.add(BasketGroupIngredient(it.ingredientIcon, it.ingredientIdx, it.categoryName, it.ingredientName, it.quantity))
            } else {
                temp.add(it.groupName to arrayListOf(BasketGroupIngredient(it.ingredientIcon, it.ingredientIdx, it.categoryName, it.ingredientName, it.quantity)))
            }
        }
        return temp
    }
    fun submitList(basketList: ArrayList<BasketIngredient>) {
        Log.d("submitlist", "testmethod ${basketList}")
        DataList = GroupData(basketList)
        notifyDataSetChanged()
    }
}