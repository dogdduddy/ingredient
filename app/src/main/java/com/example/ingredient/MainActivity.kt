package com.example.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.ingredient.fragment.ExpirationDate
import com.example.ingredient.fragment.FoodBook
import com.example.ingredient.fragment.Search
import com.example.ingredient.fragment.Tips
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val searchFragment = Search()
    private val expirationdateFragment = ExpirationDate()
    private val tipsFragment = Tips()
    private val foodbookFragment = FoodBook()


    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(searchFragment) // 시작화면은 search 화면으로

        menu_bottom.setOnItemSelectedListener() { id ->
            when (id) {
                R.id.search -> replaceFragment(searchFragment)
                R.id.expiration_date -> replaceFragment(expirationdateFragment)
                R.id.tips -> replaceFragment(tipsFragment)
                R.id.food_book -> replaceFragment(foodbookFragment)
            }
        }

        val db = Firebase.firestore
        // Add Data
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )
        // Add a new document with a generated ID
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
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transection = supportFragmentManager.beginTransaction()
            transection.replace(R.id.fragment_container, fragment)
            transection.commit()
        }
    }
}