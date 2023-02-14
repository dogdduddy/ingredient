package com.dogdduddy.ingredient.src.basket.total

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dogdduddy.ingredient.R
import com.dogdduddy.ingredient.src.basket.models.BasketGroupIngredient
import com.dogdduddy.ingredient.src.basket.models.BasketIngredient

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
        holder.Name.text = ingredientList[position].ingredientname
        holder.Quantity.text = ingredientList[position].ingredientquantity.toString() + category(ingredientList[position].ingredientcategory, ingredientList[position].ingredientname)
        holder.ingCategory.text = ingredientList[position].ingredientcategory

        Glide.with(holder.itemView)
            .load(ingredientList[position].ingredienticon)
            .into(holder.ingImage)

    }

    override fun getItemCount(): Int = ingredientList.size

    fun submitList(basketList: ArrayList<BasketIngredient>){
        ingredientList.clear()
        basketList.forEach { basket ->
            if(ingredientList.map {it.ingredientname}.indexOf(basket.ingredientname) == -1){
                ingredientList.add(BasketGroupIngredient(basket.ingredienticon, basket.ingredientidx, basket.ingredientname, basket.ingredientcategory, basket.ingredientquantity))
            }else{
                ingredientList[ingredientList.map {it.ingredientname}.indexOf(basket.ingredientname)]!!.ingredientquantity += basket.ingredientquantity
            }
        }
        notifyDataSetChanged()
    }
    fun category(categoryName: String, ingredientname: String):String {
        return when(categoryName) {
            "채소" -> {
                when{
                    ingredientname == "배추" -> "포기"
                    ingredientname == "상추" || ingredientname == "깻잎" -> "장"
                    ingredientname == "생강" || ingredientname == "마늘" -> "쪽"
                    Regex("버섯").containsMatchIn(ingredientname) -> "송이"
                    else -> "개"
                }
            }
            "육류" -> {
                when(ingredientname){
                    "돼지고기" -> "근"
                    "닭고기" -> "근"
                    "소고기" -> "근"
                    else -> "개"
                }
            }
            "수산물" -> when(ingredientname) {
                "생선" -> "마리"
                "멸치" -> "포"
                else -> "마리"
            }
            "과일" -> when(ingredientname) {
                "포도", "바나나" -> "송이"
                else -> "개"
            }
            "가공/유제품" -> when(ingredientname) {
                "두부" -> "모"
                "우유" -> "팩"
                else -> "개"
            }
            "조미료" -> when {
                ingredientname == "고춧가루" -> "근"
                Regex("간장").containsMatchIn(ingredientname) -> "종지"
                else -> "개"
            }
            else -> "개"
        }
    }

}