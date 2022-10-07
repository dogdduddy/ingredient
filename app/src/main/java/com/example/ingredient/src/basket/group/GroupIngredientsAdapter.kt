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
    private var basketData: ArrayList<BasketIngredient> = ArrayList()
    private var basketList = arrayOf<String>()
    private var click = true
    private var adapter: BasketGroupAdapter? = null
    private var recycler:RecyclerView? = null

    override fun getItemCount(): Int = basketList.size

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
        holder.groupName.text = basketList[position]
        adapter!!.submitList(basketData.filter { it.groupName == basketList[position] })

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

    fun submitList(basketData: ArrayList<BasketIngredient>, basketList: Array<String>) {
        this.basketData = basketData
        this.basketList = basketList
        notifyDataSetChanged()
    }
}