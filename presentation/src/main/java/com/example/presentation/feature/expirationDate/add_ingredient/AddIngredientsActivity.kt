package com.example.presentation.feature.expirationDate.add_ingredient

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.databinding.ActivityAddingredientsBinding
import com.example.ingredient.feature.expirationDate.add_ingredient.ingredientstate.IngredientStateActivity
import com.example.ingredient.feature.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.feature.expirationDate.add_ingredient.models.Ingredient
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator

class AddIngredientsActivity : AppCompatActivity(), AddIngredientView {
    private lateinit var binding:ActivityAddingredientsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var ingredientViewPagerAdapter:AddIngredientViewPagerAdapter
    private var pickingredients = mutableListOf<Ingredient>()
    private var ingredients = ArrayList<CategoryIngrediets>()
    private var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddingredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        // Categoty 및 재료 리스트 초기화
        IngredientService(this).GetCategoryIngrediets()

        // 검색 기능 구현 중
        // 타자기 검색 버튼으로 검색 버튼 클릭 효과
        binding.ingSearchbar.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.ingSearchbar.performClick()
                handled = true
            }
            handled
        }
        // 검색 버튼
        binding.ingSearchBtn.setOnClickListener {
            var input = binding.ingSearchbar.text.toString()
            if(input.isNullOrBlank())
                ViewPagerInit(ingredients)
            else
                IngredientService(this).GetSearchCategoryIngrediets(input, ingredients)
            binding.ingSearchbar.setText("")
            // 검색 키워드 string를 firebase로 넘겨서 검색을 진행하는 코드 삽입 or 실행하는 클래스로 넘기기
        }

        // 선택 재료 넘기기 버튼
        binding.pickingredientsave.setOnClickListener {
            if(pickingredients.isNotEmpty()) {
                intent = Intent(this, IngredientStateActivity::class.java)
                intent.putParcelableArrayListExtra(
                    "ingredients",
                    pickingredients as ArrayList<Ingredient>
                )
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.ingSearchbar.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.ingSearchbar.performClick()
                handled = true
            }
            handled
        }
    }

    fun addingredientClick(ingredient:Ingredient) {
        if(!pickingredients.contains(ingredient)) {
            pickingredients.add(ingredient)
            // 추가된 재료 리사이클러뷰에 추가 후 notification  =>  submitlist
            binding.pickingredientChip.addView(Chip(this).apply {
                text = ingredient.ingredientname
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    var ingredientNum:Int = 0
                    run { pickingredients.forEachIndexed {
                            i, v -> if(v.ingredientname == this.text) {
                                ingredientNum = i
                                return@run
                            }
                    }}
                    pickingredients.removeAt(ingredientNum)
                    binding.pickingredientChip.removeView(this)
                }
            }, 0)
        }
    }
    fun ViewPagerInit(response:ArrayList<CategoryIngrediets>) {
        viewPager = binding.viewpagerAddIngredient
        ingredientViewPagerAdapter = AddIngredientViewPagerAdapter(this, this)
        viewPager.adapter = ingredientViewPagerAdapter

        // 카테고리 적용
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = response[position].categoryname
        }.attach()

        ingredientViewPagerAdapter.submitList(response)
    }

    override fun onGetCategoryIngredietSuccess(response: ArrayList<CategoryIngrediets>) {
        ingredients = response
        ViewPagerInit(response)
    }

    override fun onGetCategoryIngredietFailure(message: String) {
        Log.d("Basket", "onGetCategoryIngredietFailure : $message")
    }

    override fun onGetSearchCategoryIngredietSuccess(response: ArrayList<CategoryIngrediets>) {
        ViewPagerInit(response)
    }

    override fun onGetSearchCategoryIngredietFailure(message: String) {
        Log.d("Basket", "onGetSearchCategoryIngredietFailure : $message")
    }

    override fun onGetCategoryIngredietListSuccess(response: ArrayList<CategoryIngrediets>) {
        ingredientViewPagerAdapter.submitList(response)
    }

    override fun onGetCategoryIngredietListFailure(message: String) {
        Log.d("Basket", "onGetCategoryIngredietListFailure : $message")
    }

    override fun onPostGroupIngredientSuccess() {
        TODO("Not yet implemented")
    }

    override fun onPostGroupIngredientFailure(message: String) {
        TODO("Not yet implemented")
    }
}