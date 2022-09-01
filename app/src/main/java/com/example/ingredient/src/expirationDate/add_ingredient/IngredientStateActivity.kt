package com.example.ingredient.src.expirationDate.add_ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityIngredientStateBinding
import com.example.ingredient.databinding.ActivityMainBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient

class IngredientStateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientStateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("extraTest", "1 : ${intent.getParcelableArrayListExtra<Ingredient>("ingredients")}")

    }

}
