package com.example.ingredient.src.basket.group.add_ingredient

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
import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BasketGroupAdapter: RecyclerView.Adapter<BasketGroupAdapter.ViewHolder>() {
    private var context: Context? = null
    private lateinit var DataList: ArrayList<BasketIngredient>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_basketgroup, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun getItemCount() = DataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientName.text = DataList[position].ingredientName
        holder.ingredientAmount.text = DataList[position].quantity.toString()
        Glide.with(holder.itemView)
            .load(DataList[position].ingredientIcon)
            .into(holder.ingredientIcon)
        // 마이너스 버튼 클릭
        holder.ingredientMinus.setOnClickListener {
            var number = holder.ingredientAmount.text.toString().toInt()
            holder.ingredientAmount.text = (--number).toString()
            FirebaseFirestore.getInstance()
                .collection("ListData")
                .document(FirebaseAuth.getInstance().uid.toString())
                .collection("Basket")
                .whereEqualTo("ingredientname", DataList[position].ingredientName)
                .whereEqualTo("groupName", DataList[position].groupName)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        if(number < 1) {
                            document.reference.delete()
                            DataList.map { it.ingredientName }.indexOf(DataList[position].ingredientName).let { it1 ->
                                DataList.removeAt(it1)
                            }
                            notifyItemRemoved(position)
                        }
                        else {
                            document.reference.update("ingredientquantity", number)
                        }
                    }
                }

        }
        // 플러스 버튼 클릭
        holder.ingredientPlus.setOnClickListener {
            var number = holder.ingredientAmount.text.toString().toInt()
            holder.ingredientAmount.text = (++number).toString()
            Log.d("numberTest", "number : $number")
            FirebaseFirestore.getInstance()
                .collection("ListData")
                .document(FirebaseAuth.getInstance().uid.toString())
                .collection("Basket")
                .whereEqualTo("ingredientname", DataList[position].ingredientName)
                .whereEqualTo("groupName", DataList[position].groupName)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        document.reference.update("ingredientquantity", number)
                    }
                }
        }
    }

    inner class ViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal var ingredientName : TextView
        internal var ingredientIcon : ImageView
        internal var ingredientAmount : TextView
        internal var ingredientMinus : Button
        internal var ingredientPlus : Button

        init {
            context = context
            ingredientName = view.findViewById(R.id.group_ingredientname)
            ingredientIcon = view.findViewById(R.id.group_ingredienticon)
            ingredientAmount = view.findViewById(R.id.group_ingredientamount)
            ingredientMinus = view.findViewById(R.id.group_ingredient_minus)
            ingredientPlus = view.findViewById(R.id.group_ingredient_plus)
        }
    }
    fun submitList(DataList: ArrayList<BasketIngredient>){
        this.DataList = DataList
        notifyDataSetChanged()
    }
}