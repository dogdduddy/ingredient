package com.example.ingredient

import android.app.Activity
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.ingredient.fragment.ExpirationDate
import com.example.ingredient.fragment.FoodBook
import com.example.ingredient.fragment.Search
import com.example.ingredient.fragment.Note
import com.example.ingredient.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val searchFragment = Search()
    private val expirationdateFragment = ExpirationDate()
    private val tipsFragment = Note()
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        var focusView = currentFocus
        if(focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            var x = ev!!.x.toInt()
            var y = ev!!.y.toInt()
            if (!rect.contains(x, y)) {
                var imm:InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}