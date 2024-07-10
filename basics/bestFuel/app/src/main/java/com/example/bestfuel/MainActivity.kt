package com.example.bestfuel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bestfuel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getResultFuelA =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val selectedItem = result.data?.getStringExtra("selectedItem")
                    binding.tvFuelA.text = ("Combustível A: $selectedItem")
                    binding.btnSelectFuelA.text = "Alterar"
                }
            }

        val getResultFuelB =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val selectedItem = result.data?.getStringExtra("selectedItem")
                    binding.tvFuelB.text = ("Combustível B: $selectedItem")
                    binding.btnSelectFuelB.text = "Alterar"
                }
            }

        binding.btnSelectFuelA.setOnClickListener {
            onSelectFuel(getResultFuelA)
        }
        binding.btnSelectFuelB.setOnClickListener {
            onSelectFuel(getResultFuelB)
        }
        binding.btnCompare.setOnClickListener {
            onCompare()
        }

        if(savedInstanceState != null){
            val result =   savedInstanceState.getString("result")
            binding.tvResult.text = getString(R.string.tv_result, result)
        } else {
            binding.tvResult.text = getString(R.string.tv_result, "")
        }

    }

    private fun onSelectFuel(getResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(this, SelectFuelActivity::class.java)
        getResult.launch(intent)
    }

    private fun showValidationMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        binding.tvResult.text = ""
    }

    private fun validateFields(): Boolean {
        val inputTextPriceA = binding.etPriceA.text.toString().toDoubleOrNull() ?: null
        val inputTextPriceB = binding.etPriceB.text.toString().toDoubleOrNull() ?: null
        val inputTextConsumptionA = binding.etConsumptionA.text.toString().toDoubleOrNull() ?: null
        val inputTextConsumptionB = binding.etConsumptionB.text.toString().toDoubleOrNull() ?: null

        if (inputTextPriceA == null) {
            showValidationMessage("Preencha o Valor Litro do combustível A!")
            return false

        } else if (inputTextPriceB == null) {
            showValidationMessage("Preencha o Valor Litro do combustível B!")
            return false

        } else if (inputTextConsumptionA == null ) {
            showValidationMessage("Preencha o consumo do combustível A!")
            return false

        } else if (inputTextConsumptionB == null ) {
            showValidationMessage("Preencha o consumo do combustível B!")
            return false
        }

        return true
    }

    private fun validateFuels(): Boolean {
        val textFuelA = binding.tvFuelA.text.toString()
        val textFuelB = binding.tvFuelB.text.toString()

        if(textFuelA == "Combustível A") {
            showValidationMessage("Preencha qual é o combustível A!")
            return false
        }

        if(textFuelB == "Combustível B") {
            showValidationMessage("Preencha qual é o combustível B!")
            return false
        }

        if(textFuelA.split(" ").last() == textFuelB.split(" ").last()) {
            showValidationMessage("Selecione combustível diferentes para A e B!")
            return false
        }

        return true
    }

    private fun onCompare() {

        if(!validateFields() || !validateFuels()) {
            return
        }


        var resultA =
            binding.etConsumptionA.text.toString().toDouble() / binding.etPriceA.text.toString().toDouble()
        var resultB =
            binding.etConsumptionB.text.toString().toDouble() / binding.etPriceB.text.toString().toDouble()

        val bestFuel = if (resultA > resultB) {
            "${binding.tvFuelA.text} é a melhor opção!"
        } else if (resultA < resultB) {
            "${binding.tvFuelB.text} é a melhor opção!"
        } else {
            "As duas opções são equivalentes!"
        }

        binding.tvResult.text = bestFuel
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("result", binding.tvResult.text.toString())
    }
}