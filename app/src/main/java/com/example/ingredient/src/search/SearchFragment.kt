package com.example.ingredient.src.search

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ingredient.R
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.databinding.FragmentSearchBinding
import com.example.ingredient.common.RecipeDialogActivity
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import android.view.inputmethod.InputMethodManager as InputMethodManager

class SearchFragment : Fragment(), MainActivity.onBackPressListener {
    private var adapter = SearchAdapter()
    private lateinit var database: FirebaseFirestore

    private var strList = mutableListOf<String>()
    private var recipeList: ArrayList<MutableMap<String, String>> = ArrayList()
    private var _binding : FragmentSearchBinding? = null
    private val binding get()  = _binding!!
    private var imm:InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseFirestore.getInstance()
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Toast.makeText(context, "onViewCreated : "+recipeList,Toast.LENGTH_LONG).show()
        adapterConnect(recipeList)
    }

    override fun onStart() {
        super.onStart()
        //fulltextUpdate()
        //insertDate()
        IniteditTextFocus()

        binding.chipGroupDelAllBtn.setOnClickListener {
            binding.chipGroup.removeAllViews()
            strList.clear()
            for(i in 0 until recipeList.size) adapter.nullItem()
        }

        binding.searchCancleBtn.setOnClickListener {
            binding.findwindow.setText("")
            binding.findwindow.clearFocus()
            imm?.hideSoftInputFromWindow(binding.findwindow.windowToken, 0)
        }

        // 키보드 자판 돋보기로 검색 실행 기능
        binding.findwindow.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchBtn.performClick()
                handled = true
            }
            handled
        }

        binding.chipgroupDrawerBtn.setOnClickListener {
            if(binding.chipGroup.isSingleLine == true) {
                binding.chipGroup.setSingleLine(false)
                binding.chipgroupDrawerBtn.rotation = 270f
            }
            else {
                binding.chipGroup.setSingleLine(true)
                binding.chipgroupDrawerBtn.rotation = 90f
            }
            binding.chipGroup.requestLayout()
        }
        binding.searchCancleBtn.setOnClickListener {
            binding.findwindow.setText("")
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        binding.searchBtn.setOnClickListener {
            // 검색창에 입력한 재료들 리스트화
            var inputData = binding.findwindow.text.toString()
            binding.findwindow.setText("")
            if(!inputData.isNullOrBlank()) {
                hideKeyboard()
                inputData.split(",").forEach { element ->
                    if(checkDuplicate(element)) {
                        // Chip 앞쪽에 추가
                        strList.add(0,element.trim())
                        // chip 생성(왼쪽에 추가) 바꾸려면 인덱스 번호를 제거 하거나, 조정
                        binding.chipGroup.addView(Chip(context).apply {
                            chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.grey_200, null))
                                text = element // chip 텍스트 설정
                            isCloseIconVisible = true // chip에서 X 버튼 보이게 하기
                            //Chip X 클릭 이벤트
                            setOnCloseIconClickListener {
                                binding.chipGroup.removeView(this)
                                strList.remove(text)
                                // chip 삭제 반영
                                if (strList.isNullOrEmpty()) {
                                    for (i in 0 until recipeList.size) adapter.nullItem()
                                    // 동적 버튼 / 검색어 없을시 서치바 위치가 내려가도록
                                    //setSearchBarMargin(down)
                                }
                                else SearchQuery(database, strList)
                            }
                        }, 0)
                        SearchQuery(database, strList)
                    }
                }
            }
        }
    }
