package com.example.ingredient

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SearchAdapter (
    private val recipeList: MutableList<Array<Any>>,
    private val context: Context,
    private val firestoreDB: FirebaseFirestore)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent!!.context).inflate(R.layout.item_recipe, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipeList[position]

            when (position % 5) {
                0 -> holder.food.setImageResource(R.drawable.buckwheat)
                1 -> holder.food.setImageResource(R.drawable.bibim)
                2 -> holder.food.setImageResource(R.drawable.ham)
                3 -> holder.food.setImageResource(R.drawable.salad)
                4 -> holder.food.setImageResource(R.drawable.steak)
            }
            holder!!.title.text = recipe[0].toString()
            holder.content.text = "재료 : " + recipe[1]
            holder.time.text = recipe[2].toString()
            holder.bind(recipe)
        }

        override fun getItemCount(): Int {
            return recipeList.size
        }

        inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
            internal var title: TextView
            internal var content: TextView
            internal var time: TextView
            internal var food: ImageView

            fun bind(item: Array<Any>) {
                /* 바인딩 된 아이템을 클릭했을 때의 이벤트
                itemView.setOnClickListener {
                    Intent(context, ContentActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        // 받아온 값을 다시 List 형태로 변경
                        val cook = item[2] as List<String>

                        putExtra("id", item[0].toString())
                        putExtra("content", item[1].toString())
                        // Extra로 넘기기 위해 Array 타입으로 변경
                        putExtra("cook", cook.toTypedArray())
                        putExtra("time", item[3].toString())

                    }.run { context.startActivity(this) }
                }

                 */
            }

            init {
                title = view.findViewById(R.id.findTitle)
                content = view.findViewById(R.id.findContent)
                time = view.findViewById(R.id.findTime)
                food = view.findViewById(R.id.foodimg)

            }
        }
        fun nullItem(position: Int) {
            recipeList.removeAt((position))
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }