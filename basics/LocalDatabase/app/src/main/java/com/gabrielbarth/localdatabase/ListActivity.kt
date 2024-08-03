package com.gabrielbarth.localdatabase

import android.R
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.SimpleCursorAdapter
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

        val adapter = SimpleCursorAdapter(
            this,
            R.layout.simple_list_item_2,
            registros,
            arrayOf( "nome", "telefone" ),
            intArrayOf( R.id.text1, R.id.text2 ),
            0
        ) //meio de campo

        binding.lvPrincipal.adapter = adapter //destino
    }
}