package com.example.ingredient.src.basket.group

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityAddingredientsBinding
import com.example.ingredient.databinding.ActivityGroupAddIngredientsBinding

class GroupAddIngredientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupAddIngredientsBinding
    private var data = arrayOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupAddIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.extras!!.get("groupList") as Array<String>


        var recyclerview = binding.addgroupRadioRecyclerview
        var adapter = GroupAddIngredientsAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = adapter

        adapter.submitList(data)


        // 라디오 버튼 추가하기 + 블로그 확인하고 Check 어떻ㄱ ㅔ바꿀지 연구
        // https://velog.io/@zzangdd/RecyclerView%EC%97%90%EC%84%9C-RadioButton-%EB%8B%A8%EC%9D%BC%EC%84%A0%ED%83%9D-%ED%95%98%EA%B8%B0

    }
}