package com.example.ingredient.feature.basket.group

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
import com.example.ingredient.feature.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GroupAdapter: RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
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
        holder.ingName.text = DataList[position].ingredientname
        holder.ingCnt.text = DataList[position].ingredientquantity.toString() + category(DataList[position].ingredientcategory, DataList[position].ingredientname)
        holder.ingCategory.text = DataList[position].ingredientcategory

        Glide.with(holder.itemView)
            .load(DataList[position].ingredienticon)
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
                .whereEqualTo("ingredientname", DataList[position].ingredientname)
                .whereEqualTo("groupName", DataList[position].groupName)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        if(number < 1) {
                            document.reference.delete()
                            DataList.map { it.ingredientname }.indexOf(DataList[position].ingredientname).let { it1 ->
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
                .whereEqualTo("ingredientname", DataList[position].ingredientname)
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

    fun submitList(DataList: ArrayList<BasketIngredient>){
        this.DataList = DataList
        notifyDataSetChanged()
    }
}