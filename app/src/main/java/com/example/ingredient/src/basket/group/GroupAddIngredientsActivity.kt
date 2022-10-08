package com.example.ingredient.src.basket.group

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityAddingredientsBinding
import com.example.ingredient.databinding.ActivityGroupAddIngredientsBinding
import com.example.ingredient.src.basket.group.add_ingredient.AddGroupIngredientsActivity

class GroupAddIngredientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupAddIngredientsBinding
    private var data = arrayOf<String>()
    private var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupAddIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        data = intent.extras!!.get("groupList") as Array<String>

        var recyclerview = binding.addgroupRadioRecyclerview
        var adapter = GroupAddIngredientsAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = adapter

        adapter.submitList(data)
        binding.addgroupRadioBtn.setOnClickListener {
            Log.d("Check", "${adapter.selecCheck()}")
            Log.d("Check", "${data[adapter.selecCheck().indexOf(1)]}")
            var intent = Intent(context, AddGroupIngredientsActivity::class.java)
            startActivity(intent)
        }
    }
}