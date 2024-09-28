package com.gabrielbarth.contacts.ui.contact.details

import com.gabrielbarth.contacts.data.Contact

data class ContactDetailsUiState(
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val contact: Contact = Contact()
)