package at.htlleonding.taskosaurus.view.screens.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.PlayerNameDto
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegisterView(
    viewModel: ViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    val isTabletPortrait = configuration.screenWidthDp >= 600 && !isLandscape

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .safeDrawingPadding()
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Linke Seite: 40% Platz
                    Column(
                        modifier = Modifier.weight(0.4f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AuthBranding(isLandscape = true)
                        Spacer(modifier = Modifier.height(24.dp))
                        AuthModeToggle(isLoginMode) { isLoginMode = it }
                    }

                    // Rechte Seite: 60% Platz
                    Card(
                        modifier = Modifier
                            .weight(0.6f)
                            .imePadding(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 20.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            AuthInputFields(
                                name = name,
                                onNameChange = { name = it },
                                password = password,
                                onPasswordChange = { password = it },
                                isLoginMode = isLoginMode,
                                isLoading = isLoading,
                                isLandscape = true,
                                isTabletPortrait = false,
                                onAuth = {
                                    handleAuth(name, password, isLoginMode, viewModel, { isLoading = it }, {}, onSuccess)
                                }
                            )
                        }
                    }
                }
            } else {
                // Portrait Modus
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = if (isTabletPortrait) 64.dp else 24.dp)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AuthBranding(false, isTabletPortrait)
                    Spacer(modifier = Modifier.height(if (isTabletPortrait) 48.dp else 32.dp))
                    AuthModeToggle(isLoginMode) { isLoginMode = it }
                    Spacer(modifier = Modifier.height(if (isTabletPortrait) 32.dp else 24.dp))
                    AuthInputFields(name, {name=it}, password, {password=it}, isLoginMode, isLoading, false, isTabletPortrait) {
                        handleAuth(name, password, isLoginMode, viewModel, {isLoading=it}, {}, onSuccess)
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthBranding(isLandscape: Boolean, isTabletPortrait: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(when { isLandscape -> 60.dp; isTabletPortrait -> 120.dp; else -> 100.dp }),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(when { isLandscape -> 8.dp; isTabletPortrait -> 28.dp; else -> 32.dp }))
        Text(
            text = "Willkommen",
            style = when {
                isLandscape -> MaterialTheme.typography.headlineSmall
                isTabletPortrait -> MaterialTheme.typography.displayMedium
                else -> MaterialTheme.typography.displaySmall
            },
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthModeToggle(isLoginMode: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = isLoginMode,
            onClick = { onToggle(true) },
            label = { Text("Anmelden", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            selected = !isLoginMode,
            onClick = { onToggle(false) },
            label = { Text("Registrieren", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AuthInputFields(
    name: String,
    onNameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoginMode: Boolean,
    isLoading: Boolean,
    isLandscape: Boolean,
    isTabletPortrait: Boolean = false,
    onAuth: () -> Unit
) {
    val fieldHeight = when { isTabletPortrait -> 68.dp; isLandscape -> 48.dp; else -> 56.dp }
    val buttonHeight = when { isTabletPortrait -> 68.dp; isLandscape -> 48.dp; else -> 56.dp }
    val spacing = when { isTabletPortrait -> 20.dp; isLandscape -> 8.dp; else -> 16.dp }
    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Benutzername") },
            singleLine = true,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().heightIn(min = fieldHeight),
            shape = RoundedCornerShape(12.dp),
            textStyle = if (isTabletPortrait) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Passwort") },
            singleLine = true,
            enabled = !isLoading,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAuth() }),
            modifier = Modifier.fillMaxWidth().heightIn(min = fieldHeight),
            shape = RoundedCornerShape(12.dp),
            textStyle = if (isTabletPortrait) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(if (isTabletPortrait) 8.dp else if (isLandscape) 4.dp else 8.dp))
        Button(
            onClick = onAuth,
            enabled = name.isNotBlank() && password.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth().height(buttonHeight),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text(
                    if (isLoginMode) "Anmelden" else "Registrieren",
                    style = if (isTabletPortrait) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun handleAuth(
    name: String,
    password: String,
    isLoginMode: Boolean,
    viewModel: ViewModel,
    onLoading: (Boolean) -> Unit,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    onLoading(true)
    val dto = PlayerNameDto(name, password)
    if (isLoginMode) {
        viewModel.loadPlayerFromDto(dto, { onLoading(false); onSuccess() }, { onLoading(false); onError(it) })
    } else {
        viewModel.createAndSaveUser(dto, { onLoading(false); onSuccess() }, { onLoading(false); onError(it) })
    }
}