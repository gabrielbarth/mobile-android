package com.gabrielbarth.contacts.data

interface ContactsObserver {
    fun onUpdate(updatedContacts: List<Contact>)
}