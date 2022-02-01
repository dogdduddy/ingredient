package com.example.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ingredient.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var adapter: SearchAdapter
    private lateinit var database: FirebaseFirestore
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseFirestore.getInstance()

        /* 파이어베이스 입출력 예
        // Add Data Struct
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 2022
        )
        // coleection name users add Data
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        // Read Data
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


        */
        binding.testBtn.setOnClickListener {
            Log.d(TAG,"TestBtn click!")
            val intent = Intent(this,TestActivity::class.java)
            startActivity(intent)
        }
        // 엔터키로 검색 실행 기능
        binding.findwindow.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchBtn.performClick()
                handled = true
            }
            handled
        }
        binding.searchBtn.setOnClickListener {
            Log.d(TAG,"Click searchBtn")

            // 검색창에 입력한 재료들 리스트화
            var str = binding.findwindow.text.toString().split(",")
            if (str.size > 0) {
                var strList = mutableListOf<String>()
                // 검색한 재료의 좌우 빈칸 제거 ex) " 감자" -> "감자"
                for (element in str) {
                    strList.add(element.trim())
                }

                val refs = database.collection("users")
                // 검색 통해 나온 레시피명을 담는 리스트
                val recipeList = mutableListOf<Array<Any>>()

                refs.whereArrayContainsAny("ingredients", strList).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d("MainTest : ", document.toString())
                            // 레시피 검색해서 나온 이름, 재료, 시간 저장
                            var int_str: String = document.get("ingredients").toString()
                            // 재료들을 포함하는 리스트
                            int_str = int_str.substring(1 .. int_str.length-2)
                            recipeList.add(
                                arrayOf(
                                    document.get("name").toString(),
                                    int_str,
                                    document.get("time").toString()
                                )
                            )
                        }

                        adapter = SearchAdapter(recipeList, applicationContext, database)
                        binding.FindrecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                        binding.FindrecyclerView.itemAnimator = DefaultItemAnimator()
                        binding.FindrecyclerView.adapter = adapter
                    }
            }
        }
    }
}