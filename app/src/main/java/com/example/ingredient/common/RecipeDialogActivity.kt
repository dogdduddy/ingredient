package com.example.ingredient.common

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.example.ingredient.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RecipeDialogActivity : Activity() {
    private var recipeName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_dialog)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onStart() {
        super.onStart()
        var image = findViewById<ImageView>(R.id.recipe_dialog_image)
        image.setImageResource(R.drawable.curry)

        recipeName = intent.extras!!.get("name").toString()
    }

    fun initRecipe(name:String) {
        var recipe = mutableMapOf<String,Any>()
        var refs = Firebase.firestore.collection("Recipe")
        refs.whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { documents ->
                var data = documents.documents.get(0).data
                data!!.get("name")
                recipe["name"] = data.get("name").toString()
                recipe["icon"] = data.get("icon").toString()
                recipe["ingredients"] = data.get("ingredients") as ArrayList<String>
                recipe["like"] = data.get("like").toString()
                recipe["subscribe"] = data.get("subscribe").toString()
                recipe["time"] = data.get("time").toString()
                recipe["level"] = data.get("level").toString()
                recipe["link"] = data.get("link").toString()
            }
    }

    override fun onBackPressed() {
        return
    }
}