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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.PlayerNameDto
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions
import at.htlleonding.taskosaurus.ui.theme.loginTitle
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegisterView(viewModel: ViewModel = viewModel(), onSuccess: () -> Unit) {
    val dims = LocalAppDimensions.current
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val doAuth = { handleAuth(name, password, isLoginMode, viewModel, { isLoading = it }, {}, onSuccess) }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding().safeDrawingPadding()) {
            if (dims.isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(0.4f), horizontalAlignment = Alignment.CenterHorizontally) {
                        AuthBranding(dims, isLandscape = true)
                        Spacer(Modifier.height(24.dp))
                        AuthModeToggle(isLoginMode) { isLoginMode = it }
                    }
                    Card(
                        modifier = Modifier.weight(0.6f).imePadding(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 20.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            AuthInputFields(dims, name, { name = it }, password, { password = it }, isLoginMode, isLoading, doAuth)
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = dims.loginPaddingH)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AuthBranding(dims, isLandscape = false)
                    Spacer(Modifier.height(if (dims.isTablet) 48.dp else 32.dp))
                    AuthModeToggle(isLoginMode) { isLoginMode = it }
                    Spacer(Modifier.height(if (dims.isTablet) 32.dp else 24.dp))
                    AuthInputFields(dims, name, { name = it }, password, { password = it }, isLoginMode, isLoading, doAuth)
                }
            }
        }
    }
}

@Composable
private fun AuthBranding(dims: at.htlleonding.taskosaurus.ui.theme.AppDimensions, isLandscape: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            Icons.Default.Person, null,
            modifier = Modifier.size(dims.loginIconSize),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(if (isLandscape) 8.dp else if (dims.isTablet) 28.dp else 32.dp))
        Text(
            "Willkommen",
            style = dims.loginTitle(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AuthModeToggle(isLoginMode: Boolean, onToggle: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = isLoginMode, onClick = { onToggle(true) },
            label = { Text("Anmelden", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            selected = !isLoginMode, onClick = { onToggle(false) },
            label = { Text("Registrieren", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AuthInputFields(
    dims: at.htlleonding.taskosaurus.ui.theme.AppDimensions,
    name: String, onNameChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    isLoginMode: Boolean, isLoading: Boolean, onAuth: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(if (dims.isTablet) 20.dp else 16.dp)) {
        OutlinedTextField(
            value = name, onValueChange = onNameChange, label = { Text("Benutzername") },
            singleLine = true, enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().heightIn(min = dims.loginFieldHeight),
            shape = RoundedCornerShape(12.dp),
            textStyle = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        OutlinedTextField(
            value = password, onValueChange = onPasswordChange, label = { Text("Passwort") },
            singleLine = true, enabled = !isLoading,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAuth() }),
            modifier = Modifier.fillMaxWidth().heightIn(min = dims.loginFieldHeight),
            shape = RoundedCornerShape(12.dp),
            textStyle = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(if (dims.isTablet) 8.dp else 8.dp))
        Button(
            onClick = onAuth, enabled = name.isNotBlank() && password.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth().height(dims.loginButtonHeight),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text(
                if (isLoginMode) "Anmelden" else "Registrieren",
                style = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun handleAuth(
    name: String, password: String, isLoginMode: Boolean, viewModel: ViewModel,
    onLoading: (Boolean) -> Unit, onError: (String) -> Unit, onSuccess: () -> Unit
) {
    onLoading(true)
    val dto = PlayerNameDto(name, password)
    if (isLoginMode) viewModel.loadPlayerFromDto(dto, { onLoading(false); onSuccess() }, { onLoading(false); onError(it) })
    else viewModel.createAndSaveUser(dto, { onLoading(false); onSuccess() }, { onLoading(false); onError(it) })
}
