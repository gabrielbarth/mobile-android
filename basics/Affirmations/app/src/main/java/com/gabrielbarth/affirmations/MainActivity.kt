package com.gabrielbarth.affirmations

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.gabrielbarth.affirmations.adapter.ItemAdapter
import com.gabrielbarth.affirmations.data.Datasource

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>( R.id.recycler_view )
        val myDataset = Datasource().loadAffirmation()

        recyclerView.adapter = ItemAdapter( this, myDataset )
    }
}