package com.example.ingredient.common

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
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

        recipeName = intent.extras!!.get("name").toString()
        initRecipe(recipeName!!)
    }

    fun initRecipe(name:String) {
        var title = findViewById<TextView>(R.id.recipe_dialog_content_box_title)
        var image = findViewById<ImageView>(R.id.recipe_dialog_image)
        var time = findViewById<TextView>(R.id.recipe_dialog_content_box_time_text)
        var count = findViewById<TextView>(R.id.recipe_dialog_content_box_count_text)
        var level = findViewById<TextView>(R.id.recipe_dialog_content_box_level_text)
        var ingredients = findViewById<TextView>(R.id.recipe_dialog_ing_box_ingredients)

        var recipe = mutableMapOf<String,Any>()
        var refs = Firebase.firestore.collection("Recipes")
        refs.whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { documents ->
                var data = documents.documents.get(0).data
                data!!.get("name")
                recipe["name"] = data.get("name").toString()
                recipe["icon"] = data.get("icon").toString()
                recipe["ingredients"] = data.get("ingredient") as ArrayList<String>
                recipe["like"] = data.get("like").toString()
                recipe["subscribe"] = data.get("subscribe").toString()
                recipe["time"] = data.get("time").toString()
                recipe["level"] = data.get("level").toString()
                recipe["link"] = data.get("link").toString()

                //
                title.text = recipe["name"].toString()
                time.text = recipe["time"].toString() + "분"
                level.text = recipe["level"].toString()
                count.text = (recipe["ingredients"] as ArrayList<String>).size.toString() + "가지"
                var str = ""
                (recipe["ingredients"] as ArrayList<String>) .forEachIndexed { index, s ->
                    if(index == 0) {
                        str = s
                    }
                    else {
                        str += " / " + s
                    }
                }
                ingredients.text = str

                Glide.with(this)
                    .load(recipe["icon"])
                    .into(image)
            }


    }

    override fun onBackPressed() {
        return
    }
}