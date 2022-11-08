package com.example.ingredient.src.search

import android.content.Context
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
import com.example.ingredient.common.PurchaseConfirmationDialogFragment
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
        Toast.makeText(context, "onViewCreated : "+recipeList,Toast.LENGTH_LONG).show()
        adapterConnect(recipeList)
    }

    override fun onStart() {
        super.onStart()

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
    // 데이터 이전
    fun InsertDate() {
        database.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val data = hashMapOf(
                        "fulltext" to doc.data["fulltext"],
                        "ingredients" to doc.data["ingredients"],
                        "name" to doc.data["name"],
                        "like" to Random().nextInt(60) + 80,
                        "subscribe" to Random().nextInt(60) + 80,
                        "icon" to ""
                    )
                    database.collection("Recipes").document()
                        .set(data)
                        .addOnSuccessListener { Log.d("Test", "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w("Test", "Error writing document", e) }
                }
            }
    }

    // fullltext만 Update
    fun InsertDate() {
        database.collection("Recipes")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    if (!doc.data["name"]!!.equals("김치볶음밥")) {
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
                            .update("fulltext", fullText(doc.data["name"].toString()))
                    }
                }
            }
    }

     */


    fun SearchQuery(database:FirebaseFirestore, strList:MutableList<String>):Unit {
        val refs = database.collection("Recipes")
        // 검색 통해 나온 레시피명을 담는 리스트
        recipeList.clear()

        refs.whereArrayContainsAny("ingredients", strList).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 레시피 검색해서 나온 이름, 재료, 시간 저장
                    var ing_str: String = ""
                    (document.get("ingredients") as ArrayList<String>).forEachIndexed { index, element ->
                        if(index == 0)
                            ing_str = element
                        else
                            ing_str += " / $element"
                    }
                    recipeList.add(
                        mutableMapOf("name" to document.get("name").toString(),
                            "ingredients" to ing_str,
                            "like" to document.get("like").toString(),
                            "subscribe" to document.get("subscribe").toString()
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

        // Fragment
        adapter.setItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                PurchaseConfirmationDialogFragment(recipeList[position]["name"].toString()).  show(
                    childFragmentManager, PurchaseConfirmationDialogFragment.TAG
                )
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