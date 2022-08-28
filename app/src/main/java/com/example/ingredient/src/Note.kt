package com.example.ingredient.src

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ingredient.activity.TestActivity
import com.example.ingredient.database.ExpirationDateDatabase
import com.example.ingredient.databinding.FragmentNoteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Note : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get()  = _binding!!
    private lateinit var db: ExpirationDateDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        val notePad = binding.notePad

        // 첫 시작시 상태  => 모든 줄을 클릭할 수 있도록 구현하기 위한 꼼수
        //                  => 아래의 기능을 구현하면 필요 없어질 과정

        notePad.setText("\n".repeat(30))

        // 1) 노트패드 크기 고정
        // 2) 노트패드 위치만큼 y에서 차감 => 원래는 절대좌표로 122인데 margin top 122이면 값 0으로
        // 3) 위 값을 기반으로 몇번째 줄 클릭 했는지 파악
        // 4) 현재 줄의 개수를 넘는 곳을 클릭했으면 그만큼 늘려주기
        // 5) 늘려준 후 selectionLast로 가버리기

        notePad.setOnTouchListener { v, event ->
            Log.d("NotePad Touch1 : ",event.y.toString())
            Log.d("NotePad Touch22 : ",event.rawY.toString())
            false
        }
        //// 이미지 로드 테스트
        var storage = Firebase.storage("gs://ingredient-7f334.appspot.com/")
        var testImage = storage.reference.child("/testimage.jpeg")

        binding.testbtn2.setOnClickListener {
            var intent = Intent(activity, TestActivity::class.java)
            startActivity(intent)
        }
        ////
    }
    companion object {
        fun newInstance(db: ExpirationDateDatabase) =
            Note().apply {
                this.db = db
            }
    }

}