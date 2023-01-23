package com.example.ingredient.feature.basket.group

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.feature.basket.BasketService
import com.example.ingredient.feature.basket.BasketView
import com.example.ingredient.feature.basket.models.BasketIngredient

class GroupIngredientsAdapter(mCallback:onGroupDrawClickListener) : RecyclerView.Adapter<GroupIngredientsAdapter.ViewHolder>(), BasketView {
    private var context: Context? = null
    private var basketData: ArrayList<BasketIngredient> = ArrayList()
    private var basketList = arrayListOf<String>()
    private var basketDataList = ArrayList<ArrayList<Any>>()
    private var click = true
    private val mCallback = mCallback
    private lateinit var listener : BasketView
    private var fa: Fragment? = null
    override fun getItemCount(): Int = basketList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_ingredient, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.groupName.text = basketList[position]
        (holder.recyclerView.adapter as GroupAdapter).apply {
            submitList(basketData.filter { it.groupName == basketList[position] } as ArrayList<BasketIngredient>)
        }
        holder.removeBtn.setOnClickListener {
            (holder.recyclerView.adapter as GroupAdapter).apply {
                groupRemove(position)
            }
        }
        holder.drawBtn.setOnClickListener {
            if((basketDataList[position][1] as ArrayList<Any>).size >  0) {
                if (click) {
                    holder.recyclerView.visibility = View.VISIBLE
                    holder.groupLayout.elevation = 8f
                    holder.drawBtn.rotation = 0f
                    mCallback.onGroupDrawOpen()
                } else {
                    holder.recyclerView.visibility = View.GONE
                    holder.groupLayout.elevation = 0f
                    holder.drawBtn.rotation = 180f
                    mCallback.onGroupDrawClose()
                }
                click = !click
            }
            else {
                Toast.makeText(context, "재료가 없습니다.", Toast.LENGTH_SHORT).show()
            }
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
                adapter = GroupAdapter()
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
        dataGrouping()
        notifyDataSetChanged()
    }

    fun submitListener(listener: BasketView) {
        this.listener = listener
    }

    fun dataGrouping() {
        basketDataList.clear()
        basketList.forEach { groupName ->
            basketDataList.add(
                arrayListOf(groupName, basketData.filter { it.groupName == groupName } as ArrayList<Any>)
            )
        }
    }

    fun groupRemove(position:Int) {
        // 해당 그룹 재료 삭제
        BasketService(this).deleteBasketGroup(basketList[position], position)
    }

    interface onGroupDrawClickListener {
        fun onGroupDrawOpen()
        fun onGroupDrawClose()
    }

    override fun onGetBasketSuccess(response: ArrayList<BasketIngredient>) {
        TODO("Not yet implemented")
    }

    override fun onGetBasketFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun itemDeleteListener() {
        TODO("Not yet implemented")
    }

    override fun onGetBasketGroupSuccess(response: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun onGetBasketGroupFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostBasketGroupSuccess() {
        TODO("Not yet implemented")
    }

    override fun onPostBasketGroupFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupListSuccess(groupName: String) {
        listener.itemDeleteListener()
    }

    override fun onDeleteBasketGroupIngredientSuccess(groupName : String, position: Int) {
        basketData.filter { it.groupName == groupName }.let { it1 ->
            basketData.removeAll(it1)
        }
        basketList.remove(groupName)
        notifyItemRemoved(position)
    }

    override fun onDeleteBasketGroupListFailure(message: String) {
        Log.d("Basket", "그룹 리스트 삭제 실패")
    }

    override fun onDeleteBasketGroupIngredientFailure(message: String) {
        Log.d("Basket", "그룹 재료 삭제 실패")
    }
}