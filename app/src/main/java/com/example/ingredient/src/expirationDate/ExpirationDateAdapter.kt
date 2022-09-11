package com.example.ingredient.src.expirationDate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.src.expirationDate.add_ingredient.ingredientstate.IngredientStateAdapter
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient

class ExpirationDateAdapter():RecyclerView.Adapter<ExpirationDateAdapter.ViewHolder>(){
    private var context: Context? = null
    private var expirationDateIngredient = arrayListOf<ExpiryDateIngredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpirationDateAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_expirydate_ingredient, parent, false)
        context = view.context
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ExpirationDateAdapter.ViewHolder, position: Int) {
        holder.name.text = expirationDateIngredient[position].ingredient.ingredientName
        holder.day.text = "D-" + expirationDateIngredient[position].expirydate.toString()
        holder.itemView.setOnLongClickListener {
            Toast.makeText(this.context, "${holder.name.text}",Toast.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }

        Glide.with(holder.itemView)
            .load(expirationDateIngredient[position].ingredient.ingredientIcon)
            .into(holder.icon)
    }
    override fun getItemCount(): Int {
        return expirationDateIngredient.size
    }
    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var icon: ImageView
        internal var name: TextView
        internal var day: TextView
        init {
            icon = view.findViewById(R.id.expirydate_ingredient_icon)
            name = view.findViewById(R.id.expirydate_ingredient_name)
            day = view.findViewById(R.id.expirydate_ingredient_day)
        }
    }
    fun ExpiryDateSubmitList(expirationDateIngredient:ArrayList<ExpiryDateIngredient>) {
        this.expirationDateIngredient = expirationDateIngredient
        notifyDataSetChanged()
    }
}