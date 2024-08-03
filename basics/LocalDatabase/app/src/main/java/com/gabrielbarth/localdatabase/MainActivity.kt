package com.gabrielbarth.localdatabase

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gabrielbarth.localdatabase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var banco : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate( layoutInflater )
        setContentView(binding.root )

        setButtonListener()

        banco = SQLiteDatabase.openOrCreateDatabase(
            this.getDatabasePath( "dbfile.sqlite" ),
            null
        )

        banco.execSQL( "CREATE TABLE IF NOT EXISTS cadastro (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT ) ")
    }

    private fun setButtonListener() {
        binding.btIncluir.setOnClickListener{
            btIncluirOnClick()
        }

        binding.btAlterar.setOnClickListener{
            btAlterarOnClick()
        }

        binding.btExcluir.setOnClickListener{
            btExcluirOnClick()
        }

        binding.btPesquisar.setOnClickListener{
            btPesquisarOnClick()
        }

        binding.btListar.setOnClickListener{
            btPListarOnClick()
        }
    }

    private fun btIncluirOnClick() {

        val registro = ContentValues()
        registro.put( "nome", binding.etNome.text.toString() )
        registro.put( "telefone", binding.etTelefone.text.toString() )

        banco.insert( "cadastro", null, registro )

        Toast.makeText( this, "Sucesso!!!", Toast.LENGTH_LONG ).show()

    }

    private fun btAlterarOnClick() {
        val registro = ContentValues()
        registro.put( "nome", binding.etNome.text.toString() )
        registro.put( "telefone", binding.etTelefone.text.toString() )

        banco.update( "cadastro", registro, "_id=${binding.etCod.text.toString()}", null )

        Toast.makeText( this, "Sucesso!!!", Toast.LENGTH_LONG ).show()
    }

    private fun btExcluirOnClick() {
        banco.delete( "cadastro", "_id=${binding.etCod.text.toString()}", null)
        Toast.makeText( this, "Sucesso!!!", Toast.LENGTH_LONG ).show()
    }

    private fun btPesquisarOnClick() {
        val registro =  banco.query(
            "cadastro",
            null,
            "_id=${binding.etCod.text.toString()}",
            null,
            null,
            null,
            null
        )

        if ( registro.moveToNext() ) {
            binding.etNome.setText( registro.getString(Companion.NOME) )
            binding.etTelefone.setText( registro.getString(Companion.TELEFONE) )
        } else {
            Toast.makeText( this, "Registro não encontrado", Toast.LENGTH_LONG ).show()
        }



    }

    private fun btPListarOnClick() {
        val registro =  banco.query(
            "cadastro",
            null,
            null,
            null,
            null,
            null,
            null
        )

        var saida = StringBuilder()

        while ( registro.moveToNext() ) {
            saida.append( registro.getInt(Companion.COD) )
            saida.append( "-" )
            saida.append( registro.getString(Companion.NOME) )
            saida.append( "-" )
            saida.append( registro.getString(Companion.TELEFONE) )
            saida.append( "\n" )
        }

        Toast.makeText( this, saida.toString(), Toast.LENGTH_LONG ).show()
    }

    companion object {
        private const val COD = 0
        private const val NOME = 1
        private const val TELEFONE = 2
    }


}