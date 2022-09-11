package com.example.ingredient.src.expirationDate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.database.ExpirationDateDao
import com.example.ingredient.database.ExpirationDateDatabase
import com.example.ingredient.databinding.FragmentExpirationDateBinding
import com.example.ingredient.src.expirationDate.add_ingredient.AddIngredientsActivity
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpirationDate : Fragment() {
    private var _binding:FragmentExpirationDateBinding? = null
    private val binding get() = _binding!!
    private var expiryDates = ArrayList<ExpiryDateIngredient>()
    private lateinit var database: FirebaseFirestore
    private lateinit var expiryAdapter:ExpirationDateAdapter


    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpirationDateBinding.inflate(layoutInflater, container, false)
        database = FirebaseFirestore.getInstance()

        // 임시 데이터 출력 메서드
        checkAuth()

        // Adapter 연결
        expiryAdapter = ExpirationDateAdapter()
        binding.expirationRecyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.expirationRecyclerview.adapter = expiryAdapter

        binding.btnTest.setOnClickListener {
            var intent = Intent(activity, AddIngredientsActivity::class.java)
            startActivity(intent)
        }
        expiryAdapter.ExpiryDateSubmitList(expiryDates)

        return binding.root
    }
    // 데이터 출력을 위해 사용자 식별
    // 따로 구현해야 할것만 같지만 어려워서 auth를 사용하는 것으로 잠시 합의봄
    fun checkAuth() {
        auth = FirebaseAuth.getInstance()

        database.collection("Refrigerator")
            .whereEqualTo("userid", auth.uid)
            .get()
            .addOnSuccessListener { documents ->
                updateData(documents.documents[0].id!!)
            }
    }

    override fun onStart() {
        super.onStart()
        // documentID를 넘겨받는 방식일 때의 코드, auth로 사용자를 인식하는 방식을 할 때에는 불필요
        // if(!documentID.isNullOrEmpty())ExpirationDateSubmit()
        ExpirationDateSubmit()
    }

    fun updateData(documentID:String) {
        database.collection("Refrigerator")
            .document(documentID!!)
            .collection("ingredients")
            .get()
            .addOnSuccessListener { documents ->
                var temp = ArrayList<ExpiryDateIngredient>()
                for (document in documents) {
                    temp.add(ExpiryDateIngredient(
                        Ingredient(
                            document.get("ingredienticon").toString(),
                            document.get("ingredientidx").toString().toInt(),
                            document.get("ingredientname").toString()
                        ),
                        document.get("expirydate").toString().toInt(),
                        document.get("ingredientstatus").toString().toInt(),
                        document.get("storagestatus").toString().toInt()
                    ))
                }
                DataUpdate(temp)
            }
    }

    fun ExpirationDateSubmit() {
        checkAuth()
    }
    fun DataUpdate(expiryDate:ArrayList<ExpiryDateIngredient>) {
        expiryDates = expiryDate
        expiryAdapter.ExpiryDateSubmitList(expiryDates)
    }
    companion object {
        fun newInstance() =
            ExpirationDate()
    }


}
