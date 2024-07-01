package com.example.bestfuel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvFuelA : TextView
    private lateinit var btnSelectFuelA : Button
    private lateinit var tvFuelB : TextView
    private lateinit var btnSelectFuelB : Button
    private lateinit var btnCompare : Button
    private lateinit var etPriceA : EditText
    private lateinit var etConsumptionA : EditText
    private lateinit var etPriceB : EditText
    private lateinit var etConsumptionB : EditText
    private lateinit var tvResult : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvFuelA  = findViewById(R.id.tvFuelA)
        btnSelectFuelA = findViewById(R.id.btnSelectFuelA)
        tvFuelB  = findViewById(R.id.tvFuelB)
        btnSelectFuelB = findViewById(R.id.btnSelectFuelB)
        etPriceA = findViewById(R.id.etPriceA)
        etConsumptionA = findViewById(R.id.etConsumptionA)
        etPriceB = findViewById(R.id.etPriceB)
        etConsumptionB = findViewById(R.id.etConsumptionB)

        btnCompare = findViewById(R.id.btnCompare)
        tvResult = findViewById(R.id.tvResult)

        val getResultFuelA = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedItem = result.data?.getStringExtra("selectedItem")
                tvFuelA.text = ("Combustível A: $selectedItem")
                btnSelectFuelA.text = "Alterar"
            }
        }

        val getResultFuelB = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedItem = result.data?.getStringExtra("selectedItem")
                tvFuelB.text = ("Combustível B: $selectedItem")
                btnSelectFuelB.text = "Alterar"
            }
        }

        btnSelectFuelA.setOnClickListener {
            onSelectFuel(getResultFuelA)
        }
        btnSelectFuelB.setOnClickListener {
            onSelectFuel(getResultFuelB)
        }
        btnCompare.setOnClickListener {
            onCompare()
        }

    }

     private fun onSelectFuel(getResult: ActivityResultLauncher<Intent>){
        val intent = Intent(this, SelectFuelActivity::class.java)
        getResult.launch(intent)
    }

    private fun onCompare(){
        var resultA =   etConsumptionA.text.toString().toDouble() / etPriceA.text.toString().toDouble()
        var resultB = etConsumptionB.text.toString().toDouble() / etPriceB.text.toString().toDouble()

        val bestFuel =  if (resultA >= resultB) {
            "${tvFuelA.text} é a melhor opção!"
        } else {
            "${tvFuelB.text} é a melhor opção!"
        }

        tvResult.text = bestFuel
    }
}