package com.example.ingredient.src.basket.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.databinding.FragmentGroupingredientsBinding
import com.example.ingredient.src.basket.models.BasketIngredient

class GroupIngredientsFragment(basketList: ArrayList<BasketIngredient>): Fragment() {
    var basketList = basketList
    private var _binding : FragmentGroupingredientsBinding? = null
    private val binding get()  = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupingredientsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d("basketTest", "data Test : ${basketList}")
        val adapter = GroupIngredientsAdapter()
        val recyclerview = binding.groupRecyclerView
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.submitList(basketList)

        val Addbnt = binding.groupAddBtn
        Addbnt.setOnClickListener {
            addIngredientBtn()
        }

    }

    fun addIngredientBtn() {
        var temp = arrayListOf<String>()
        basketList.forEach {
            temp.add(it.groupName)
        }

        var intent = Intent(context, GroupAddIngredientsActivity::class.java)
        temp.toSet().toTypedArray()
        intent.putExtra("groupList", temp.toSet().toTypedArray())
        startActivity(intent)
    }
}