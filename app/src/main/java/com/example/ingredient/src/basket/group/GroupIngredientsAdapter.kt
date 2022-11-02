package com.example.ingredient.src.basket.group

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.src.basket.models.BasketIngredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GroupIngredientsAdapter(mCallback:onGroupDrawClickListener) : RecyclerView.Adapter<GroupIngredientsAdapter.ViewHolder>() {
    private var context: Context? = null
    private var basketData: ArrayList<BasketIngredient> = ArrayList()
    private var basketList = arrayListOf<String>()
    private var click = true
    private val mCallback = mCallback
    override fun getItemCount(): Int = basketList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_ingredient, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.groupName.text = basketList[position]
        (holder.recyclerView.adapter as BasketGroupAdapter).apply {
            submitList(basketData.filter { it.groupName == basketList[position] } as ArrayList<BasketIngredient>)
        }
        holder.removeBtn.setOnClickListener {
            (holder.recyclerView.adapter as BasketGroupAdapter).apply {
                groupRemove(basketList[position])
            }
        }
        holder.drawBtn.setOnClickListener {
            if(basketData.map { it.groupName }.contains(basketList[position]) && click) {
                holder.recyclerView.visibility = View.VISIBLE
                holder.groupLayout.elevation = 8f
                holder.drawBtn.rotation = 0f
                mCallback.onGroupDrawOpen()
            }
            else if(!click) {
                holder.recyclerView.visibility = View.GONE
                holder.groupLayout.elevation = 0f
                holder.drawBtn.rotation = 180f
                mCallback.onGroupDrawClose()
            }
            click = !click
        }

    }
    inner class ViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal var groupName : TextView
        internal var recyclerView : RecyclerView = view.findViewById(R.id.group_recyclerview)
        internal var drawBtn : ImageButton
        internal var removeBtn : ImageButton
        internal var groupLayout : FrameLayout

        init {
            context = context
            groupName = view.findViewById(R.id.group_groupname)
            recyclerView.apply {
                adapter = BasketGroupAdapter()
                layoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            }
            drawBtn = view.findViewById(R.id.group_drawBtn)
            removeBtn = view.findViewById(R.id.group_removeBtn)
            groupLayout = view.findViewById(R.id.group_layout)
        }
    }

    fun submitList(basketData: ArrayList<BasketIngredient>, basketList: ArrayList<String>) {
        this.basketData = basketData
        this.basketList = basketList
        Log.d("categoryTest", "GroupIngredientsAdapter : ${basketData[0].categoryName}  /  ${basketData[0].ingredientName}")
        notifyDataSetChanged()
    }

    fun groupRemove(groupName:String) {
        // 해당 그룹 재료 삭제
        FirebaseFirestore.getInstance()
            .collection("ListData")
            .document(FirebaseAuth.getInstance().uid.toString())
            .collection("Basket")
            .whereEqualTo("groupName", groupName)
            .get()
            .addOnSuccessListener {
                it.forEach { document ->
                    document.reference.delete()
                }
            }
        FirebaseFirestore.getInstance()
            .collection("ListData")
            .document(FirebaseAuth.getInstance().uid.toString())
            .collection("BasketList")
            .whereEqualTo("groupName", groupName)
            .get()
            .addOnSuccessListener {
                it.forEach { document ->
                    document.reference.delete()
                }
            }
        basketData.filter { it.groupName == groupName }.let { it1 ->
            basketData.removeAll(it1)
        }
        basketList.remove(groupName)
        notifyDataSetChanged()
    }

    interface onGroupDrawClickListener {
        fun onGroupDrawOpen()
        fun onGroupDrawClose()
    }
}