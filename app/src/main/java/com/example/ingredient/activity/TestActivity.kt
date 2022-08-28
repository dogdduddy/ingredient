package com.example.ingredient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.ingredient.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        //// 이미지 로드 테스트
        var storage = Firebase.storage("gs://ingredient-7f334.appspot.com")
        var testImage = storage.reference.child("/testimage.jpeg")
        Log.d("storageTest", "Test 1 : ${storage}")
        Log.d("storageTest", "Test 1 : ${storage.reference}")
        Log.d("storageTest", "Test 1 : ${storage.reference.child("testimage1.jpeg")}")


        Log.d("imageTest", "Test 1 : ${testImage}")

        var btn = findViewById<Button>(R.id.testbtn2)
        var imageView = findViewById<ImageView>(R.id.testImage2)
        /*
        btn.setOnClickListener {
            Glide.with(this)
                .load(testImage2)
                .into(imageView)
        }

         */
        Firebase.storage("gs://ingredient-7f334.appspot.com").reference.child("testimage1.jpeg").downloadUrl
            .addOnSuccessListener {
                Glide.with(this)
                    .load(it)
                    .into(imageView)
            }


    }
}