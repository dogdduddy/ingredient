package com.example.ingredient.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.ingredient.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main2)

		// 카테고리 속 리스트 데이터 Reference Type으로 변경
		var ingredientMapData = mutableMapOf<String,DocumentReference>()
		var recipeMapData = mutableMapOf<String,DocumentReference>()

		var trans:Button = findViewById<Button>(R.id.transBtn)
		var read:Button = findViewById<Button>(R.id.readBtn)


		var ingcategortrec = mutableMapOf<String, MutableList<String>>()

		val ref = FirebaseFirestore.getInstance()
		trans.setOnClickListener {
			ref.collection("ingredients")
				.get()
				.addOnSuccessListener { result ->
					for (document in result) {
						if(ingcategortrec.containsKey(document.get("ingredientcategory").toString())) {
							ingcategortrec[document.get("ingredientcategory")
								.toString()]?.add(document.get("ingredientname").toString())
						}
						else {
							ingcategortrec[document.get("ingredientcategory").toString()] = mutableListOf(document.get("ingredientname").toString())
						}
						ingredientMapData[document.get("ingredientname").toString()] = document.reference
					}
				}
			ref.collection("Recipes")
				.get()
				.addOnSuccessListener { result ->
					for (document in result) {
						recipeMapData[document.get("name").toString()] = document.reference

					}
				}
		}

		var category = listOf("전체", "채소", "육류","수산물","조미료","견과류", "가공/유제품", "기타")
		var foodcategory = listOf("전체", "한식", "중식","양식", "일식",  "아시안", "디저트", "기타")
		// [호박전, 매운어묵꼬치, 김치찌개, 불닭팽이, 김치볶음밥, 김치전, 김치만두, 비빔밥, 스콘, 마들렌, 땅콩버터쿠키, 쭈꾸미볶음, 까르보나라, 칼국수, 두부전골, 계란간장밥, 호떡]
		val foodcategoryrec = listOf(
			listOf("호박전", "매운어묵꼬치", "김치찌개", "불닭팽이", "김치볶음밥", "김치전", "김치만두", "비빔밥", "스콘", "마들렌", "땅콩버터쿠키", "쭈꾸미볶음", "까르보나라", "칼국수", "두부전골", "계란간장밥", "호떡"),
			listOf("호박전", "매운어묵꼬치", "김치찌개", "불닭팽이", "김치볶음밥","김치전", "김치만두", "비빔밥","쭈꾸미볶음", "칼국수", "계란간장밥", "호떡"),
			listOf(),
			listOf("까르보나라"),
			listOf("두부전골"),
			listOf(),
			listOf("스콘", "마들렌", "땅콩버터쿠키", "호떡"),
			listOf()
			)


		read.setOnClickListener {
			ref.collection("Category")
				.get()
				.addOnSuccessListener { result ->
					result.forEachIndexed { index, document ->
						var temp = mutableListOf<DocumentReference>()
						if(category[index] == "전체") {
							temp = ingredientMapData.values.toMutableList()
						}
						else {
							ingcategortrec[category[index]]?.forEach {
								temp.add(ingredientMapData[it]!!)
							}
						}
						document.reference.set(
							mapOf(
								"categoryid" to index,
								"categoryname" to category[index],
								"ingredientlist" to temp
							))
					}
				}
			/*
			foodcategory.forEachIndexed { index, s ->
				var temp = mutableListOf<DocumentReference>()
				foodcategoryrec[index].forEach {
					temp.add(recipeMapData[it]!!)
				}
				Log.d("test", "foodca : ${temp}")
				var mapData = mapOf(
					"categoryid" to index,
					"categoryname" to s,
					"recipes" to  temp
				)
				ref.collection("FoodCategory")
					.document()
					.set(mapData)
			}

			 */
		}
	}
}