package com.gabrielbarth.contacts.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gabrielbarth.contacts.ui.contact.details.ContactDetailsScreen
import com.gabrielbarth.contacts.ui.contact.list.ContactListScreen

private object Screens {
    const val CONTACTS_LIST = "contactList"
    const val CONTACT_DETAILS = "contactDetails"
}

private object Arguments {
    const val CONTACT_ID = "contactId"
}

private object Routes {
    const val CONTACTS_LIST = Screens.CONTACTS_LIST
    const val CONTACT_DETAILS = "${Screens.CONTACT_DETAILS}/{${Arguments.CONTACT_ID}}"
}

@Composable
fun AppEntryPoint(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.CONTACTS_LIST
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Routes.CONTACTS_LIST) {
            ContactListScreen(
                onAddPressed = {},
                onContactPressed = { contact ->
                    navController.navigate("${Screens.CONTACT_DETAILS}/${contact.id}")
                }
            )
        }
        composable(
            route = Routes.CONTACT_DETAILS,
            arguments = listOf(
                navArgument(name = Arguments.CONTACT_ID) {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val contactId: Int = navBackStackEntry.arguments?.getInt(Arguments.CONTACT_ID) ?: 0
            ContactDetailsScreen(
                contactId = contactId
            )
        }
    }
}
