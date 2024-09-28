package com.gabrielbarth.contacts.ui.contact.list

import com.gabrielbarth.contacts.data.Contact

data class ContactsListUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val contacts: Map<String, List<Contact>> = mapOf()
)