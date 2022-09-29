package com.example.ingredient.src.basket

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketGroupIngredient
import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GroupIngredientsAdapter() : RecyclerView.Adapter<GroupIngredientsAdapter.ViewHolder>() {
    private var context: Context? = null
    private var DataList = mutableListOf<Pair<String, ArrayList<BasketGroupIngredient>>>()
    private var click = true

    //그룹 (그룹명, 재료 리스트)
    //그룹 속 재료 (아이콘, 카테고리, 재료명, 수량)

    override fun getItemCount(): Int = DataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupIngredientsAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_group_ingredient, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupIngredientsAdapter.ViewHolder, position: Int) {
        holder.groupName.text = DataList[position].first
        holder.ingredientName.text = DataList[position].second[0].ingredientName
        holder.ingredientAmount.text = DataList[position].second[0].quantity.toString()
        Glide.with(holder.itemView)
            .load(DataList[position].second[0].ingredientIcon)
            .into(holder.ingredientIcon)

        holder.drawBtn.setOnClickListener {
            if(click) {
                holder.ingredientIcon.visibility = View.VISIBLE
                holder.ingredientName.visibility = View.VISIBLE
                holder.ingredientAmount.visibility = View.VISIBLE


            }
            else {
                holder.ingredientIcon.visibility = View.GONE
                holder.ingredientName.visibility = View.GONE
                holder.ingredientAmount.visibility = View.GONE
            }
            click = !click
        }
    }

    inner class ViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal var groupName : TextView
        internal var ingredientName : TextView
        internal var ingredientIcon : ImageView
        internal var ingredientAmount : TextView
        internal var drawBtn : Button

        init {
            context = context
            groupName = view.findViewById(R.id.group_ingredientgroupname)
            ingredientName = view.findViewById(R.id.group_ingredientname)
            ingredientIcon = view.findViewById(R.id.group_ingredienticon)
            ingredientAmount = view.findViewById(R.id.group_ingredientamount)
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

        Log.d("basketTest", "adpater : ${temp}")
        return temp
    }
    fun testmethod(basketList: ArrayList<BasketIngredient>) {
        Log.d("basketTest", "testmethod")
        DataList = GroupData(basketList)
        notifyDataSetChanged()
    }
}