/*

    // 데이터 추가
    fun insertDate() {
        database.collection("Recipes")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    if(!(doc.data["fulltext"] as MutableList<String>?).isNullOrEmpty()) {
                        when ((doc.data["fulltext"] as MutableList<String>?)!!.last()) {
                            "스콘" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "스콘",
                                            "like" to "34",
                                            "subscribe" to "12",
                                            "level" to "Normal",
                                            "time" to "120",
                                            "link" to "https://www.10000recipe.com/recipe/6833706",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%A9%E1%86%AB.jpeg?alt=media&token=383d4f57-f75c-466e-811b-f92ea2af101e",
                                            "ingredient" to listOf(
                                                "박력분",
                                                "소금",
                                                "베이킹소다",
                                                "우유",
                                                "설탕",
                                                "베이킹파우더",
                                                "버터"
                                            )
                                        )
                                    )
                            }
                            "땅콩버터쿠키" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "땅콩버터쿠키",
                                            "like" to "77",
                                            "subscribe" to "56",
                                            "level" to "Normal",
                                            "time" to "160",
                                            "link" to "https://www.10000recipe.com/recipe/2480375",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%84%E1%85%A1%E1%86%BC%E1%84%8F%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A5%E1%84%90%E1%85%A5%E1%84%8F%E1%85%AE%E1%84%8F%E1%85%B5.jpeg?alt=media&token=c5c91c4e-6214-4566-8321-dbe5d438dbca",
                                            "ingredient" to listOf("계란", "중력분", "베이킹소다", "버터", "설탕", "소금")
                                        )
                                    )
                            }
                            "마들렌" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "마들렌",
                                            "like" to "67",
                                            "subscribe" to "34",
                                            "level" to "Normal",
                                            "time" to "160",
                                            "link" to "https://www.10000recipe.com/recipe/6892928",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%86%E1%85%A1%E1%84%83%E1%85%B3%E1%86%AF%E1%84%85%E1%85%A6%E1%86%AB.jpeg?alt=media&token=423a7633-7378-4eee-b607-79a3e58f9e36",
                                            "ingredient" to listOf(
                                                "계란",
                                                "밀가루",
                                                "아몬드",
                                                "버터",
                                                "설탕",
                                                "베이킹파우더"
                                            )
                                        )
                                    )
                            }

                            "호떡" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "호떡",
                                            "like" to "34",
                                            "subscribe" to "12",
                                            "level" to "Normal",
                                            "time" to "35",
                                            "link" to "https://www.10000recipe.com/recipe/6863101",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%92%E1%85%A9%E1%84%84%E1%85%A5%E1%86%A8.jpeg?alt=media&token=8d488e96-ae59-4ef7-82d7-83797d769ea6",
                                            "ingredient" to listOf(
                                                "밀가루",
                                                "물",
                                                "베이킹파우더",
                                                "설탕",
                                                "우유",
                                                "식용류"
                                            )
                                        )
                                    )
                            }

                            "두부전골" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "두부전골",
                                            "like" to "276",
                                            "subscribe" to "167",
                                            "level" to "Normal",
                                            "time" to "45",
                                            "link" to "https://www.10000recipe.com/recipe/6918127",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%83%E1%85%AE%E1%84%87%E1%85%AE%E1%84%8C%E1%85%A5%E1%86%AB%E1%84%80%E1%85%A9%E1%86%AF.jpeg?alt=media&token=a16a5892-d1da-44bf-95b1-1b2aec7946e2",
                                            "ingredient" to listOf(
                                                "두부",
                                                "애호박",
                                                "양파",
                                                "대파",
                                                "청양고추",
                                                "느타리버섯",
                                                "고춧가루",
                                                "마늘"
                                            )
                                        )
                                    )
                            }

                            "칼국수" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "칼국수",
                                            "like" to "132",
                                            "subscribe" to "64",
                                            "level" to "Easy",
                                            "time" to "40",
                                            "link" to "https://www.10000recipe.com/recipe/6883666",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%8F%E1%85%A1%E1%86%AF%E1%84%80%E1%85%AE%E1%86%A8%E1%84%89%E1%85%AE.jpeg?alt=media&token=6b569ce6-52df-4835-8b48-df94c7eafd98",
                                            "ingredient" to listOf("칼국수면", "계란", "멸치", "고춧가루", "마늘", "국간장", "소금")
                                        )
                                    )
                            }
                            "쭈꾸미볶음" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "쭈꾸미볶음",
                                            "like" to "105",
                                            "subscribe" to "78",
                                            "level" to "Normal",
                                            "time" to "45",
                                            "link" to "https://www.10000recipe.com/recipe/6324187",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%8D%E1%85%AE%E1%84%81%E1%85%AE%E1%84%86%E1%85%B5%E1%84%87%E1%85%A9%E1%86%A9%E1%84%8B%E1%85%B3%E1%86%B7.jpeg?alt=media&token=9a68521b-6884-40fc-9736-2a800ed68567",
                                            "ingredient" to listOf("쭈꾸미", "당근", "대파", "식용유", "소금", "밀가루", "고추장", "고춧가루", "참기름", "마늘", "올리고당")
                                        )
                                    )
                            }
                            "불닭팽이" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "불닭팽이",
                                            "like" to "550",
                                            "subscribe" to "489",
                                            "level" to "Easy",
                                            "time" to "15",
                                            "link" to "https://www.10000recipe.com/recipe/6957636",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%87%E1%85%AE%E1%86%AF%E1%84%83%E1%85%A1%E1%86%B0%E1%84%91%E1%85%A2%E1%86%BC%E1%84%8B%E1%85%B5.jpeg?alt=media&token=913b4e55-fdcb-4c85-9726-0c083a2340a1",
                                            "ingredient" to listOf("팽이버섯", "버터", "진간장", "설탕", "불닭소스", "고추장")
                                        )
                                    )
                            }
                            "매운어묵꼬치" -> {
                                database.collection("Recipes").document(doc.id)
                                    .update(
                                        mapOf(
                                            "name" to "매운어묵꼬치",
                                            "like" to "175",
                                            "subscribe" to "118",
                                            "level" to "Easy",
                                            "time" to "25",
                                            "link" to "https://www.10000recipe.com/recipe/6900996",
                                            "icon" to "https://firebasestorage.googleapis.com/v0/b/ingredient-7f334.appspot.com/o/%E1%84%86%E1%85%A2%E1%84%8B%E1%85%AE%E1%86%AB%E1%84%8B%E1%85%A5%E1%84%86%E1%85%AE%E1%86%A8%E1%84%81%E1%85%A9%E1%84%8E%E1%85%B5.jpeg?alt=media&token=8165f0bc-8c5b-468d-99d7-5c33218946f0",
                                            "ingredient" to listOf("어묵", "멸치", "고추장", "마늘", "고춧가루", "양조간장", "올리고당")
                                        )
                                    )
                            }
                            else -> ""
                        }
                    }
                }
            }
    }

 */

