package at.htlleonding.taskosaurus.view.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginRegisterView(
    viewModel: ViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            errorMessage = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Willkommen",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login / Registrieren
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                FilterChip(
                    selected = isLoginMode,
                    onClick = { isLoginMode = true },
                    label = { Text("Anmelden") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = !isLoginMode,
                    onClick = { isLoginMode = false },
                    label = { Text("Registrieren") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(if (isLoginMode) "Spieler-ID eingeben" else "Dein Name")
                },
                singleLine = true,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isLoginMode) KeyboardType.Number else KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        handleAuth(
                            name = name,
                            isLoginMode = isLoginMode,
                            viewModel = viewModel,
                            onLoading = { isLoading = it },
                            onError = { errorMessage = it },
                            onSuccess = onSuccess
                        )
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    handleAuth(
                        name = name,
                        isLoginMode = isLoginMode,
                        viewModel = viewModel,
                        onLoading = { isLoading = it },
                        onError = { errorMessage = it },
                        onSuccess = onSuccess
                    )
                },
                enabled = name.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (isLoginMode) "Anmelden" else "Registrieren",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private fun handleAuth(
    name: String,
    isLoginMode: Boolean,
    viewModel: ViewModel,
    onLoading: (Boolean) -> Unit,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    onLoading(true)

    if (isLoginMode) {
        // Login with ID
        val playerId = name.toIntOrNull()
        if (playerId != null) {
            viewModel.loginWithId(playerId)
            kotlinx.coroutines.GlobalScope.launch {
                kotlinx.coroutines.delay(1000)
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                    onLoading(false)
                    onSuccess()
                }
            }
        } else {
            onLoading(false)
            onError("Ungültige Spieler-ID")
        }
    } else {
        viewModel.createAndSaveUser(
            playerName = name,
            onSuccess = {
                onLoading(false)
                onSuccess()
            },
            onError = { error ->
                onLoading(false)
                onError(error)
            }
        )
    }
}