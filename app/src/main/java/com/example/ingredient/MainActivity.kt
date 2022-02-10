package com.example.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.ingredient.fragment.ExpirationDate
import com.example.ingredient.fragment.FoodBook
import com.example.ingredient.fragment.Search
import com.example.ingredient.fragment.Tips
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val searchFragment = Search()
    private val expirationdateFragment = ExpirationDate()
    private val tipsFragment = Tips()
    private val foodbookFragment = FoodBook()


    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        replaceFragment(searchFragment) // 시작화면은 search 화면으로
        binding.menuBottom.setOnItemSelectedListener { id ->
            when (id) {
                R.id.search -> replaceFragment(searchFragment)
                R.id.expiration_date -> replaceFragment(expirationdateFragment)
                R.id.tips -> replaceFragment(tipsFragment)
                R.id.food_book -> replaceFragment(foodbookFragment)
            }

        }


    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transection = supportFragmentManager.beginTransaction()
            transection.replace(R.id.fragment_container, fragment)
            transection.commit()
        }
    }
}