/*
        // fullltext만 Update
    fun fulltextUpdate() {
        database.collection("Recipes")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    if (doc.data["name"]!!.equals("칼국수")) {
                        fun fullText(string: String): ArrayList<String> {
                            var fulltextList = ArrayList<String>()
                            val length: Int = string.length
                            for (i in 1..length) {
                                for (j in 0..length - i) {
                                    fulltextList.add(string.slice(j until j + i))
                                }
                            }
                            return fulltextList
                        }
                        database.collection("Recipes").document(doc.id)
                            .update(mapOf("fulltext" to fullText(doc.data["name"].toString())))
                    }
                }

        }
    }

 */


    fun SearchQuery(database:FirebaseFirestore, strList:MutableList<String>):Unit {
        val refs = database.collection("Recipes")
        // 검색 통해 나온 레시피명을 담는 리스트
        recipeList.clear()

        refs.whereArrayContainsAny("ingredient", strList).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 레시피 검색해서 나온 이름, 재료, 시간 저장
                    var ing_str: String = ""
                    (document.get("ingredient") as ArrayList<String>).forEachIndexed { index, element ->
                        if(index == 0)
                            ing_str = element
                        else
                            ing_str += " / $element"
                    }
                    recipeList.add(
                        mutableMapOf("name" to document.get("name").toString(),
                            "ingredient" to ing_str,
                            "like" to document.get("like").toString(),
                            "subscribe" to document.get("subscribe").toString(),
                            "icon" to document.get("icon").toString(),
                        ))
                }
                adapterConnect(recipeList)
            }
    }

    // Adapter에서 setItemClickListener interface 생성
    // interface에 onClick 메서드 생성
    // interface 타입의 변수를 갖고 있는 setItemClickListener 메서드 생성
    // fragment에서 setItemClickListener 메서드를 실행 후 onClick 메서드를 재정의

    private fun adapterConnect(recipeList: ArrayList<MutableMap<String, String>>){
        adapter.submitList(recipeList)
        adapter.setItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, RecipeDialogActivity::class.java)
                intent.putExtra("name", recipeList[position]["name"].toString())
                (activity as MainActivity).addRecentRecipe(recipeList[position]["name"].toString(), recipeList[position]["icon"].toString())
                startActivity(intent)
            }
        })

        binding.FindrecyclerView.layoutManager = GridLayoutManager(activity, 2)
        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
        binding.FindrecyclerView.adapter = adapter
    }

    // 검색 후 키보드 내리기
    fun hideKeyboard() {
        imm?.hideSoftInputFromWindow(binding.findwindow.windowToken,0)
    }

    fun showKeyboard() {
        imm?.showSoftInput(binding.findwindow,0)
    }

    fun IniteditTextFocus() {
        binding.findwindow.requestFocus()
        showKeyboard()
    }

    // 검색 재료 중복 체크
    fun checkDuplicate(element: String): Boolean {
        return !strList.any { it == element.trim()}
    }

    companion object {
        fun newInstance() =
            SearchFragment()
    }

    override fun onBackPressed() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}