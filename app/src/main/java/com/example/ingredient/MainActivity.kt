package com.example.ingredient

import android.app.Activity
import android.app.Application
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.ingredient.database.ExpirationDateDatabase
import com.example.ingredient.fragment.ExpirationDate
import com.example.ingredient.fragment.FoodBook
import com.example.ingredient.fragment.Search
import com.example.ingredient.fragment.Note
import com.example.ingredient.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Room DB 생성 및 프래그먼트 초기화
    private lateinit var searchFragment:Search
    private lateinit var foodbookFragment:FoodBook
    private lateinit var expirationdateFragment:ExpirationDate
    private lateinit var noteFragment:Note

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = ExpirationDateDatabase.getInstance(applicationContext)
        searchFragment = Search.newInstance(db!!)
        expirationdateFragment = ExpirationDate.newInstance(db!!)
        noteFragment = Note.newInstance(db!!)
        foodbookFragment = FoodBook.newInstance(db!!)

        // 초기 화면을 Search 프래그먼트로 설정
        val transection = supportFragmentManager
        transection.beginTransaction().
            add(R.id.fragment_container,Search(),"Search").commit()

        // 하단바를 통해 화면(프래그먼트) 전환
        binding.menuBottom.setOnItemSelectedListener { id ->
            when (id) {
                // Navigation : 프래그먼트 객체를 변수에 저장하고, 필요시 호출 => State 유지
                R.id.search -> replaceFragment(searchFragment)
                R.id.expiration_date -> replaceFragment(expirationdateFragment)
                R.id.tips -> replaceFragment(noteFragment)
                R.id.food_book -> replaceFragment(foodbookFragment)

            }
        }
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
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}