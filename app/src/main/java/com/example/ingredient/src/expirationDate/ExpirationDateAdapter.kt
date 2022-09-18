package com.example.ingredient.src.expirationDate

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.src.expirationDate.add_ingredient.ingredientstate.IngredientStateAdapter
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient
import com.google.firebase.firestore.FirebaseFirestore

class ExpirationDateAdapter(
    private val showBtnDelete: (Boolean) -> Unit
):RecyclerView.Adapter<ExpirationDateAdapter.ViewHolder>(){
    private var context: Context? = null
    private var expirationDateIngredient = arrayListOf<ExpiryDateIngredient>()
    private var isEnable = false
    private var itemSelectedList =  mutableListOf<Int>()
    private var colorPurple:Int = 0
    private var colorWhite:Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpirationDateAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_expirydate_ingredient, parent, false)
        context = view.context
        colorPurple = ContextCompat.getColor(context!!, R.color.purple_200)
        colorWhite = ContextCompat.getColor(context!!, R.color.white)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ExpirationDateAdapter.ViewHolder, position: Int) {
        val item = expirationDateIngredient[position]
        holder.name.text = expirationDateIngredient[position].ingredient.ingredientName
        holder.day.text = "D-" + expirationDateIngredient[position].expirydate.toString()

        // 아이템 삭제

        holder.check.visibility = View.GONE
        // Check 모드로 진입. (그냥 첫 아이템 Check를 통해 isEnable을 true로 바꿈)
        holder.itemView.setOnLongClickListener {
            selectItem(holder, item, position)
            true
        }
        holder.itemView.setOnClickListener {
            // Check 표시가 있는 아이템을 클릭
            if(itemSelectedList.contains(position)) {
                itemSelectedList.remove(position)
                holder.check.visibility = View.GONE
                // 아마 의미 없을듯
                item.selected = false
                // 모든 Check를 없앴을 때
                if(itemSelectedList.isEmpty()) {
                    showBtnDelete(false)
                    isEnable = false
                }
            }
            // Check 모드에서 Check 표시 없는 아이템을 클릭.
            else if(isEnable) {
                selectItem(holder, item, position)
            }
        }

        // 이미지 로드
        Glide.with(holder.itemView)
            .load(expirationDateIngredient[position].ingredient.ingredientIcon)
            .into(holder.icon)
    }

    private fun selectItem(holder: ExpirationDateAdapter.ViewHolder, item: ExpiryDateIngredient, position: Int) {
        isEnable = true
        itemSelectedList.add(position)
        item.selected = true
        showBtnDelete(true)
        // Check 이벤트
        holder.check.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return expirationDateIngredient.size
    }
    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var icon: ImageView
        internal var name: TextView
        internal var day: TextView
        internal var check:ImageView
        init {
            icon = view.findViewById(R.id.expirydate_ingredient_icon)
            name = view.findViewById(R.id.expirydate_ingredient_name)
            day = view.findViewById(R.id.expirydate_ingredient_day)
            check = view.findViewById(R.id.expirydate_ingredient_check)
        }
    }
    fun ExpiryDateSubmitList(expirationDateIngredient:ArrayList<ExpiryDateIngredient>) {
        this.expirationDateIngredient = expirationDateIngredient
        notifyDataSetChanged()
    }

    fun deleteSelectedItem(userID:String) {
        // 삭제할 유통기한 리스트(이름)
        var removeList = itemSelectedList.map {
            expirationDateIngredient[it].ingredient.ingredientName
        }
        // 리스트 속 유통기한 재료 삭제
        FirebaseFirestore.getInstance()
        .collection("Refrigerator")
            .document(userID)
            .collection("ingredients")
            .whereIn("ingredientname", removeList)
            .get()
            .addOnSuccessListener {
                it.forEach { document ->
                    document.reference.delete()
                }
            }
        // 체크 리스트 리셋
        if(itemSelectedList.isNotEmpty()) {
            expirationDateIngredient.removeAll { item -> item.selected }
            isEnable = false
            showBtnDelete(false)
            itemSelectedList.clear()
        }
        notifyDataSetChanged()
    }
}