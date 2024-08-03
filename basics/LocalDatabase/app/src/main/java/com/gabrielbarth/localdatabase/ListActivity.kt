package com.gabrielbarth.localdatabase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.gabrielbarth.localdatabase.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_list)

        binding = ActivityListBinding.inflate( layoutInflater )
        setContentView(binding.root)

        val registros = listOf<String>( "Brasil", "Argentina", "Paraguai", "Uruguai" )

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, registros )
        binding.lvPrincipal.adapter = adapter
    }
}