package com.example.ingredient.activity

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ingredient.R
import com.example.ingredient.src.expirationDate.ExpirationDate
import com.example.ingredient.src.FoodBook
import com.example.ingredient.src.search.Search
import com.example.ingredient.src.basket.Basket
import com.example.ingredient.databinding.ActivityMainBinding
import com.example.ingredient.src.search.MainFragment
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var mainFragment: MainFragment
    private lateinit var foodbookFragment:FoodBook
    private lateinit var expirationdateFragment: ExpirationDate
    private lateinit var basketFragment: Basket
    private lateinit var database: FirebaseFirestore
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var transection : FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()
        InitFragment()

        // 하단바를 통해 화면(프래그먼트) 전환
        binding.menuBottom.setOnItemSelectedListener { id ->
            when (id) {
                // Navigation : 프래그먼트 객체를 변수에 저장하고, 필요시 호출 => State 유지
                R.id.search -> replaceFragment(mainFragment)
                R.id.expiration_date -> replaceFragment(expirationdateFragment)
                R.id.tips -> replaceFragment(basketFragment)
                R.id.food_book -> replaceFragment(foodbookFragment)
            }
        }
    }
    fun InitFragment() {
        mainFragment = MainFragment.newInstance()
        expirationdateFragment = ExpirationDate.newInstance()
        basketFragment = Basket.newInstance()
        foodbookFragment = FoodBook.newInstance()

        // 초기 화면을 Search 프래그먼트로 설정
        transection.beginTransaction().
        add(R.id.fragment_container, mainFragment,"Search").commit()
        binding.menuBottom.setItemSelected(R.id.search)
    }
    // 프래그먼트 전환 메서드. State는 프래그먼트를 객체로 갖고 있기에, 뷰 단에서 저장과 복구 진행함.
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
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        var fragmentList = supportFragmentManager.fragments
        fragmentList.forEach {
            if(it is onBackPressListener){
                (it as onBackPressListener).onBackPressed()
                return
            }
        }
        super.onBackPressed()
    }
    interface onBackPressListener {
        fun onBackPressed()
    }
}