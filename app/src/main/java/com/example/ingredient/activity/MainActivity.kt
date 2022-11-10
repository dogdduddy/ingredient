package com.example.ingredient.activity

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.example.ingredient.src.expirationDate.ExpirationDateFragment
import com.example.ingredient.src.basket.BasketFragment
import com.example.ingredient.databinding.ActivityMainBinding
import com.example.ingredient.src.foodbook.FoodBookMainFragment
import com.example.ingredient.src.search.SearchMainFragment
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback

class MainActivity : AppCompatActivity() {
    private lateinit var mainFragment: SearchMainFragment
    private lateinit var foodbookFragment: FoodBookMainFragment
    private lateinit var expirationdateFragment: ExpirationDateFragment
    private lateinit var basketFragment: BasketFragment
    private lateinit var database: FirebaseFirestore

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private var nav_adapter = RecentRecipeAdapter()
    private var recent_recipe_list = ArrayList<MutableMap<String, String>>()
    private var transection : FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()
        InitFragment()
        toolBarInit()

        if(intent.hasExtra("fragment")) {
            when(intent.getStringExtra("fragment")) {
                "main" -> setMainFragment()
                "expiry" -> {
                    setExpirationDateFragment()
                    binding.menuBottom.setItemSelected(R.id.expiration_date, true)
                }
                "basket" -> {
                    setBasketFragment()
                    binding.menuBottom.setItemSelected(R.id.basket, true)
                }
                "foodbook" -> setFoodBookFragment()
            }
        }

        // 하단바를 통해 화면(프래그먼트) 전환
        binding.menuBottom.setOnItemSelectedListener { id ->
            when (id) {
                // Navigation : 프래그먼트 객체를 변수에 저장하고, 필요시 호출 => State 유지
                R.id.search -> {
                    setMainFragment()
                }
                R.id.expiration_date -> {
                    setExpirationDateFragment()
                }
                R.id.basket -> {
                    setBasketFragment()
                }
                R.id.food_book -> {
                    setFoodBookFragment()
                }
            }
        }
        var navi_header = binding.navigationView.getHeaderView(0)
        navi_header.findViewById<ImageView>(R.id.nav_setting).setOnClickListener {
            Toast.makeText(this,"설정", Toast.LENGTH_SHORT).show()
        }
        navi_header.findViewById<Button>(R.id.nav_logout).setOnClickListener { logout() }
        if(!intent.extras?.get("user").toString().isNullOrBlank()) {
            navi_header.findViewById<TextView>(R.id.nav_profile_nickname).text = intent.extras?.get("user").toString()
        }
        if(!intent.extras?.get("email").toString().isNullOrBlank()) {
            navi_header.findViewById<TextView>(R.id.nav_profile_email).text = intent.extras?.get("email").toString()
        }

        // 이미지 로드
        if(!intent.extras?.get("photo").toString().isNullOrBlank()) {
            Glide.with(this).load(intent.extras?.get("photo")).into(navi_header.findViewById<ImageView>(R.id.nav_profile_image)).onLoadFailed(ResourcesCompat.getDrawable(resources,R.drawable.profile_defalut_1, null))
        }else {
            navi_header.findViewById<ImageView>(R.id.nav_profile_image).setImageResource(R.drawable.profile_defalut_1)
        }

        // 최근 본 레시피
        var recent_recyclerview = navi_header.findViewById<RecyclerView>(R.id.nav_recent_recipe)
        recent_recyclerview.layoutManager = GridLayoutManager(this, 3)
        recent_recyclerview.adapter = nav_adapter
    }

    fun addRecentRecipe(name:String, icon:String) {
        Log.d("navSubmitList", recent_recipe_list.toString())
        recent_recipe_list.map { it["name"] }.forEach {
            if(it == name) {
                return
            }
        }
        recent_recipe_list.add(0, mutableMapOf("name" to name, "image" to icon))
        if(recent_recipe_list.size > 6) {
            recent_recipe_list.removeLast()
        }
        navSubmitList()
    }

    fun navSubmitList() {
        Log.d("navSubmitList", recent_recipe_list.toString())
        nav_adapter.submitList(recent_recipe_list)
    }

    fun setMainFragment(){
        binding.toolbarTitle.visibility = View.GONE
        binding.mainAchaLogo.visibility = View.VISIBLE
        replaceFragment(mainFragment)
    }
    fun setExpirationDateFragment(){
        binding.mainAchaLogo.visibility = View.GONE
        binding.toolbarTitle.text = "유통기한"
        binding.toolbarTitle.visibility = View.VISIBLE
        replaceFragment(expirationdateFragment)
    }
    fun setBasketFragment(){
        binding.mainAchaLogo.visibility = View.GONE
        binding.toolbarTitle.text = "장바구니"
        binding.toolbarTitle.visibility = View.VISIBLE
        replaceFragment(basketFragment)
    }
    fun setFoodBookFragment(){
        binding.mainAchaLogo.visibility = View.GONE
        binding.toolbarTitle.text = "레시피사전"
        binding.toolbarTitle.visibility = View.VISIBLE
        replaceFragment(foodbookFragment)
    }

    fun InitFragment() {
        mainFragment = SearchMainFragment.newInstance()
        expirationdateFragment = ExpirationDateFragment.newInstance()
        basketFragment = BasketFragment.newInstance()
        foodbookFragment = FoodBookMainFragment.newInstance()

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
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else if (fragmentList.size > 1) {
            fragmentList.forEach {
                if (it is onBackPressListener) {
                    (it as onBackPressListener).onBackPressed()
                    return
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    interface onBackPressListener {
        fun onBackPressed()
    }

    fun toolBarInit() {
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

        drawerLayout = binding.drawerlayout
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun logout() {
        LoginManager.getInstance().logOut()
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                FirebaseAuth.getInstance().signOut()
            }
        })
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}