package com.dogdduddy.ingredient.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dogdduddy.ingredient.R
import com.dogdduddy.ingredient.activity.MainActivity
import com.dogdduddy.ingredient.src.WebViewActivity
import com.dogdduddy.ingredient.src.foodbook.models.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RecipeDialogActivity : Activity() {
    private var recipeName: String? = null
    private var ingredientList = mutableListOf<String>()
    private val activityThis = this
    private lateinit var recipe:Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_dialog)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onStart() {
        super.onStart()
        var database = Firebase.firestore
        var addBasket: Button = findViewById(R.id.recipe_dialog_pass_basket_btn)
        var linkSite: Button = findViewById(R.id.recipe_dialog_pass_link_btn)

        recipeName = intent.extras!!.getString("name")
        initRecipe(recipeName!!)

        // 미소지 재료 장바구니 넣기
        addBasket.setOnClickListener {
            if(ingredientList.isNotEmpty()) {
                ingredientList.forEach {
                    database.collection("ingredients")
                        .whereEqualTo("ingredientname", it)
                        .get()
                        .addOnSuccessListener {  documents ->
                            if(!documents.documents.isNullOrEmpty()) {
                                documents.documents[0].data.run {
                                    var hash = hashMapOf(

                                        "ingredienticon" to this?.get("ingredienticon").toString(),
                                        "ingredientidx" to this?.get("ingredientidx").toString().toInt(),
                                        "ingredientname" to this?.get("ingredientname").toString(),
                                        "ingredientcategory" to this?.get("ingredientcategory").toString(),
                                        "groupName" to recipeName,
                                        "ingredientquantity" to 1
                                    )
                                    database.collection("ListData")
                                        .document(FirebaseAuth.getInstance().uid!!)
                                        .collection("Basket")
                                        .document()
                                        .set(hash)
                                }
                            }
                        }
                }
                database.collection("ListData")
                    .document(FirebaseAuth.getInstance().uid!!)
                    .collection("BasketList")
                    .document()
                    .set(hashMapOf("groupName" to recipeName))

                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fragment", "basket")
                startActivity(intent)
            }
        }
        linkSite.setOnClickListener {
            // 레시피 WebView 링크로 이동
            var intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("link", recipe.link)
            startActivity(intent)
        }
    }

    fun initRecipe(name:String) {
        var title = findViewById<TextView>(R.id.recipe_dialog_content_box_title)
        var image = findViewById<ImageView>(R.id.recipe_dialog_image)
        var time = findViewById<TextView>(R.id.recipe_dialog_content_box_time_text)
        var count = findViewById<TextView>(R.id.recipe_dialog_content_box_count_text)
        var level = findViewById<TextView>(R.id.recipe_dialog_content_box_level_text)
        var ingredients = findViewById<TextView>(R.id.recipe_dialog_ing_box_ingredients)


        var refs = Firebase.firestore
        CoroutineScope(Dispatchers.Main).launch {
            val responseRecipe = refs.collection("Recipes").whereEqualTo("name", name)
                .get()
                .await().toObjects<Recipe>()

            val responseIngredients = refs.collection("ListData")
                .document(FirebaseAuth.getInstance().uid!!)
                .collection("Refrigerator")
                .get()
                .await()
                .toMutableList()
                .map { it.get("ingredientname") }

            recipe = responseRecipe[0]

            recipe.apply {
                title.text = this.name
                time.text = this.time + "분"
                level.text = this.level
                count.text = (ingredient as List<String>).size.toString() + "가지"
                Glide.with(activityThis).load(icon).into(image)
            }
            var str = ""
            ingredientList.clear()

            var setUserIng:Set<String> = responseIngredients.toSet() as Set<String>
            var setRecipeIng = (recipe.ingredient as MutableList<String>).toSet()
            var textPosition = arrayListOf<ArrayList<Int>>()
            ingredientList = (setRecipeIng - setUserIng).toMutableList()
            val intersectSize = setUserIng.intersect(setRecipeIng).size

            (setUserIng.intersect(setRecipeIng)).forEach {
                textPosition.add(arrayListOf(str.length,str.length + it.toString().length))
                str += it.toString() + " / "
            }
            (setRecipeIng - setUserIng).forEach {
                textPosition.add(arrayListOf(str.length,str.length + it.toString().length))
                str += it.toString() + " / "
            }
            str = str.substring(0, str.length - 3)
            ingredients.text = str
            ingredients.movementMethod = LinkMovementMethod.getInstance()
            var span: Spannable = ingredients.text as Spannable
            // 각 텍스트를 터치했을 때 색이 변경되는 이벤트는 movementMethod를 공부해보자.
            // https://developer.android.com/reference/android/text/method/MovementMethod
            // https://stackoverflow.com/questions/16007147/how-to-get-rid-of-the-underline-in-a-spannable-string-with-a-clickable-object
            // ingredientList는 현재 없는 재료만 들어 있는 상태이므로, 클릭 한 재료의 추가 삭제도 구현해주어야 함
            textPosition.forEachIndexed { index, textP ->
                span.setSpan(object : ClickableSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                    override fun onClick(widget: View) {
                        Log.d("testtest", "click : ${index}")
                    }
                }, textP[0], textP[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                if(index > intersectSize - 1) {
                    span.setSpan(
                        ForegroundColorSpan(activityThis.getColor(R.color.orange_300)),
                        textP[0],
                        textP[1],
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    span.setSpan(
                        ForegroundColorSpan(activityThis.getColor(R.color.grey_1000)),
                        textP[0],
                        textP[1],
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

    }
}