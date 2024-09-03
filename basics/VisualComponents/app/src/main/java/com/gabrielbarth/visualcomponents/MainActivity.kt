package com.gabrielbarth.visualcomponents

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.View
import android.view.ViewParent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

//    private lateinit var rgSexo : RadioGroup
//    private lateinit var rbMasculino : RadioButton
//    private lateinit var rbFeminino : RadioButton
//
//    private lateinit var cbMasculino : CheckBox
//    private lateinit var cbFeminino : CheckBox

//    private lateinit var dpNasc: DatePicker

//    private lateinit var etCidade : AutoCompleteTextView

//    private lateinit var spCidade : Spinner

    private lateinit var barra: ProgressBar

    private lateinit var main: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        rgSexo = findViewById(R.id.rgSexo)
//        rbMasculino = findViewById(R.id.rbMasculino)
//        rbFeminino = findViewById(R.id.rbFeminino)

//        cbMasculino = findViewById(R.id.cbMasculino)
//        cbFeminino = findViewById(R.id.cbFeminino)

//        dpNasc = findViewById(R.id.dpNasc)

//        etCidade = findViewById(R.id.etCidade)

//        spCidade = findViewById(R.id.spCidade)

        barra = findViewById(R.id.barra)

        main = findViewById(R.id.main)

//        cbFeminino.setOnCheckedChangeListener {_, checkedId ->
//           Snackbar.make(main, "Feminino selecionado", Snackbar.LENGTH_SHORT).show()
//        }

        // spinner ou auto complete
        val cities: List<String> =
            listOf<String>("Pato Branco", "Coronel vivida", "Marmeleiro", "Mariopolis", "Marechal")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities)
//        etCidade.setAdapter(adapter)
//        spCidade.setAdapter(adapter)
//
//        spCidade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                Toast.makeText(baseContext, spCidade.selectedItem.toString(), Toast.LENGTH_SHORT).show()
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
    }

    fun btTestComponentOnClick(view: View) {
//        if( cbMasculino.isChecked ){
//            val snackbar: Snackbar = Snackbar.make(main, "Masculino selecionado", Snackbar.LENGTH_SHORT)
//
//            snackbar.setBackgroundTint(Color.GRAY)
//            snackbar.setAction("Cancelar") {
//                snackbar.dismiss()
//            }
//
//            snackbar.show()
//        }

        // dpNasc em tela
//        val dateFormatted = "${dpNasc.dayOfMonth}/${dpNasc.month + 1}/${dpNasc.year}"
//        Snackbar.make(main, dateFormatted, Snackbar.LENGTH_SHORT).show()

        // dpNasc em modal
//        val dateDialog = DatePickerDialog(
//            this,
//            {_, selectedYear, selectedMonth, selectedDay ->
//                val dateFormatted = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
//                Snackbar.make(main, dateFormatted, Snackbar.LENGTH_SHORT).show()
//            },
//            2024, 7, 25
//        )
//        dateDialog.show()


        // Snackbar.make(main, spCidade.selectedItem.toString(), Snackbar.LENGTH_SHORT).show()

        Thread {
            for (i in 0..100) {
                barra.progress = i
                Thread.sleep(100)
            }
        }.start()


    }
}