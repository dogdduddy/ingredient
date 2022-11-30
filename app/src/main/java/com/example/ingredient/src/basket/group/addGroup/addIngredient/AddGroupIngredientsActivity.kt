package com.example.ingredient.src.basket.group.addGroup.addIngredient

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.databinding.ActivityAddGroupIngredientsBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
interface AddIngredientView {
    fun onGetCategoryIngredietSuccess(response: ArrayList<CategoryIngrediets>)
    fun onGetCategoryIngredietFailure(message: String)

    fun onGetSearchCategoryIngredietSuccess(response: ArrayList<CategoryIngrediets>)
    fun onGetSearchCategoryIngredietFailure(message: String)

    fun test(response: ArrayList<CategoryIngrediets>)
}

class AddGroupIngredientsActivity : AppCompatActivity(), AddIngredientView {
    private lateinit var binding: ActivityAddGroupIngredientsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var database: FirebaseFirestore
    private lateinit var ingredientViewPagerAdapter: AddGroupIngredientViewPagerAdapter
    private var pickingredients = mutableListOf<Ingredient>()
    private var ingredients = ArrayList<CategoryIngrediets>()
    private var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGroupIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        database = FirebaseFirestore.getInstance()

        // Categoty 및 재료 리스트 초기화
        IngredientService(this).GetCategoryIngrediets()

        // 검색 기능 구현 중
        // 타자기 검색 버튼으로 검색 버튼 클릭 효과
        binding.groupAddIngredientSearch.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.groupAddIngredientSearch.performClick()
                handled = true
            }
            handled
        }
        // 검색 버튼
        binding.groupAddIngredientSearchBtn.setOnClickListener {
            var input = binding.groupAddIngredientSearch.text.toString()
            if(input.isNullOrBlank())
                IngredientService(this).GetCategoryIngrediets()
             else
                IngredientService(this).GetSearchCategoryIngrediets(input, ingredients)
            binding.groupAddIngredientSearch.setText("")
        }


        // 저장 버튼 (선택 재료 넘기기)
        binding.groupAddPickingredientsave.setOnClickListener {
            if(pickingredients.isNotEmpty()) {
                var group = intent.extras!!.get("groupName").toString()
                database.collection("ListData")
                    .document(FirebaseAuth.getInstance().uid!!)
                    .collection("Basket")
                    .whereEqualTo("groupName", group)
                    .get()
                    .addOnSuccessListener { documents ->
                        // 해당 그룹에 재료가 없을 때
                        if(documents.isEmpty()) {
                            pickingredients.forEach {
                                var hash = hashMapOf(
                                    "ingredienticon" to it.ingredientIcon,
                                    "ingredientidx" to it.ingredientIdx,
                                    "ingredientname" to it.ingredientName,
                                    "ingredientcategory" to it.ingredientCategory,
                                    "groupName" to group,
                                    "ingredientquantity" to 1
                                )
                                database.collection("ListData")
                                    .document(FirebaseAuth.getInstance().uid!!)
                                    .collection("Basket")
                                    .document()
                                    .set(hash)
                            }
                        }
                        // 해당 그룹에 재료가 있을 때
                        else {
                            var values = arrayListOf<Ingredient>()
                            for (document in documents) {
                                document.data.apply {
                                    values.add(
                                        Ingredient(
                                            get("ingredienticon").toString(),
                                            get("ingredientidx").toString().toInt(),
                                            get("ingredientname").toString(),
                                            get("ingredientcategory").toString()
                                        )
                                    )
                                }
                            }
                            pickingredients.forEachIndexed { index, ingredient ->
                                var number = values.indexOf(ingredient)
                                // 중복 재료가 있을 때
                                if(number != -1) {
                                    Log.d("AddTest", "Match Data")
                                    database.collection("ListData")
                                        .document(FirebaseAuth.getInstance().uid!!)
                                        .collection("Basket")
                                        .document(documents.documents[number].reference.id)
                                        .update(
                                            mapOf(
                                                "ingredientquantity" to documents.documents[number].data!!.get(
                                                    "ingredientquantity"
                                                ).toString().toInt() + 1
                                            )
                                        )
                                }
                                // 중복 재료가 없을 때
                                else {
                                    var ingredient = pickingredients[index]
                                    var hash = hashMapOf(
                                        "ingredienticon" to ingredient.ingredientIcon,
                                        "ingredientidx" to ingredient.ingredientIdx,
                                        "ingredientname" to ingredient.ingredientName,
                                        "ingredientcategory" to ingredient.ingredientCategory,
                                        "groupName" to group,
                                        "ingredientquantity" to 1
                                    )
                                    database.collection("ListData")
                                        .document(FirebaseAuth.getInstance().uid!!)
                                        .collection("Basket")
                                        .document()
                                        .set(hash)
                                }
                            }
                        }
                    }
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fragment", "basket")
                startActivity(intent)
            }
        }
    }
    fun ViewPagerInit(response:ArrayList<CategoryIngrediets>) {
        viewPager = binding.groupAddViewpager
        ingredientViewPagerAdapter = AddGroupIngredientViewPagerAdapter(this, this)
        viewPager.adapter = ingredientViewPagerAdapter

        // 카테고리 적용
        TabLayoutMediator(binding.groupAddTablayout, viewPager) { tab, position ->
            tab.text = response[position].ingredientCategoryName
        }.attach()
        Log.d("testT", "viewpagerInit : ${response}")
        ingredientViewPagerAdapter.submitList(response)
    }

    fun addingredientClick(ingredient: Ingredient) {
        if(!pickingredients.contains(ingredient)) {
            pickingredients.add(ingredient)
            // 추가된 재료 리사이클러뷰에 추가 후 notification  =>  submitlist
            binding.groupAddPickingredientChip.addView(Chip(this).apply {
                text = ingredient.ingredientName
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    var ingredientNum:Int = 0
                    run { pickingredients.forEachIndexed {
                            i, v -> if(v.ingredientName == this.text) {
                        ingredientNum = i
                        return@run
                    }
                    }}
                    pickingredients.removeAt(ingredientNum)
                    binding.groupAddPickingredientChip.removeView(this)
                    Log.d("piingredients", "P : ${pickingredients}")
                }
            }, 0)
        }
    }
    override fun onStart() {
        super.onStart()
        binding.groupAddIngredientSearch.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.groupAddIngredientSearchBtn.performClick()
                handled = true
            }
            handled
        }
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

    override fun test(response: ArrayList<CategoryIngrediets>) {
        Log.d("testT", "test : ${response}")
        ingredientViewPagerAdapter.submitList(response)
    }
}