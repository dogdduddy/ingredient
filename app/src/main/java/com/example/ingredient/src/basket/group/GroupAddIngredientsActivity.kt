package com.example.ingredient.src.basket.group

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityAddingredientsBinding
import com.example.ingredient.databinding.ActivityGroupAddIngredientsBinding
import com.example.ingredient.src.basket.group.add_ingredient.AddGroupIngredientsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GroupAddIngredientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupAddIngredientsBinding
    private var groupList = arrayListOf<String>()
    private var context: Context? = null
    private var adapter =GroupAddIngredientsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupAddIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

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
            var intent = Intent(context, AddGroupIngredientsActivity::class.java)
            intent.putExtra("groupName", groupList[adapter.selecCheck().indexOf(1)])
            startActivity(intent)
        }

        // 새그룹 추가
        binding.addgroupAddgroupBtn.setOnClickListener {
            binding.addgroupAddgroupBtn.visibility = View.GONE
            binding.addgroupGroupEdittext.visibility = View.VISIBLE
            binding.addgroupGroupsaveBtn.visibility = View.VISIBLE
        }

        // 추가 새그룹 저장
        binding.addgroupGroupsaveBtn.setOnClickListener {
            if(binding.addgroupGroupEdittext.text.toString().trim().isNullOrBlank()) {
                Toast.makeText(context, "그룹명을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(!groupList.contains(binding.addgroupGroupEdittext.text.toString())) {
                // 그룹 확인
                Log.d("addgroup", "생성 중")
                FirebaseFirestore.getInstance()
                    .collection("ListData")
                    .document(FirebaseAuth.getInstance().uid.toString())
                    .collection("BasketList")
                    .document()
                    .set(hashMapOf("groupName" to binding.addgroupGroupEdittext.text.toString()))
                    .addOnSuccessListener {
                        Log.d("addgroup", "생성 성공")
                        binding.addgroupGroupEdittext.setText("")
                        InitData()
                    }
            }
            else {
                Log.d("addgroup", "이미 존재하는 그룹")
                Toast.makeText(context, "이미 존재하는 그룹입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun InitData() {
        Log.d("addgroup", "Init Data")
        groupList.clear()
        FirebaseFirestore.getInstance()
            .collection("ListData")
            .document(FirebaseAuth.getInstance().uid!!)
            .collection("BasketList")
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    groupList.add(document.data["groupName"].toString())
                }
                adapter.submitList(groupList)
            }
    }
}