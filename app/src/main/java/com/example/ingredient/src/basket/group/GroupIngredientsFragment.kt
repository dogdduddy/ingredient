package com.example.ingredient.src.basket.group

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.databinding.FragmentGroupingredientsBinding
import com.example.ingredient.src.basket.models.BasketIngredient
import com.example.ingredient.src.search.MainFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class GroupIngredientsFragment: Fragment(), GroupIngredientsAdapter.onGroupDrawClickListener {
    private var basketData: ArrayList<BasketIngredient>? = null
    private var basketList = arrayListOf<String>()
    private var _binding : FragmentGroupingredientsBinding? = null
    private val binding get()  = _binding!!
    private val adapter = GroupIngredientsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupingredientsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        val recyclerview = binding.groupInnerRecycler
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        InitData()

        // 그룹 및 재료 추가
        binding.groupInnerBtnOrigin.setOnClickListener {
            addIngredientBtn()
        }
        binding.groupInnerBtnOther.setOnClickListener {
            addIngredientBtn()
        }
    }
    
    fun InitData() {
        basketList.clear()
        FirebaseFirestore.getInstance()
            .collection("ListData")
            .document(FirebaseAuth.getInstance().uid!!)
            .collection("BasketList")
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    basketList.add(document.data["groupName"].toString())
                }
                adapter.submitList(basketData!!, basketList)
            }
    }

    fun addIngredientBtn() {
        var intent = Intent(context, GroupAddIngredientsActivity::class.java)
        intent.putStringArrayListExtra("groupList", basketList)
        startActivity(intent)
    }

    fun submitList(basketData: ArrayList<BasketIngredient>) {
        this.basketData = basketData
        adapter.submitList(basketData, basketList)
    }

    override fun onGroupDrawOpen() {
        binding.groupInnerBtnOther.visibility = View.VISIBLE
        binding.groupInnerBtnOrigin.visibility = View.GONE
    }

    override fun onGroupDrawClose() {
        CoroutineScope(Dispatchers.Main).launch {
            launch {  binding.groupInnerRecycler.requestLayout() }.join()
            launch {if(binding.groupInnerRecycler.height != 1636) {
                binding.groupInnerBtnOther.visibility = View.GONE
                binding.groupInnerBtnOrigin.visibility = View.VISIBLE
            }}
        }

    }
}

