package br.edu.utfpr.consultacep.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.utfpr.consultacep.shared.data.repository.CepRepository
import br.edu.utfpr.consultacep.shared.data.validator.CepValidator

class CepViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CepViewModel::class.java)) {
            return CepViewModel(
                cepRepository = CepRepository(),
                cepValidator = CepValidator()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}