package com.example.ingredient.src.expirationDate.add_ingredient.ingredientstate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import java.time.LocalDate
import kotlin.collections.ArrayList

class IngredientStateAdapter(val ingredients:ArrayList<Ingredient>, val activity:IngredientStateActivity) : RecyclerView.Adapter<IngredientStateAdapter.ViewHolder>() {
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_add_ingredientstate, parent, false)
        context = view.context
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientName.text = ingredients[position].ingredientName
        holder.ingredientGroup.setOnCheckedChangeListener { group, checkedId ->
            if(holder.storageGroup.checkedRadioButtonId != -1) {
                val ingredientstatus:Int = when(checkedId) {
                    R.id.state_ingredient_r_btn1 -> 0
                    R.id.state_ingredient_r_btn2 -> 1
                    R.id.state_ingredient_r_btn3 -> 2
                    else -> -1
                }
                val storagestatus:Int = when(holder.storageGroup.checkedRadioButtonId) {
                    R.id.state_storage_r_btn1 -> 0
                    R.id.state_storage_r_btn2 -> 1
                    R.id.state_storage_r_btn3 -> 2
                    else -> -1
                }
                activity.expiryListSubmit(position,
                    ExpiryDateIngredient(ingredients[position], (5 .. 10).random(), ingredientstatus, storagestatus, false, LocalDate.now()))
            }
        }
        holder.storageGroup.setOnCheckedChangeListener { group, checkedId ->
            if(holder.ingredientGroup.checkedRadioButtonId != -1) {
                val storagestatus:Int = when(checkedId) {
                    R.id.state_storage_r_btn1 -> 0
                    R.id.state_storage_r_btn2 -> 1
                    R.id.state_storage_r_btn3 -> 2
                    else -> -1
                }
                val ingredientstatus:Int = when(holder.ingredientGroup.checkedRadioButtonId) {
                    R.id.state_ingredient_r_btn1 -> 0
                    R.id.state_ingredient_r_btn2 -> 1
                    R.id.state_ingredient_r_btn3 -> 2
                    else -> -1
                }
                activity.expiryListSubmit(position,
                    ExpiryDateIngredient(ingredients[position], (5 .. 10).random(), ingredientstatus, storagestatus, false, LocalDate.now()))
            }
        }
        Log.d("recyclerviewTest", "2 : ${ingredients[position].ingredientName}")
    }
    override fun getItemCount(): Int {
        Log.d("recyclerviewTest", "3 : ${ingredients.size}")
        return ingredients.size
    }
    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var ingredientName: TextView
        internal var ingredientGroup: RadioGroup
        internal var storageGroup: RadioGroup
        init {
            var context = context
        }
        init {
            ingredientName = view.findViewById(R.id.state_ingredient_name)
            ingredientGroup = view.findViewById(R.id.state_ingredient_rg)
            storageGroup = view.findViewById(R.id.state_storage_rg)
        }
    }
}