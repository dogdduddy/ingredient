package com.example.ingredient

import android.content.Context
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
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.commit
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

        // Fragmnet 구현 방식의 차이
        // 현재는 Fragment 클래스를 변수에 저장 / 아래 방식은 띄워질 때 호출하는 방식
        val transection = supportFragmentManager
        transection.beginTransaction().
            add(R.id.fragment_container,Search(),"Search").commit()

        //replaceFragment(searchFragment) // 시작화면은 search 화면으로
        binding.menuBottom.setOnItemSelectedListener { id ->
            when (id) {
                /*
                // Navigation : 각 아이템 클릭시에 객체가 생성되도록 구현 => State 휘발
                R.id.search ->
                    transection.beginTransaction().replace(R.id.fragment_container, Search()).
                    commitAllowingStateLoss()
                R.id.expiration_date ->
                    transection.beginTransaction().replace(R.id.fragment_container, ExpirationDate()).
                            commitAllowingStateLoss()
                R.id.tips ->
                    transection.beginTransaction().replace(R.id.fragment_container, Tips()).
                    commitAllowingStateLoss()
                R.id.food_book ->
                    transection.beginTransaction().replace(R.id.fragment_container, FoodBook()).
                    commitAllowingStateLoss()
                 */
                // Navigation : 프래그먼트 객체를 변수에 저장하고, 필요시 호출 => State 유지
                R.id.search -> replaceFragment(searchFragment)
                R.id.expiration_date -> replaceFragment(expirationdateFragment)
                R.id.tips -> replaceFragment(tipsFragment)
                R.id.food_book -> replaceFragment(foodbookFragment)

            }
        }
    }
//  Navigation : 프래그먼트 객체를 변수에 저장하고, 필요시 호출 => State 유지
    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transection = supportFragmentManager.beginTransaction()
            transection.replace(R.id.fragment_container, fragment)
            transection.commit()
        }
    }
}