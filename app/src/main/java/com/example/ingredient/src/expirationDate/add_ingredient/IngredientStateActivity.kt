package com.example.ingredient.src.expirationDate.add_ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityIngredientStateBinding
import com.example.ingredient.databinding.ActivityMainBinding

class IngredientStateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientStateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding

    }
}