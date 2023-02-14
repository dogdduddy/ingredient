package com.dogdduddy.ingredient.src.basket.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dogdduddy.ingredient.databinding.FragmentGroupingredientsBinding
import com.dogdduddy.ingredient.src.basket.BasketService
import com.dogdduddy.ingredient.src.basket.BasketView
import com.dogdduddy.ingredient.src.basket.group.addGroup.GroupAddIngredientsActivity
import com.dogdduddy.ingredient.src.basket.models.BasketIngredient
import kotlinx.coroutines.*

class GroupIngredientsFragment: Fragment(), GroupIngredientsAdapter.onGroupDrawClickListener, BasketView {
    private var basketData: ArrayList<BasketIngredient> = arrayListOf()
    private var basketList = arrayListOf<String>()
    private var _binding : FragmentGroupingredientsBinding? = null
    private val binding get()  = _binding!!
    private val adapter = GroupIngredientsAdapter(this)
    private var imm: InputMethodManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupingredientsBinding.inflate(layoutInflater, container, false)
        imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        val recyclerview = binding.groupInnerRecycler
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        BasketService(this@GroupIngredientsFragment).getBasketGroup()

        // 그룹 및 재료 추가
        binding.groupInnerBtnOrigin.setOnClickListener {
            addIngredientBtn()
        }
        binding.groupInnerBtnOther.setOnClickListener {
            addIngredientBtn()
        }
    }
    fun addIngredientBtn() {
        var intent = Intent(context, GroupAddIngredientsActivity::class.java)
        intent.putStringArrayListExtra("groupList", basketList)
        startActivity(intent)
    }

    fun submitList(basketData: ArrayList<BasketIngredient>, listener: BasketView) {
        this.basketData = basketData
        adapter.submitList(basketData, basketList)
        adapter.submitListener(listener)
    }

    override fun onGroupDrawOpen() {
        binding.groupInnerBtnOther.visibility = View.VISIBLE
        binding.groupInnerBtnOrigin.visibility = View.GONE
    }

    override fun onGroupDrawClose() {
        CoroutineScope(Dispatchers.Main).launch {
            launch {  binding.groupInnerRecycler.requestLayout() }.join()
            launch {if(binding.groupInnerRecycler.height != binding.groupInnerBtnOther.height) {
                binding.groupInnerBtnOther.visibility = View.GONE
                binding.groupInnerBtnOrigin.visibility = View.VISIBLE
            }}
        }

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
        basketList = response
        Log.d("basketTest", "response : $response")
        Log.d("basketTest", "basketData : $basketData")
        adapter.submitList(basketData, response)
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

    override fun onDeleteBasketGroupListSuccess(response: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupIngredientSuccess(response: String, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupListFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleteBasketGroupIngredientFailure(message: String) {
        TODO("Not yet implemented")
    }
}

