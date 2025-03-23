package br.edu.utfpr.consultacep

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.utfpr.consultacep.databinding.ActivityMainBinding
import br.edu.utfpr.consultacep.shared.Greeting
import br.edu.utfpr.consultacep.ui.CepViewModel
import br.edu.utfpr.consultacep.ui.CepViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CepViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "Hello from shared module: ${Greeting().greet()}")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editCep = binding.editCep
        val btnBuscar = binding.btnBuscar
        val progress = binding.progress
        val txtCep = binding.txtCep
        val txtLogradouro = binding.txtLogradouro
        val txtBairro = binding.txtBairro
        val txtLocalidade = binding.txtLocalidade
        val txtUf = binding.txtUf

        viewModel = ViewModelProvider(this, CepViewModelFactory())[CepViewModel::class.java]

        viewModel.formState.observe(this@MainActivity, Observer {
            val formState = it ?: return@Observer

            btnBuscar.isEnabled = formState.isDataValid
            btnBuscar.visibility = if (formState.isLoading) View.GONE else View.VISIBLE
            progress.visibility = if (formState.isLoading) View.VISIBLE else View.GONE

            txtCep.text = getString(R.string.cep, formState.endereco.cep)
            txtLogradouro.text = getString(R.string.logradouro, formState.endereco.logradouro)
            txtBairro.text = getString(R.string.bairro, formState.endereco.bairro)
            txtLocalidade.text = getString(R.string.localidade, formState.endereco.localidade)
            txtUf.text = getString(R.string.uf, formState.endereco.uf)
        })

        editCep.apply {
            addTextChangedListener(object : TextWatcher {
                private var isUpdating = false

                override fun afterTextChanged(editable: Editable?) {
                    if (isUpdating || editable == null) return

                    isUpdating = true
                    val filteredValue = editable.toString().replace("\\D".toRegex(), "")
                    var formattedValue = ""

                    filteredValue.mapIndexed { index, char ->
                        if (index == 5) {
                            formattedValue += "-"
                        }
                        if (index <= 7) {
                            formattedValue += char
                        }
                    }

                    editCep.setText(formattedValue)
                    editCep.setSelection(formattedValue.length)
                    isUpdating = false

                    viewModel.onCepChanged(formattedValue)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.buscarCep(editCep.text.toString())
                }
                false
            }
        }

        btnBuscar.setOnClickListener {
            val cep = editCep.text.toString()
            viewModel.buscarCep(cep)
        }
    }
}