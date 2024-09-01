package com.gabrielbarth.localdatabase.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.gabrielbarth.localdatabase.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val database = Firebase.firestore

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME} (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT ) ")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABLE_NAME}")
        onCreate(db)
    }

    fun insert(cadastro: Cadastro) {
        val register = hashMapOf(
            "_id" to cadastro._id,
            "nome" to cadastro.nome,
            "telefone" to cadastro.telefone
        )

        database.collection("cadastro")
            .document(cadastro._id.toString())
            .set(register)
            .addOnSuccessListener {
                println("Sucesso")
            }
            .addOnFailureListener { e ->
                println("Sucesso${e.message}")
            }
    }

    fun update(cadastro: Cadastro) {
        val register = hashMapOf(
            "_id" to cadastro._id,
            "nome" to cadastro.nome,
            "telefone" to cadastro.telefone
        )

        database.collection("cadastro")
            .document(cadastro._id.toString())
            .set(register)
            .addOnSuccessListener {
                println("Sucesso")
            }
            .addOnFailureListener { e ->
                println("Sucesso${e.message}")
            }
    }

    fun delete(id: Int) {
        database.collection("cadastro")
            .document(id.toString())
            .delete()
            .addOnSuccessListener {
                println("Sucesso")
            }
            .addOnFailureListener { e ->
                println("Sucesso${e.message}")
            }
    }

    fun find(id: Int): Cadastro? {
        database.collection("cadastro")
            .whereEqualTo("_id", id)
            .get()
            .addOnSuccessListener { result ->
                val register = result.documents.get(0)

                val registerData = Cadastro(
                    register.data?.get("_id").toString().toInt(),
                    register.data?.get("nome").toString(),
                    register.data?.get("telefone").toString()
                )
            }
            .addOnFailureListener { e ->
                println("Erro${e.message}")
            }

        return null
    }

    fun list(): MutableList<Cadastro> {
        var registers = mutableListOf<Cadastro>()

        database.collection("cadastro")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val register = Cadastro(
                        document.data.get("_id").toString().toInt(),
                        document.data.get("nome").toString(),
                        document.data.get("telefone").toString()
                    )
                    registers.add(register)
                }
            }
            .addOnFailureListener { e ->
                println("Error: ${e.message}")
            }
        return registers
    }

    fun cursorList(): Cursor {
        val db = this.writableDatabase

        val registro = db.query(
            "cadastro",
            null,
            null,
            null,
            null,
            null,
            null
        )

        return registro
    }


    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "cadastro"
        private const val COD = 0
        private const val NOME = 1
        private const val TELEFONE = 2
    }

}