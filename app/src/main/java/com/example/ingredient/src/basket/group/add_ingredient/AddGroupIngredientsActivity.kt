package com.example.ingredient.src.basket.group.add_ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.viewpager2.widget.ViewPager2
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.databinding.ActivityAddGroupIngredientsBinding
import com.example.ingredient.src.expirationDate.add_ingredient.models.CategoryIngrediets
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class AddGroupIngredientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddGroupIngredientsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var database: FirebaseFirestore
    private lateinit var ingredientViewPagerAdapter: AddGroupIngredientViewPagerAdapter
    private var pickingredients = mutableListOf<Ingredient>()
    private var ingredients = ArrayList<CategoryIngrediets>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGroupIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseFirestore.getInstance()

        // Categoty 및 재료 리스트 초기화
        getIngredientsInit()

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
            var string = binding.groupAddIngredientSearch.text.toString()
            getIngredients(string)
            // 검색 키워드 string를 firebase로 넘겨서 검색을 진행하는 코드 삽입 or 실행하는 클래스로 넘기기
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

    // 검색의 결과를 받아서 출력하는 메서드
    // 첫 시작은 onCreate에서 ""의 겸색 결과를 넘기도록 코드 삽입 예정 => 전체 출력
    fun ViewPagerInit(response:ArrayList<CategoryIngrediets>) {
        viewPager = binding.groupAddViewpager
        ingredientViewPagerAdapter = AddGroupIngredientViewPagerAdapter(this, this)
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
        TabLayoutMediator(binding.groupAddTablayout, viewPager) { tab, position ->
            tab.text = tablayerName[position]
        }.attach()

        ingredientViewPagerAdapter.submitList(ingredients)
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

    // 최신 08.22
    // 1. 쿼리를 통한 결과(Category Document List)를 반복문으로 돌림
    // 2. 각 카테고리의 ingredientList를 가져와 ingredientQuery 메서드 에서 재료리스트의 재료들을 ingredients collection에서 불러옴
    // 3. categoryMerge 메서드 에서 해당 재료 리스트와 Category를 결합함
    // categoryList는 공유 변수로 두고 이용함.
    fun getIngredientsInit() {
        val refs = database.collection("Category")
        var categoryList = ArrayList<CategoryIngrediets>()

        // 3번
        fun categoryMerge(document:QueryDocumentSnapshot, ingredientList:MutableList<Ingredient>) {
            categoryList.add(CategoryIngrediets(
                document.get("categoryid").toString().toInt(),
                document.get("categoryname").toString(),
                ingredientList as List<Ingredient>
            ))
            ViewPagerInit(categoryList)
        }
        // 2번
        fun ingredientQuery(document:QueryDocumentSnapshot, list:List<String>) {
            var ingredientList = mutableListOf<Ingredient>()
            for (i in 0 until list.size step 10) {
                var temp = listOf<String>()
                if(i/10 == list.size/10) {
                    temp = list.subList(i,list.size)
                }else {
                    temp = list.subList(i,i+10)
                }
                database.collection("ingredients")
                    .whereIn("ingredientname", temp)
                    .get()
                    .addOnSuccessListener { doc ->
                        doc.forEach {
                            ingredientList.add(
                                Ingredient(
                                    it.get("ingredienticon").toString(),
                                    it.get("ingredientidx").toString().toInt(),
                                    it.get("ingredientname").toString(),
                                    it.get("ingredientcategory").toString()
                                )
                            )
                        }
                    }
            }
            categoryMerge(document, ingredientList)
        }

        // Category Collection 쿼리
        refs.get()
            .addOnSuccessListener { documents ->
                var sortedDocs = documents.sortedBy { it.get("categoryid").toString().toInt() }
                for (document in sortedDocs) {
                    ingredientQuery(document,(document.get("ingredientlist") as List<String>))
                }
            }
    }

    // Firetore DocumentSnapshot to Categoryingrediets
    fun getIngredients(keyword: String) {
        val refs = database.collection("ingredients")
        // 검색 통해 나온 레시피명을 담는 리스트
        refs.orderBy("ingredientname").startAt(keyword).endAt(keyword+ "\uf8ff")
            .get()
            .addOnSuccessListener { doc ->
                var ingredientList = mutableListOf<Ingredient>()
                doc.forEach {
                    ingredientList.add(
                        Ingredient(
                            it.get("ingredienticon").toString(),
                            it.get("ingredientidx").toString().toInt(),
                            it.get("ingredientname").toString(),
                            it.get("ingredientcategory").toString()
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