package com.example.ingredient.src.expirationDate.add_ingredient.ingredientstate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ingredient.R
import com.example.ingredient.activity.MainActivity
import com.example.ingredient.databinding.ActivityIngredientStateBinding
import com.example.ingredient.databinding.ActivityMainBinding
import com.example.ingredient.src.expirationDate.ExpirationDate
import com.example.ingredient.src.expirationDate.ExpirationDateAdapter
import com.example.ingredient.src.expirationDate.add_ingredient.models.ExpiryDateIngredient
import com.example.ingredient.src.expirationDate.add_ingredient.models.Ingredient
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IngredientStateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientStateBinding
    private lateinit var ingredients:ArrayList<Ingredient>
    private lateinit var ExpiryList:Array<ExpiryDateIngredient?>
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()

        ingredients = intent.getParcelableArrayListExtra("ingredients")!!
        ExpiryList = arrayOfNulls<ExpiryDateIngredient>(ingredients.size)

        var adapter = IngredientStateAdapter(ingredients, this)
        binding.statePickingRecycler.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL, false)
        binding.statePickingRecycler.adapter = adapter

        binding.statePickingSaveBtn.setOnClickListener {
            var userid = FirebaseAuth.getInstance().uid!!
            if(!ExpiryList.contains(null)) {
                for(i in 0 until ExpiryList.size) {
                    var hashData = hashMapOf(
                        "ingredienticon" to ExpiryList[i]!!.ingredient.ingredientIcon,
                        "ingredientidx" to ExpiryList[i]!!.ingredient.ingredientIdx,
                        "ingredientname" to ExpiryList[i]!!.ingredient.ingredientName,
                        "expirydate" to ExpiryList[i]!!.expirydate,
                        "ingredientstatus" to ExpiryList[i]!!.ingredientstatus,
                        "storagestatus" to ExpiryList[i]!!.storagestatus,
                        "localdate" to ExpiryList[i]!!.addedDate,
                        "discard" to ExpiryList[i]!!.discard
                    )
                    ExpiryList[i] = null
                    database.collection("ListData")
                        .document(userid)
                        .collection("Refrigerator")
                        .document()
                        .set(hashData)
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("fragment", "expiry")
                startActivity(intent)
            }
        }

        binding.statePickingBackBtn.setOnClickListener {
            finish()
        }
    }
    fun expiryListSubmit(position:Int, value:ExpiryDateIngredient) {
        ExpiryList[position] = value
    }

}
