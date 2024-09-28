package com.gabrielbarth.contacts.ui.contact.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gabrielbarth.contacts.R
import com.gabrielbarth.contacts.data.Contact
import com.gabrielbarth.contacts.data.ContactDatasource
import com.gabrielbarth.contacts.data.generateContacts
import com.gabrielbarth.contacts.data.groupByInitial
import com.gabrielbarth.contacts.ui.theme.ContactsTheme
import com.gabrielbarth.contacts.ui.utils.composables.ContactAvatar
import com.gabrielbarth.contacts.ui.utils.composables.DefaultErrorContent
import com.gabrielbarth.contacts.ui.utils.composables.DefaultLoadingContent
import com.gabrielbarth.contacts.ui.utils.composables.FavoriteIconButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onAddPressed: () -> Unit,
    onContactPressed: (Contact) -> Unit
) {
    var isInitialComposition: Boolean by rememberSaveable { mutableStateOf(true) }
    var uiState: ContactsListUiState by remember { mutableStateOf(ContactsListUiState()) }

    val loadContacts: () -> Unit = {
        uiState = uiState.copy(
            isLoading = true,
            hasError = false
        )
        coroutineScope.launch {
            delay(2000)
            val contacts: List<Contact> = ContactDatasource.instance.findAll()
            uiState = uiState.copy(
                contacts = contacts.groupByInitial(),
                isLoading = false
            )
        }
    }

    val toggleFavorite: (Contact) -> Unit = { contact ->
        val updatedContact = contact.copy(isFavorite = !contact.isFavorite)
        ContactDatasource.instance.save(updatedContact)
        val contacts: List<Contact> = ContactDatasource.instance.findAll()
        uiState = uiState.copy(contacts = contacts.groupByInitial())
    }

    if (isInitialComposition) {
        loadContacts()
        isInitialComposition = false
    }

    val contentModifier = modifier.fillMaxSize()
    if (uiState.isLoading) {
        DefaultLoadingContent(
            modifier = contentModifier,
            text = stringResource(R.string.loading_contacts)
        )
    } else if (uiState.hasError) {
        DefaultErrorContent(
            modifier = contentModifier,
            onTryAgainPressed = loadContacts
        )
    } else {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    onRefreshPressed = loadContacts
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = onAddPressed) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Adicionar"
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Novo contato")
                }
            }
        ) { paddingValues ->
            val defaultModifier = Modifier.padding(paddingValues)
            if (uiState.contacts.isEmpty()) {
                EmptyList(modifier = defaultModifier)
            } else {
                List(
                    modifier = defaultModifier,
                    contacts = uiState.contacts,
                    onFavoritePressed = toggleFavorite,
                    onContactPressed = onContactPressed
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    modifier: Modifier = Modifier,
    onRefreshPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(stringResource(R.string.contacts))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = onRefreshPressed) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.refresh)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview() {
    ContactsTheme {
        AppBar(onRefreshPressed = {})
    }
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.no_data),
            contentDescription = stringResource(R.string.no_data)
        )
        Text(
            text = stringResource(R.string.no_data),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.no_data_hint),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyListPreview() {
    ContactsTheme {
        EmptyList()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun List(
    modifier: Modifier = Modifier,
    contacts: Map<String, List<Contact>>,
    onFavoritePressed: (Contact) -> Unit,
    onContactPressed: (Contact) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        contacts.forEach { (initial, contacts) ->
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Text(
                        text = initial,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .padding(start = 16.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            items(contacts) { contact ->
                ContactListItem(
                    contact = contact,
                    onFavoritePressed = onFavoritePressed,
                    onContactPressed = onContactPressed
                )
            }
        }
    }
}

@Composable
private fun ContactListItem(
    modifier: Modifier = Modifier,
    contact: Contact,
    onFavoritePressed: (Contact) -> Unit,
    onContactPressed: (Contact) -> Unit
) {
    ListItem(
        modifier = modifier.clickable { onContactPressed(contact) },
        headlineContent = { Text(contact.fullName) },
        leadingContent = {
            ContactAvatar(
                firstName = contact.firstName,
                lastName = contact.lastName
            )
        },
        trailingContent = {
            FavoriteIconButton(
                isFavorite = contact.isFavorite,
                onPressed = { onFavoritePressed(contact) }
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun ListPreview() {
    ContactsTheme {
        List(
            contacts = generateContacts().groupByInitial(),
            onFavoritePressed = {},
            onContactPressed = {}
        )
    }
}