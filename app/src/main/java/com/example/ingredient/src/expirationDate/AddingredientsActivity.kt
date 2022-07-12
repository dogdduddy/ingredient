package com.example.ingredient.src.expirationDate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityAddingredientsBinding
import com.example.ingredient.databinding.ActivityMainBinding

class AddingredientsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddingredientsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddingredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}