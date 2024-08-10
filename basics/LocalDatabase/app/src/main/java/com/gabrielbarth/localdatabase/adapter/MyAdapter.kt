package com.gabrielbarth.localdatabase.adapter;

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.gabrielbarth.localdatabase.R
import com.gabrielbarth.localdatabase.entity.Cadastro
import com.gabrielbarth.localdatabase.MainActivity

class MyAdapter(val context: Context, val cursor: Cursor) : BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)
        val cadastro = Cadastro(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2)
        )
        return cadastro
    }

    override fun getItemId(position: Int): Long {

        cursor.moveToPosition(position)
        return cursor.getLong(0)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.list_element, null)

        val tvNomeElementoLista = v.findViewById<TextView>(R.id.tvNomeElementoLista)
        val tvTelefoneElementoLista = v.findViewById<TextView>(R.id.tvTelefoneElementoLista)
        val btEditarElementoLista = v.findViewById<ImageButton>( R.id.btEditarElementoLista )

        cursor.moveToPosition(position)

        tvNomeElementoLista.setText(cursor.getString(1))
        tvTelefoneElementoLista.setText(cursor.getString(2))

        btEditarElementoLista.setOnClickListener{
            cursor.moveToPosition( position )

            val intent = Intent( context, MainActivity::class.java)

            intent.putExtra( "cod", cursor.getInt( 0 ) )
            intent.putExtra( "nome", cursor.getString( 1 ) )
            intent.putExtra( "telefone", cursor.getString( 2 ) )

            context.startActivity( intent )
        }

        return v
    }



}