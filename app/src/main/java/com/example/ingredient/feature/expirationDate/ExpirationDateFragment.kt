package com.example.ingredient.feature.expirationDate

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.databinding.FragmentExpirationDateBinding
import com.example.ingredient.feature.expirationDate.add_ingredient.AddIngredientsActivity
import com.example.ingredient.feature.expirationDate.add_ingredient.models.ExpiryDateIngredient
import com.example.ingredient.feature.expirationDate.add_ingredient.models.Ingredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class ExpirationDateFragment : Fragment() {
    private var _binding:FragmentExpirationDateBinding? = null
    private val binding get() = _binding!!
    private var expiryDates = ArrayList<ExpiryDateIngredient>()
    private lateinit var userid:String
    private var sortNumber = 0
    private lateinit var database: FirebaseFirestore
    private lateinit var expiryAdapter:ExpirationDateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpirationDateBinding.inflate(layoutInflater, container, false)
        database = FirebaseFirestore.getInstance()

        // 임시 데이터 출력 메서드
        updateData()

        // 삭제 버튼 안 보이도록 설정
        showBtnDelete(false)

        // Adapter 연결
        expiryAdapter = ExpirationDateAdapter() {show -> showBtnDelete(show)}
        binding.expirationRecyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.expirationRecyclerview.adapter = expiryAdapter

        binding.expiryAddBtn.setOnClickListener {
            var intent = Intent(activity, AddIngredientsActivity::class.java)
            startActivity(intent)
        }
        expiryAdapter.ExpiryDateSubmitList(expiryDates)

        // 삭제 버튼 클릭
        binding.removeExpiryIngredientBtn.setOnClickListener {
            expiryAdapter.deleteSelectedItem(userid!!)
        }


        /// 정렬 선택지 보여줄 다이얼로그
        binding.expirySortLayout.setOnClickListener {
            var dialogList = arrayOf("최신순", "오래된순", "적은순", "많은순")
            AlertDialog.Builder(context)
                .setTitle("정렬")
                .setItems(dialogList, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        sortNumber = which
                        binding.expirySortText.text = dialogList[which]
                        DataUpdate(expiryDates)
                    }
                }).show()
        }
        return binding.root
    }

    // 유통기한 재료 리스트 설정
    fun sortList(number:Int) {
        if(number == 0)
            expiryDates.sortBy { it.expirydate }
        else if(number == 1)
            expiryDates.sortByDescending { it.expirydate }
        else if(number == 2)
            expiryDates.sortByDescending { it.addedDate }
        else
            expiryDates.sortBy { it.addedDate }
    }

    override fun onStart() {
        super.onStart()
        ExpirationDateSubmit()
    }

    fun updateData() {
        userid = FirebaseAuth.getInstance().uid!!
        database.collection("ListData")
            .document(userid)
            .collection("Refrigerator")
            .get()
            .addOnSuccessListener { documents ->
                var temp = ArrayList<ExpiryDateIngredient>()
                for (document in documents) {
                    temp.add(ExpiryDateIngredient(
                        Ingredient(
                            document.get("ingredienticon").toString(),
                            document.get("ingredientidx").toString().toInt(),
                            document.get("ingredientname").toString(),
                            document.get("ingredientcategory").toString()
                        ),
                        document.get("expirydate").toString().toInt(),
                        document.get("ingredientstatus").toString().toInt(),
                        document.get("storagestatus").toString().toInt(),
                        false,
                        (document.get("localdate") as com.google.firebase.Timestamp).toDate(),
                        (document.get("discard") as com.google.firebase.Timestamp).toDate()
                    ))
                }
                DataUpdate(temp)
            }
            .addOnFailureListener { Log.d("Expiry", "Auth Exception : ${it}") }
    }

    fun ExpirationDateSubmit() { updateData() }

    fun DataUpdate(expiryDate:ArrayList<ExpiryDateIngredient>) {
        expiryDates = expiryDate
        sortList(sortNumber)
        expiryAdapter.ExpiryDateSubmitList(expiryDates)
    }
    // 유통기한 재료 삭제 버튼 보이기 여부
    fun showBtnDelete(show:Boolean) {
        binding.removeExpiryIngredientBtn.isVisible = show
    }
    companion object {
        fun newInstance() =
            ExpirationDateFragment()
    }
}
