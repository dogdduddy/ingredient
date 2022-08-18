package com.example.ingredient.src.expirationDate.add_ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.contains
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.fragment.app.findFragment
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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
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


        // 임시 재료 데이터
        val carrot = Ingredient("carrot", 0,"Carrot")
        val potato = Ingredient("potato", 1,"potato")
        val chicken = Ingredient("chicken", 2,"chicken")
        val fish = Ingredient("fish", 3,"fish")
        val shrimp = Ingredient("shrimp", 4, "shrimp")

        // 임시 카테고리별 재료 데이터
        val all =  CategoryIngrediets(0, "전체", listOf(carrot, potato, chicken, shrimp, fish))
        val bag = CategoryIngrediets(1, "채소", listOf(carrot, potato))
        val meat = CategoryIngrediets(2, "육류", listOf(chicken))
        val seafood = CategoryIngrediets(3, "해산물", listOf(fish, shrimp))


        val category = listOf(all, bag, meat, seafood)

        // 전체를 출력하기 위한 코드
        // DB구성 후에는 DB로 "" 키워드를 가지고 검색해서 전체를 출력하도록 만들면 됨
        ViewPagerInit(category)

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
    }

    // 검색의 결과를 받아서 출력하는 메서드
    // 첫 시작은 onCreate에서 ""의 겸색 결과를 넘기도록 코드 삽입 예정 => 전체 출력
    fun ViewPagerInit(response:List<CategoryIngrediets>) {
        viewPager = binding.viewpagerAddIngredient
        ingredientViewPagerAdapter = AddIngredientViewPagerAdapter(this, this)
        viewPager.adapter = ingredientViewPagerAdapter

        // 임시 카테고리 이름 데이터
        var tablayerName = ArrayList<String>()

        // 재료 리스트를 적용 및 카테고리만 추출
        ingredients.clear()

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

    // Firetore DocumentSnapshot -> Categoryingrediets
    fun getIngredients(keyword: String) {
        val refs = database.collection("Category")
        // 검색 통해 나온 레시피명을 담는 리스트
        var categoryList = mutableListOf<CategoryIngredietsTest>()
        refs.whereNotEqualTo("categoryid", 2).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("firestoreTest", "Document : ${document}")


                    var test = (document.data.get("ingredientlist") as List<Any>).get(0) as DocumentReference
                    (document.get("ingredientlist") as List<Any>)
                        .forEach{
                            (it as DocumentReference)
                                .get()
                                .addOnSuccessListener { doc ->
                                    Log.d("firestoreTest", "DocumentGet9-1 ${doc.get("ingredientname")}")
                                }
                        }
                    /*
                    var test:CategoryIngredietsTest = document.toObject(CategoryIngredietsTest::class.java)
                    Log.d("firestoreTest", "Model : ${test}")
                    Log.d("firestoreTest", "Reference : ${test.ingredientList}")

                    categoryList.add(test)

                     */
                }
                Log.d("firestoreTest", "List : ${categoryList}")
            }
        //ViewPagerInit(categoryList)
    }

}