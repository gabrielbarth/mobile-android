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

class MyAdapter(val context: Context, val registers: MutableList<Cadastro>) : BaseAdapter() {
    override fun getCount(): Int {
        return registers.size
    }

    override fun getItem(position: Int): Any {
        val cadastro = registers.get( position )
        return cadastro
    }

    override fun getItemId(position: Int): Long {
        val cadastro = registers.get( position )
        return cadastro._id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.list_element, null)

        val tvNomeElementoLista = v.findViewById<TextView>(R.id.tvNomeElementoLista)
        val tvTelefoneElementoLista = v.findViewById<TextView>(R.id.tvTelefoneElementoLista)
        val btEditarElementoLista = v.findViewById<ImageButton>( R.id.btEditarElementoLista )

        val register = registers.get( position )

        tvNomeElementoLista.setText( register.nome )
        tvTelefoneElementoLista.setText( register.telefone )

        btEditarElementoLista.setOnClickListener{
            val register = registers.get( position )

            val intent = Intent( context, MainActivity::class.java)

            intent.putExtra( "cod", register._id )
            intent.putExtra( "nome", register.nome )
            intent.putExtra( "telefone", register.telefone )

            context.startActivity( intent )
        }

        return v
    }


}