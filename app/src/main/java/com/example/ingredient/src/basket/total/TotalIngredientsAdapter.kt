package com.example.ingredient.src.basket.total

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketGroupIngredient
import com.example.ingredient.src.basket.models.BasketIngredient

class TotalIngredientsAdapter():RecyclerView.Adapter<TotalIngredientsAdapter.ViewHolder>() {
    private var context: Context? = null
    private var ingredientList = arrayListOf<BasketGroupIngredient>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_total_ingredient, parent, false)
        context = view.context

        return ViewHolder(view)
    }
    inner class ViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal var Name : TextView
        internal var Quantity : TextView
        internal var ingImage : ImageView
        internal var ingCategory : TextView

        init {
            context = context
            Name = view.findViewById(R.id.total_inner_ing_name)
            Quantity = view.findViewById(R.id.total_inner_ing_cnt)
            ingImage = view.findViewById(R.id.total_inner_ing_icon)
            ingCategory = view.findViewById(R.id.total_inner_ing_category)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Name.text = ingredientList[position].ingredientName
        holder.Quantity.text = ingredientList[position].quantity.toString() + category(ingredientList[position].categoryName, ingredientList[position].ingredientName)
        holder.ingCategory.text = ingredientList[position].categoryName

        Glide.with(holder.itemView)
            .load(ingredientList[position].ingredientIcon)
            .into(holder.ingImage)

    }

    override fun getItemCount(): Int = ingredientList.size

    fun submitList(basketList: ArrayList<BasketIngredient>){
        ingredientList.clear()
        basketList.forEach { basket ->
            if(ingredientList.map {it.ingredientName}.indexOf(basket.ingredientName) == -1){
                ingredientList.add(BasketGroupIngredient(basket.ingredientIcon, basket.ingredientIdx, basket.ingredientName, basket.categoryName, basket.quantity))
            }else{
                ingredientList[ingredientList.map {it.ingredientName}.indexOf(basket.ingredientName)]!!.quantity += basket.quantity
            }
        }
        notifyDataSetChanged()
    }
    fun category(categoryName: String, ingreidentName: String):String {
        Log.d("categoryTest", "categoryName : $categoryName  /  $ingreidentName")
        return when(categoryName) {
            "채소" -> {
                when(ingreidentName) {
                    "배추" -> "포기"
                    "상추", "깻잎" -> "장"
                    "생강", "마늘" -> "쪽"
                    "버섯" -> "송이"
                    else -> "개"
                }
            }
            "육류" -> "근"
            "수산물" -> when(ingreidentName) {
                "생선" -> "마리"
                else -> "개"
            }
            "과일" -> when(ingreidentName) {
                "포도", "바나나" -> "송이"
                else -> "개"
            }
            else -> "개"
        }
    }

}