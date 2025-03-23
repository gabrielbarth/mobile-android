package br.edu.utfpr.consultacep.ui

import br.edu.utfpr.consultacep.shared.data.model.Endereco

data class CepFormState(
    val cep: String = "",
    val isDataValid: Boolean = false,
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val endereco: Endereco = Endereco()
)