package com.gabrielbarth.localdatabase

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gabrielbarth.localdatabase.adapter.MyAdapter
import com.gabrielbarth.localdatabase.databinding.ActivityListBinding
import com.gabrielbarth.localdatabase.database.DatabaseHandler

class ListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListBinding
    private lateinit var banco : DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate( layoutInflater )
        setContentView(binding.root)

        banco = DatabaseHandler( this )

        val registros : Cursor = banco.cursorList() //fonte  tem que ter selecionado o campo _id, com exatamente este nome

        val adapter = MyAdapter( this, registros )

        binding.lvPrincipal.adapter = adapter //destino
    }
}