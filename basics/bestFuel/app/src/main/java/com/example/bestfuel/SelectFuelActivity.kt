package com.example.bestfuel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SelectFuelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_fuel)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf("Álcool", "Gasolina")
        recyclerView.adapter = MyAdapter(items) { selectedItem ->
            val resultIntent = Intent()
            resultIntent.putExtra("selectedItem", selectedItem)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    class MyAdapter(
        private val items: List<String>,
        private val itemClickListener: (String) -> Unit
    ) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.textView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fuel_option_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = items[position]
            holder.itemView.setOnClickListener {
                itemClickListener(items[position])
            }
        }

        override fun getItemCount() = items.size
    }
}