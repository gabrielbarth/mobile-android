package com.gabrielbarth.localdatabase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gabrielbarth.localdatabase.databinding.ActivityMainBinding
import com.gabrielbarth.localdatabase.database.DatabaseHandler
import com.gabrielbarth.localdatabase.entity.Cadastro

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListener()

        banco = DatabaseHandler(this)

        println( "onCreate() executed" )
    }

    private fun setButtonListener() {
        binding.btIncluir.setOnClickListener {
            btIncluirOnClick()
        }

        binding.btAlterar.setOnClickListener {
            btAlterarOnClick()
        }

        binding.btExcluir.setOnClickListener {
            btExcluirOnClick()
        }

        binding.btPesquisar.setOnClickListener {
            btPesquisarOnClick()
        }

        binding.btListar.setOnClickListener {
            btPListarOnClick()
        }
    }

    private fun btIncluirOnClick() {
        banco.insert(
            Cadastro(
                0,
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )
        )
        Toast.makeText(this, "Sucesso!!!", Toast.LENGTH_LONG).show()
    }

    private fun btAlterarOnClick() {
        banco.update(
            Cadastro(
                binding.etCod.text.toString().toInt(),
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )
        )
        Toast.makeText(this, "Sucesso!!!", Toast.LENGTH_LONG).show()
    }

    private fun btExcluirOnClick() {
        banco.delete(binding.etCod.text.toString().toInt())
        Toast.makeText(this, "Sucesso!!!", Toast.LENGTH_LONG).show()
    }

    private fun btPesquisarOnClick() {
        val registro = banco.find(binding.etCod.text.toString().toInt())

        if (registro != null) {
            binding.etNome.setText(registro.nome)
            binding.etTelefone.setText(registro.telefone)
        } else {
            Toast.makeText(this, "Registro não encontrado", Toast.LENGTH_LONG).show()
        }
    }

    private fun btPListarOnClick() {
        /*
        val registro = banco.list()

        var saida = StringBuilder()

        registro.forEach {
            saida.append(it._id)
            saida.append("-")
            saida.append(it.nome)
            saida.append("-")
            saida.append(it.telefone)
            saida.append("\n")
        }

        Toast.makeText( this, saida.toString(), Toast.LENGTH_LONG ).show()*/
        val intent = Intent( this, ListActivity::class.java )
        startActivity( intent )
    }

    override fun onStart() {
        super.onStart()
        println( "onStart() executed" )
    }

    override fun onResume() {
        super.onResume()
        println( "onResume() executed" )
    }

    override fun onPause() {
        super.onPause()
        println( "onPause() executed" )
    }

    override fun onStop() {
        super.onStop()
        println( "onStop() executed" )
    }

    override fun onDestroy() {
        super.onDestroy()
        println( "onDestroy() executed" )
    }

    override fun onRestart() {
        super.onRestart()
        println( "onDestroy() executed" )
    }

}