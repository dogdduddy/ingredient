package com.example.ingredient.src.expirationDate.add_ingredient.ingredientstate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class IngredientStateAdapter(val ingredients:ArrayList<Ingredient>, val activity:IngredientStateActivity) : RecyclerView.Adapter<IngredientStateAdapter.ViewHolder>() {
    private var context: Context? = null
    private val cal = Calendar.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_add_ingredientstate, parent, false)
        context = view.context
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.registerddate.text = "등록날짜 ${SimpleDateFormat("yyyy.MM.dd").format(Date())}"
        holder.ingredientName.text = ingredients[position].ingredientname
        Glide.with(holder.itemView)
            .load(ingredients[position].ingredienticon)
            .into(holder.ingredientIcon)
        holder.ingredientGroup.setOnCheckedChangeListener { group, checkedId ->
            if(holder.storageGroup.checkedRadioButtonId != -1) {
                val ingredientstatus:Int = when(checkedId) {
                    R.id.state_ing_r1 -> 0
                    R.id.state_ing_r2 -> 1
                    R.id.state_ing_r3 -> 2
                    else -> -1
                }
                val storagestatus:Int = when(holder.storageGroup.checkedRadioButtonId) {
                    R.id.state_sto_r1 -> 0
                    R.id.state_sto_r2 -> 1
                    R.id.state_sto_r3 -> 2
                    else -> -1
                }

                var randomN = (5 .. 10).random()
                activity.expiryListSubmit(
                    position,
                    ExpiryDateIngredient(
                        ingredients[position],
                        randomN,
                        ingredientstatus,
                        storagestatus,
                        false,
                        Date(),
                        cal.apply {add(Calendar.DATE,randomN)}.time)
                )
            }
        }
        holder.storageGroup.setOnCheckedChangeListener { group, checkedId ->
            if(holder.ingredientGroup.checkedRadioButtonId != -1) {
                val storagestatus:Int = when(checkedId) {
                    R.id.state_sto_r1 -> 0
                    R.id.state_sto_r2 -> 1
                    R.id.state_sto_r3 -> 2
                    else -> -1
                }
                val ingredientstatus:Int = when(holder.ingredientGroup.checkedRadioButtonId) {
                    R.id.state_ing_r1 -> 0
                    R.id.state_ing_r2 -> 1
                    R.id.state_ing_r3 -> 2
                    else -> -1
                }
                var randomN = (5 .. 10).random()
                activity.expiryListSubmit(
                    position,
                    ExpiryDateIngredient(
                        ingredients[position],
                        randomN,
                        ingredientstatus,
                        storagestatus,
                        false,
                        Date(),
                        cal.apply {add(Calendar.DATE,randomN)}.time)
                )
            }
        }
    }
    override fun getItemCount(): Int {
        return ingredients.size
    }
    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var ingredientName: TextView
        internal var ingredientGroup: RadioGroup
        internal var storageGroup: RadioGroup
        internal var ingredientIcon: ImageView
        internal var registerddate : TextView
        init {
            var context = context
        }
        init {
            ingredientName = view.findViewById(R.id.state_picking_name)
            ingredientGroup = view.findViewById(R.id.state_ing_rg)
            storageGroup = view.findViewById(R.id.state_sto_rg)
            ingredientIcon = view.findViewById(R.id.state_picking_icon)
            registerddate = view.findViewById(R.id.state_picking_date)
        }
    }
}