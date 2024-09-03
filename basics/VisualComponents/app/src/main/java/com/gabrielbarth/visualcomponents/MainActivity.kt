package com.gabrielbarth.visualcomponents

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var rgSexo : RadioGroup
    private lateinit var rbMasculino : RadioButton
    private lateinit var rbFeminino : RadioButton

    private lateinit var main : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rgSexo = findViewById(R.id.rgSexo)
        rbMasculino = findViewById(R.id.rbMasculino)
        rbFeminino = findViewById(R.id.rbFeminino)

        main = findViewById(R.id.main)


        rgSexo.setOnCheckedChangeListener {_, checkedId ->
           Snackbar.make(main, "Componente selecionado", Snackbar.LENGTH_SHORT).show()
        }
    }

    fun btTestComponentOnClick(view: View) {
        if( rbMasculino.isChecked ){
            val snackbar: Snackbar = Snackbar.make(main, "Masculino selecionado", Snackbar.LENGTH_SHORT)

            snackbar.setBackgroundTint(Color.GRAY)
            snackbar.setAction("Cancelar") {
                snackbar.dismiss()
            }

            snackbar.show()
        }
    }
}