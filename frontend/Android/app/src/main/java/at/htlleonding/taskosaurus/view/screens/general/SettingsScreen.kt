package at.htlleonding.taskosaurus.view.screens.general

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource // NEU: Import für Localization
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.R // NEU: Import deiner Resource-Klasse
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun SettingsScreen(
    viewModel: ViewModel = viewModel(),
    onLogout: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val currentPlayer by viewModel.player.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    val isTabletPortrait = configuration.screenWidthDp >= 600 && !isLandscape

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text(stringResource(R.string.logout_dialog_title)) }, // GEÄNDERT: Localization
            text = { Text(stringResource(R.string.logout_dialog_confirm)) }, // GEÄNDERT: Localization
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text(stringResource(R.string.logout_button), color = MaterialTheme.colorScheme.error) // GEÄNDERT
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(stringResource(R.string.cancel_button)) // GEÄNDERT: Localization
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    horizontal = if (isTabletPortrait) 48.dp else 24.dp,
                    vertical = if (isTabletPortrait) 28.dp else 20.dp
                )
        ) {
            Text(
                text = stringResource(R.string.settings_title), // GEÄNDERT: Localization
                style = when {
                    isTabletPortrait -> MaterialTheme.typography.displaySmall
                    isLandscape -> MaterialTheme.typography.headlineLarge
                    else -> MaterialTheme.typography.headlineMedium
                },
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (isTabletPortrait) 48.dp else 32.dp),
                textAlign = if (isLandscape) TextAlign.Start else TextAlign.Center
            )

            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(0.4f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileHeader(currentPlayer, false)
                    }

                    Column(modifier = Modifier.weight(0.6f)) {
                        SettingsActionsCard(false) { showLogoutDialog = true }
                        Spacer(modifier = Modifier.weight(1f))
                        VersionText(false)
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .then(if (isTabletPortrait) Modifier.widthIn(max = 520.dp).fillMaxWidth() else Modifier.fillMaxWidth()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileHeader(currentPlayer, isTabletPortrait)
                        Spacer(modifier = Modifier.height(if (isTabletPortrait) 64.dp else 48.dp))
                        SettingsActionsCard(isTabletPortrait) { showLogoutDialog = true }
                        Spacer(modifier = Modifier.weight(1f))
                        VersionText(isTabletPortrait)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(player: at.htlleonding.taskosaurus.data.model.Player?, isTabletPortrait: Boolean) {
    player?.let {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(if (isTabletPortrait) 140.dp else 100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = it.name.take(1).uppercase(),
                        style = if (isTabletPortrait) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(if (isTabletPortrait) 28.dp else 16.dp))
            Text(
                // GEÄNDERT: Localization mit Platzhalter für den Namen
                text = stringResource(R.string.hello_player, it.name),
                style = if (isTabletPortrait) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(if (isTabletPortrait) 20.dp else 12.dp))
            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.manage_account), // GEÄNDERT: Localization
                    style = if (isTabletPortrait) MaterialTheme.typography.titleSmall else MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun SettingsActionsCard(isTabletPortrait: Boolean, onLogoutClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTabletPortrait) 28.dp else 24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = if (isTabletPortrait) 8.dp else 4.dp)) {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(R.string.logout_button), // GEÄNDERT: Localization
                        style = if (isTabletPortrait) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
                    )
                },
                leadingContent = { Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error) },
                modifier = Modifier.clickable { onLogoutClick() },
                colors = ListItemDefaults.colors(
                    headlineColor = MaterialTheme.colorScheme.error,
                    containerColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun VersionText(isTabletPortrait: Boolean = false) {
    Text(
        // GEÄNDERT: Localization mit Versions-String als Parameter
        text = stringResource(R.string.version_text, "1.0.0"),
        style = if (isTabletPortrait) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        textAlign = TextAlign.Center
    )
}