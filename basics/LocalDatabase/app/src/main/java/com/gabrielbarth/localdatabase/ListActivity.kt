package com.gabrielbarth.localdatabase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gabrielbarth.localdatabase.adapter.MyAdapter
import com.gabrielbarth.localdatabase.database.DatabaseHandler
import com.gabrielbarth.localdatabase.databinding.ActivityListBinding
import com.gabrielbarth.localdatabase.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler(this)

        binding.btIncluir.setOnClickListener {
            btIncluirOnClick()
        }

    }

    private fun btIncluirOnClick() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        val banco = Firebase.firestore

        banco.collection("cadastro")
            .get()
            .addOnSuccessListener { result ->
                var registerList = mutableListOf<Cadastro>()
                for (document in result) {
                    val cadastro = Cadastro(
                        document.data.get("_id").toString().toInt(),
                        document.data.get("nome").toString(),
                        document.data.get("telefone").toString()
                    )
                    registerList.add(cadastro)
                }

                val adapter = MyAdapter(this, registerList)
                binding.lvPrincipal.adapter = adapter //destino

            }
            .addOnFailureListener { e ->
                println("Erro${e.message}")
            }


        //val registros = banco.cursorList() //fonte  tem que ter selecionado o campo _id, com exatamente este nome
    }
}