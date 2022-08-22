package com.example.ingredient.src.expirationDate.add_ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityAddingredientsBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngredietsTest
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.example.ingredient.src.expirationDate.add_ingredient.models.Testmodel
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.getField
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*
import java.lang.ref.Reference

class AddIngredientsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddingredientsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var database:FirebaseFirestore
    private lateinit var ingredientViewPagerAdapter:AddIngredientViewPagerAdapter
    private var pickingredients = mutableListOf<String>()
    private var ingredients = ArrayList<CategoryIngrediets>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddingredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()

        getIngredientsInit()

        // 검색 기능 구현 중
        // 타자기 검색 버튼으로 검색 버튼 클릭 효과
        binding.ingredientSearch.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.ingredientSearchBtn.performClick()
                handled = true
            }
            handled
        }
        // 검색 버튼
        binding.ingredientSearchBtn.setOnClickListener {
            var string = binding.ingredientSearch.text.toString()
            getIngredients(string)
            // 검색 키워드 string를 firebase로 넘겨서 검색을 진행하는 코드 삽입 or 실행하는 클래스로 넘기기
        }

        // 검색 취소 버튼
        binding.ingredientSearchCancle.setOnClickListener {
            getIngredientsInit()
        }
    }

    // 검색의 결과를 받아서 출력하는 메서드
    // 첫 시작은 onCreate에서 ""의 겸색 결과를 넘기도록 코드 삽입 예정 => 전체 출력
    fun ViewPagerInit(response:ArrayList<CategoryIngrediets>) {
        Log.d("TTTT", "4 : ${response}")
        viewPager = binding.viewpagerAddIngredient
        ingredientViewPagerAdapter = AddIngredientViewPagerAdapter(this, this)
        viewPager.adapter = ingredientViewPagerAdapter

        // 임시 카테고리 이름 데이터
        var tablayerName = ArrayList<String>()

        // 재료 리스트를 적용 및 카테고리만 추출
        ingredients.clear()

        Log.d("TTTT", "4 : ${response}")
        response.forEach {
            ingredients.add(it)
            tablayerName.add(it.ingredientCategoryName)
        }

        // 카테고리 적용
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = tablayerName[position]
        }.attach()

        ingredientViewPagerAdapter.submitList(ingredients)
    }

    fun addingredientClick(ingredient:String) {
        if(!pickingredients.contains(ingredient)) {
            pickingredients.add(ingredient)
            // 추가된 재료 리사이클러뷰에 추가 후 notification  =>  submitlist
            binding.pickingredientChip.addView(Chip(this).apply {
                text = ingredient
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    pickingredients.remove(this.text)
                    binding.pickingredientChip.removeView(this)
                }
            }, 0)
        }
    }

    // 최신 08.22
    // 1. 쿼리를 통한 결과(Category Document List)를 반복문으로 돌림
    // 2. 각 카테고리의 ingredientList를 가져와 ingredientQuery 메서드 에서 재료리스트의 재료들을 ingredients collection에서 불러옴
    // 3. categoryMerge 메서드 에서 해당 재료 리스트와 Category를 결합함
    // categoryList는 공유 변수로 두고 이용함.
    fun getIngredientsInit() {
        val refs = database.collection("Category")
        var categoryList = ArrayList<CategoryIngrediets>()

        // 3번
        fun categoryMerge(document:QueryDocumentSnapshot, doc:QuerySnapshot) {
            var ingredientList = mutableListOf<Ingredient>()
            doc.forEach {
                ingredientList.add(
                    Ingredient(
                        it.get("ingredienticon").toString(),
                        it.get("ingredientidx").toString().toInt(),
                        it.get("ingredientname").toString()
                    )
                )
            }
            categoryList.add(CategoryIngrediets(
                document.get("categoryid").toString().toInt(),
                document.get("categoryname").toString(),
                ingredientList as List<Ingredient>
            ))
            if(categoryList.size == 2) {
                ViewPagerInit(categoryList)
            }
        }
        // 2번
        fun ingredientQuery(document:QueryDocumentSnapshot, list:List<String>) {
            database.collection("ingredients")
                .whereIn("ingredientname", list)
                .get()
                .addOnSuccessListener {
                    categoryMerge(document, it)
                }
        }

        // Category Collection 쿼리
        refs.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    ingredientQuery(document,(document.get("ingredientlist") as List<String>))
                }
            }
    }


    // Firetore DocumentSnapshot -> Categoryingrediets
    fun getIngredients(keyword: String) {
        val refs = database.collection("ingredients")
        // 검색 통해 나온 레시피명을 담는 리스트
        //refs.whereArrayContains("ingredienttag", keyword)
        refs.orderBy("ingredientname").startAt(keyword).endAt(keyword+ "\uf8ff")
            .get()
            .addOnSuccessListener { doc ->
                var ingredientList = mutableListOf<Ingredient>()
                doc.forEach {
                    ingredientList.add(
                        Ingredient(
                            it.get("ingredienticon").toString(),
                            it.get("ingredientidx").toString().toInt(),
                            it.get("ingredientname").toString()
                        )
                    )
                }
                var temt = arrayListOf<CategoryIngrediets>()
                ingredients.forEachIndexed { i, v ->
                    temt.add(
                        CategoryIngrediets(
                            v.ingredientCategoryIdx,
                            v.ingredientCategoryName,
                            if(i==0) ingredientList else v.ingredientList
                        )
                    )
                }
                ViewPagerInit(temt)
            }
    }

}