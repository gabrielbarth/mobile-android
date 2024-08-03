package com.gabrielbarth.localdatabase.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.gabrielbarth.localdatabase.entity.Cadastro

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME} (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT ) ")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABLE_NAME}")
        onCreate(db)
    }

    fun insert(cadastro: Cadastro) {
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put("nome", cadastro.nome)
        registro.put("telefone", cadastro.telefone)
        db.insert(TABLE_NAME, null, registro)
    }

    fun update(cadastro: Cadastro) {
        val db = this.writableDatabase
        val registro = ContentValues()
        registro.put("nome", cadastro.nome)
        registro.put("telefone", cadastro.telefone)
        db.update(TABLE_NAME, registro, "_id=${cadastro._id}", null)
    }

    fun delete(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "_id=${id}", null)
    }

    fun find(id: Int): Cadastro? {
        val db = this.writableDatabase
        val registro = db.query(
            "cadastro",
            null,
            "_id=${id}",
            null,
            null,
            null,
            null
        )
        if (registro.moveToNext()) {
            val cadastro = Cadastro(
                id,
                registro.getString(NOME),
                registro.getString(TELEFONE),
            )
            return cadastro
        } else {
            return null
        }
    }

    fun list(): MutableList<Cadastro> {
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

        var registros = mutableListOf<Cadastro>()

        while (registro.moveToNext()) {
            val cadastro = Cadastro(
                registro.getInt(COD),
                registro.getString(NOME),
                registro.getString(TELEFONE),
            )

            registros.add(cadastro)
        }

        return registros
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