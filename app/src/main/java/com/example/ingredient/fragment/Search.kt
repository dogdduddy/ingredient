package com.example.ingredient.fragment

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.SearchAdapter
import com.example.ingredient.databinding.FragmentSearchBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import android.view.inputmethod.InputMethodManager as InputMethodManager

class Search : Fragment() {
    private lateinit var adapter: SearchAdapter
    private lateinit var database: FirebaseFirestore

    private var strList = mutableListOf<String>()
    private var recipeList = mutableListOf<Array<String>>()
    private var _binding : FragmentSearchBinding? = null
    private val binding get()  = _binding!!
    private var imm:InputMethodManager? = null

    // 구글과 같은 동적 애니메이션 위한 코드
    private val layoutParams = ConstraintLayout.LayoutParams(250, 48)
    private val up = 40
    private val down = 140

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
        // 키보드 자판 돋보기로 검색 실행 기능
        binding.findwindow.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchBtn.performClick()
                handled = true
            }
            handled
        }

        binding.searchBtn.setOnClickListener {
            // 검색창에 입력한 재료들 리스트화
            var str = binding.findwindow.text.toString().split(",")
            binding.findwindow.setText("")
            // 검색 후 키보드 내리기
            imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(binding.findwindow.windowToken,0)

            if (str.size > 0) {
                var check = true
                // 검색한 재료의 좌우 빈칸 제거 ex) " 감자" -> "감자"
                for (element in str) {
                    check = true
                    for (i in 0 until strList.size) {
                        if (strList[i] == element) {
                            check = false
                            break
                        }
                    }
                    // 검색 재료 중복 체크
                    if (element.isNullOrEmpty()) check = false
                    if(check) {
                        strList.add(element.trim())
                        // chip 생성
                        binding.chipGroup.addView(Chip(context).apply {
                            text = element.trim() // chip 텍스트 설정
                            isCloseIconVisible = true // chip에서 X 버튼 보이게 하기
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
                        })
                    }
                }
                if (check) {
                    // 동적 버튼 / 검색시 서치바 위치가 올라r가도록
                    //setSearchBarMargin(up)
                    // null, 중복 없다면 쿼리 실행
                    SearchQuery(database, strList)
                }
            }
        }
    }

    fun SearchQuery(database:FirebaseFirestore, strList:MutableList<String>):Unit {
        val refs = database.collection("users")
        // 검색 통해 나온 레시피명을 담는 리스트
        recipeList = mutableListOf<Array<String>>()
        refs.whereArrayContainsAny("ingredients", strList).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("MainTest : ", document.toString())
                    // 레시피 검색해서 나온 이름, 재료, 시간 저장

                    var int_str: String = document.get("ingredients").toString()
                    // 재료들을 포함하는 리스트
                    int_str = int_str.substring(1..int_str.length - 2)
                    recipeList.add(
                        arrayOf(
                            document.get("name").toString(),
                            int_str,
                            document.get("time").toString()
                        )
                    )
                }
                adapterConnect(recipeList)
            }
    }

    // Adapter에서 setItemClickListener interface 생성
    // interface에 onClick 메서드 생성
    // interface 타입의 변수를 갖고 있는 setItemClickListener 메서드 생성
    // fragment에서 setItemClickListener 메서드를 실행 후 onClick 메서드를 재정의

    private fun adapterConnect(recipeList: MutableList<Array<String>>){
        adapter = SearchAdapter(recipeList)

        // Fragment
        adapter.setItemClickListener(object: SearchAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                PurchaseConfirmationDialogFragment(recipeList[position][0].toString()).  show(
                    childFragmentManager, PurchaseConfirmationDialogFragment.TAG)
            }
        })

        binding.FindrecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
        binding.FindrecyclerView.adapter = adapter
    }

    fun setSearchBarMargin(locate:Int){
        // 동적 버튼 / 검색시 서치바 위치가 올라가도록
        layoutParams.setMargins(0, locate,0,0)
        binding.findwindow.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(context, "Destroy",Toast.LENGTH_LONG).show()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Toast.makeText(context, "Detach",Toast.LENGTH_LONG).show()
    }
}