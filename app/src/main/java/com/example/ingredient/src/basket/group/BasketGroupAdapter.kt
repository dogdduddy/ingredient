package com.example.ingredient.src.basket.group

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
        holder.ingName.text = DataList[position].ingredientName
        holder.ingCnt.text = DataList[position].quantity.toString() + category(DataList[position].categoryName, DataList[position].ingredientName)
        holder.ingCategory.text = DataList[position].categoryName

        Glide.with(holder.itemView)
            .load(DataList[position].ingredientIcon)
            .into(holder.ingIcon)
        // 마이너스 버튼 클릭
        holder.ingMinus.setOnClickListener {
            var cnt = holder.ingCnt.text.toString()
            var number = cnt.replace("[^0-9]".toRegex(), "").toInt() - 1
            holder.ingCnt.text = number.toString() + cnt.replace("[0-9]".toRegex(), "")

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
        holder.ingPlus.setOnClickListener {

            var cnt = holder.ingCnt.text.toString()
            var number = cnt.replace("[^0-9]".toRegex(), "").toInt() + 1
            holder.ingCnt.text = number.toString() + cnt.replace("[0-9]".toRegex(), "")

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
        internal var ingName : TextView
        internal var ingIcon : ImageView
        internal var ingCnt : TextView
        internal var ingMinus : Button
        internal var ingPlus : Button
        internal var ingCategory : TextView

        init {
            context = context
            ingName = view.findViewById(R.id.group_inner_ing_name)
            ingIcon = view.findViewById(R.id.group_inner_ing_icon)
            ingCnt = view.findViewById(R.id.group_inner_ing_cnt)
            ingMinus = view.findViewById(R.id.group_inner_ing_minus)
            ingPlus = view.findViewById(R.id.group_inner_ing_plus)
            ingCategory = view.findViewById(R.id.group_inner_ing_category)
        }
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

    fun submitList(DataList: ArrayList<BasketIngredient>){
        this.DataList = DataList
        notifyDataSetChanged()
    }
}