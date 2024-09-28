package com.gabrielbarth.contacts.ui.contact.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrielbarth.contacts.R
import com.gabrielbarth.contacts.data.ContactTypeEnum
import com.gabrielbarth.contacts.ui.theme.ContactsTheme
import com.gabrielbarth.contacts.ui.utils.composables.ContactAvatar
import com.gabrielbarth.contacts.ui.utils.composables.DefaultErrorContent
import com.gabrielbarth.contacts.ui.utils.composables.DefaultLoadingContent
import com.gabrielbarth.contacts.utils.format
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun ContactFormScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactFormViewModel = viewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackPressed: () -> Unit,
    onContactSaved: () -> Unit
) {
    LaunchedEffect(viewModel.uiState.contactSaved) {
        if (viewModel.uiState.contactSaved) {
            onContactSaved()
        }
    }
    val context = LocalContext.current
    LaunchedEffect(snackbarHostState, viewModel.uiState.hasErrorSaving) {
        if (viewModel.uiState.hasErrorSaving) {
            snackbarHostState.showSnackbar(
                context.getString(R.string.error_saving)
            )
        }
    }
    val contentModifier: Modifier = modifier.fillMaxSize()
    if (viewModel.uiState.isLoading) {
        DefaultLoadingContent(modifier = contentModifier)
    } else if (viewModel.uiState.hasErrorLoading) {
        DefaultErrorContent(
            modifier = contentModifier,
            onTryAgainPressed = viewModel::loadContact
        )
    } else {
        Scaffold(
            modifier = contentModifier,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                AppBar(
                    isNewContact = viewModel.uiState.isNewContact,
                    onBackPressed = onBackPressed,
                    isSaving = viewModel.uiState.isSaving,
                    onSavePressed = viewModel::save
                )
            }
        ) { paddingValues ->
            FormContent(
                modifier = Modifier.padding(paddingValues),
                isSaving = viewModel.uiState.isSaving,
                firstName = viewModel.uiState.firstName,
                lastName = viewModel.uiState.lastName,
                phone = viewModel.uiState.phone,
                email = viewModel.uiState.email,
                isFavorite = viewModel.uiState.isFavorite,
                birthDate = viewModel.uiState.birthDate,
                type = viewModel.uiState.type,
                onFirstNameChanged = viewModel::onFirstNameChanged,
                onLastNameChanged = viewModel::onLastNameChanged,
                onPhoneChanged = viewModel::onPhoneChanged,
                onEmailChanged = viewModel::onEmailChanged,
                onTypeChanged = viewModel::onTypeChanged,
                onIsFavoriteChanged = viewModel::onIsFavoriteChanged,
                onBirthDateChanged = viewModel::onBirthDateChanged
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    modifier: Modifier = Modifier,
    isNewContact: Boolean,
    onBackPressed: () -> Unit,
    isSaving: Boolean,
    onSavePressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            val title = stringResource(
                if (isNewContact) R.string.new_contact
                else R.string.edit_contact
            )
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(16.dp),
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(onClick = onSavePressed) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(R.string.save)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview() {
    ContactsTheme {
        AppBar(
            isNewContact = true,
            onBackPressed = {},
            isSaving = false,
            onSavePressed = {}
        )
    }
}

@Composable
private fun FormTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    label: String,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    errorMessageCode: Int = 0,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Unspecified,
    keyboardImeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val hasError = errorMessageCode > 0
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChanged,
            label = { Text(label) },
            maxLines = 1,
            enabled = enabled,
            readOnly = readOnly,
            isError = hasError,
            keyboardOptions = KeyboardOptions(
                capitalization = keyboardCapitalization,
                imeAction = keyboardImeAction,
                keyboardType = keyboardType
            ),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon
        )
        if (hasError) {
            Text(
                text = stringResource(errorMessageCode),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormTextFieldPreview() {
    ContactsTheme {
        FormTextField(
            modifier = Modifier.padding(20.dp),
            value = "Teste",
            onValueChanged = {},
            label = "Nome"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormTextFieldErrorPreview() {
    ContactsTheme {
        FormTextField(
            modifier = Modifier.padding(20.dp),
            value = "Teste",
            onValueChanged = {},
            label = "Nome",
            errorMessageCode = R.string.loading_error
        )
    }
}

@Composable
private fun FormCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    enabled: Boolean = true,
    label: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckChanged,
            enabled = enabled
        )
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
private fun FormCheckboxPreview() {
    ContactsTheme {
        FormCheckbox(
            modifier = Modifier.padding(20.dp),
            checked = false,
            onCheckChanged = {},
            label = "Favorito"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormCheckboxCheckedPreview() {
    ContactsTheme {
        FormCheckbox(
            modifier = Modifier.padding(20.dp),
            checked = true,
            onCheckChanged = {},
            label = "Favorito"
        )
    }
}


@Composable
fun FormRadioButton(
    modifier: Modifier = Modifier,
    value: ContactTypeEnum,
    groupValue: ContactTypeEnum,
    onValueChanged: (ContactTypeEnum) -> Unit,
    enabled: Boolean = true,
    label: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = value == groupValue,
            onClick = { onValueChanged(value) },
            enabled = enabled
        )
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
private fun FormRadioButtonPreview() {
    ContactsTheme {
        FormRadioButton(
            modifier = Modifier.padding(20.dp),
            value = ContactTypeEnum.PERSONAL,
            groupValue = ContactTypeEnum.PROFESSIONAL,
            onValueChanged = {},
            label = "Pessoal"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormRadioButtonSelectedPreview() {
    ContactsTheme {
        FormRadioButton(
            modifier = Modifier.padding(20.dp),
            value = ContactTypeEnum.PERSONAL,
            groupValue = ContactTypeEnum.PERSONAL,
            onValueChanged = {},
            label = "Pessoal"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDatePicker(
    modifier: Modifier = Modifier,
    label: String,
    value: LocalDate,
    onValueChanged: (LocalDate) -> Unit,
    errorMessageCode: Int = 0,
    enabled: Boolean = true
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    FormTextField(
        modifier = modifier,
        value = value.format(),
        onValueChanged = {},
        label = label,
        readOnly = true,
        enabled = enabled,
        errorMessageCode = errorMessageCode,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = !showDatePicker }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.select_date)
                )
            }
        }
    )
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = !showDatePicker },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant
                            .ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onValueChanged(date)
                    }
                }) {
                    Text(stringResource(R.string.ok))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormDatePickerPreview() {
    ContactsTheme {
        FormDatePicker(
            modifier = Modifier.padding(20.dp),
            label = "Data",
            value = LocalDate.now(),
            onValueChanged = {}
        )
    }
}


@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    isSaving: Boolean,
    firstName: FormField<String>,
    lastName: FormField<String>,
    phone: FormField<String>,
    email: FormField<String>,
    isFavorite: FormField<Boolean>,
    birthDate: FormField<LocalDate>,
    type: FormField<ContactTypeEnum>,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onIsFavoriteChanged: (Boolean) -> Unit,
    onBirthDateChanged: (LocalDate) -> Unit,
    onTypeChanged: (ContactTypeEnum) -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val formTextFieldModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ContactAvatar(
            modifier = Modifier.padding(16.dp),
            firstName = firstName.value,
            lastName = lastName.value,
            size = 150.dp,
            textStyle = MaterialTheme.typography.displayLarge
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = stringResource(R.string.name),
                tint = MaterialTheme.colorScheme.outline
            )
            FormTextField(
                modifier = formTextFieldModifier,
                label = stringResource(R.string.name),
                value = firstName.value,
                errorMessageCode = firstName.errorMessageCode,
                onValueChanged = onFirstNameChanged,
                keyboardCapitalization = KeyboardCapitalization.Words,
                enabled = !isSaving
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = stringResource(R.string.lastName),
                tint = MaterialTheme.colorScheme.background
            )
            FormTextField(
                modifier = formTextFieldModifier,
                label = stringResource(R.string.lastName),
                value = lastName.value,
                errorMessageCode = lastName.errorMessageCode,
                onValueChanged = onLastNameChanged,
                keyboardCapitalization = KeyboardCapitalization.Words,
                enabled = !isSaving
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = stringResource(R.string.phone),
                tint = MaterialTheme.colorScheme.outline
            )
            FormTextField(
                modifier = formTextFieldModifier,
                label = stringResource(R.string.phone),
                value = phone.value,
                errorMessageCode = phone.errorMessageCode,
                onValueChanged = onPhoneChanged,
                keyboardType = KeyboardType.Phone,
                enabled = !isSaving
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Mail,
                contentDescription = stringResource(R.string.email),
                tint = MaterialTheme.colorScheme.outline
            )
            FormTextField(
                modifier = formTextFieldModifier,
                label = stringResource(R.string.email),
                value = email.value,
                errorMessageCode = email.errorMessageCode,
                onValueChanged = onEmailChanged,
                keyboardType = KeyboardType.Email,
                enabled = !isSaving
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = stringResource(R.string.birth_date),
                tint = MaterialTheme.colorScheme.background
            )
            FormDatePicker(
                modifier = formTextFieldModifier,
                label = stringResource(R.string.birth_date),
                value = birthDate.value,
                errorMessageCode = birthDate.errorMessageCode,
                onValueChanged = onBirthDateChanged,
                enabled = !isSaving
            )
        }
        val checkOptionsModifier = Modifier.padding(vertical = 8.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = stringResource(R.string.birth_date),
                tint = MaterialTheme.colorScheme.background
            )
            FormCheckbox(
                modifier = checkOptionsModifier,
                label = stringResource(R.string.favorite_contact),
                checked = isFavorite.value,
                onCheckChanged = onIsFavoriteChanged,
                enabled = !isSaving
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = stringResource(R.string.birth_date),
                tint = MaterialTheme.colorScheme.background
            )
            FormRadioButton(
                modifier = checkOptionsModifier,
                label = stringResource(R.string.personal_contact),
                value = ContactTypeEnum.PERSONAL,
                groupValue = type.value,
                onValueChanged = onTypeChanged,
                enabled = !isSaving
            )
            FormRadioButton(
                modifier = checkOptionsModifier,
                label = stringResource(R.string.professional_contact),
                value = ContactTypeEnum.PROFESSIONAL,
                groupValue = type.value,
                onValueChanged = onTypeChanged,
                enabled = !isSaving
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormContentPreview() {
    ContactsTheme {
        FormContent(
            isSaving = false,
            firstName = FormField(value = "Gabriel"),
            lastName = FormField(value = "Barth"),
            phone = FormField(value = "6799998888"),
            email = FormField(value = ""),
            isFavorite = FormField(value = false),
            birthDate = FormField(value = LocalDate.now()),
            type = FormField(value = ContactTypeEnum.PERSONAL),
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onPhoneChanged = {},
            onEmailChanged = {},
            onTypeChanged = {},
            onIsFavoriteChanged = {},
            onBirthDateChanged = {}
        )
    }
}
