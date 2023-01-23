package com.example.ingredient.feature.basket.group.addGroup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.databinding.ActivityGroupAddIngredientsBinding
import com.example.ingredient.feature.basket.BasketService
import com.example.ingredient.feature.basket.BasketView
import com.example.ingredient.feature.basket.group.addGroup.addIngredient.AddGroupIngredientsActivity
import com.example.ingredient.feature.basket.models.BasketIngredient

class GroupAddIngredientsActivity : AppCompatActivity(), BasketView {
    private lateinit var binding: ActivityGroupAddIngredientsBinding
    private var groupList = arrayListOf<String>()
    private var context: Context? = null
    private var adapter = GroupAddIngredientsAdapter()
    private var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupAddIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        groupList = intent.extras!!.getStringArrayList("groupList") as ArrayList<String>

        var recyclerview = binding.addgroupRadioRecyclerview
        adapter = GroupAddIngredientsAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = adapter

        adapter!!.submitList(groupList)

        binding.addgroupFinishBtn.setOnClickListener {
            finish()
        }
        // 재료 추가 (선택 그룹 넘기기)
        binding.addgroupRadioBtn.setOnClickListener {
            if(groupList.isNullOrEmpty()) {
                Toast.makeText(context, "그룹을 추가해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                var intent = Intent(context, AddGroupIngredientsActivity::class.java)
                intent.putExtra("groupName", groupList[adapter.selecCheck()])
                startActivity(intent)
            }
        }

        // 새그룹 추가
        binding.addgroupAddgroupBtn.setOnClickListener {
            binding.addgroupAddgroupBtn.visibility = View.GONE
            binding.addgroupGroupEdittext.visibility = View.VISIBLE
            binding.addgroupGroupsaveBtn.visibility = View.VISIBLE

            IniteditTextFocus()
            showKeyboard()
        }

        // 추가 새그룹 저장
        binding.addgroupGroupsaveBtn.setOnClickListener {
            Log.d("test", "${groupList}")
            if(binding.addgroupGroupEdittext.text.toString().trim().isNullOrBlank()) {
                Toast.makeText(context, "그룹명을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(!groupList.contains(binding.addgroupGroupEdittext.text.toString())) {
                BasketService(this).postBasketGroup(binding.addgroupGroupEdittext.text.toString())
            }
            else {
                Log.d("addgroup", "이미 존재하는 그룹")
                Toast.makeText(context, "이미 존재하는 그룹입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.addgroupGroupEdittext.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.addgroupGroupsaveBtn.performClick()
                handled = true
            }
            handled
        }
    }

    fun showKeyboard() {
        imm?.showSoftInput(binding.addgroupGroupEdittext,0)
    }

    fun IniteditTextFocus() {
        binding.addgroupGroupEdittext.requestFocus()
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
        groupList.clear()
        groupList.addAll(response)
        adapter.submitList(response)
    }

    override fun onGetBasketGroupFailure(message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostBasketGroupSuccess() {
        binding.addgroupGroupEdittext.text.clear()
        BasketService(this).getBasketGroup()
    }

    override fun onPostBasketGroupFailure(message: String) {
        Log.d("BasketGroup", message)
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