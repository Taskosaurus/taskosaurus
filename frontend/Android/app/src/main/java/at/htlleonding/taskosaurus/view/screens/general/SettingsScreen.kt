package at.htlleonding.taskosaurus.view.screens.general

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.data.model.Player
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions
import at.htlleonding.taskosaurus.ui.theme.settingsName
import at.htlleonding.taskosaurus.ui.theme.settingsTitle
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun SettingsScreen(
    viewModel: ViewModel = viewModel(),
    onLogout: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val dims = LocalAppDimensions.current
    val currentPlayer by viewModel.player.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text(stringResource(R.string.logout_dialog_title)) },
            text = { Text(stringResource(R.string.logout_dialog_text)) },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text(stringResource(R.string.logout_button), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))) {
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding()
                .padding(horizontal = dims.settingsPaddingH, vertical = dims.settingsPaddingV)
        ) {
            if (dims.isLandscape) {
                LandscapeSettingsLayout(currentPlayer, dims) { showLogoutDialog = true }
            } else {
                Text(
                    stringResource(R.string.settings_title),
                    style = dims.settingsTitle(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                    textAlign = TextAlign.Center
                )

                if (dims.isTablet) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Column(
                            modifier = Modifier.fillMaxHeight().widthIn(max = dims.settingsMaxWidth)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ProfileHeader(currentPlayer, dims)
                            Spacer(modifier = Modifier.height(64.dp))
                            LogoutCard(dims) { showLogoutDialog = true }
                            Spacer(modifier = Modifier.weight(1f))
                            VersionText(dims)
                        }
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ProfileHeader(currentPlayer, dims)
                        Spacer(modifier = Modifier.height(48.dp))
                        LogoutCard(dims) { showLogoutDialog = true }
                        Spacer(modifier = Modifier.weight(1f))
                        VersionText(dims)
                    }
                }
            }
        }
    }
}

@Composable
private fun LandscapeSettingsLayout(player: Player?, dims: AppDimensions, onLogoutClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {

        Card(
            modifier = Modifier.width(260.dp).fillMaxHeight(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(modifier = Modifier.size(dims.settingsAvatarSize), shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            player?.name?.take(1)?.uppercase() ?: "?",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text(player?.name ?: "", style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Surface(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50.dp)) {
                    Text(stringResource(R.string.active_account), modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(R.string.settings_general), style = dims.settingsTitle(), fontWeight = FontWeight.Bold)

            SettingsActionCard(
                icon = Icons.Default.Person,
                title = stringResource(R.string.manage_account),
                subtitle = stringResource(R.string.profile_subtitle),
                iconTint = MaterialTheme.colorScheme.primary,
                onClick = {}
            )

            SettingsActionCard(
                icon = Icons.Default.Shield,
                title = stringResource(R.string.privacy_title),
                subtitle = stringResource(R.string.privacy_subtitle),
                iconTint = MaterialTheme.colorScheme.tertiary,
                onClick = {}
            )

            SettingsActionCard(
                icon = Icons.Default.Info,
                title = stringResource(R.string.about_app_title),
                subtitle = stringResource(R.string.about_app_subtitle),
                iconTint = MaterialTheme.colorScheme.secondary,
                onClick = {}
            )

            Spacer(Modifier.weight(1f))
        }

        Column(modifier = Modifier.width(220.dp).fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onLogoutClick() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Logout, null,
                        tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(stringResource(R.string.logout_button), style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center)
                    Spacer(Modifier.height(4.dp))
                    Text(stringResource(R.string.logout_secure_hint), style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f), textAlign = TextAlign.Center)
                }
            }

            VersionText(dims)
        }
    }
}

@Composable
private fun SettingsActionCard(
    icon: ImageVector, title: String, subtitle: String,
    iconTint: Color, onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(modifier = Modifier.size(44.dp), shape = RoundedCornerShape(12.dp),
                color = iconTint.copy(alpha = 0.15f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = iconTint, modifier = Modifier.size(24.dp))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}


@Composable
private fun ProfileHeader(player: Player?, dims: AppDimensions) {
    player?.let {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(modifier = Modifier.size(dims.settingsAvatarSize), shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer) {
                Box(contentAlignment = Alignment.Center) {
                    Text(it.name.take(1).uppercase(),
                        style = if (dims.isTablet) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(if (dims.isTablet) 28.dp else 16.dp))

            Text(stringResource(R.string.hello_user, it.name), style = dims.settingsName(), fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(if (dims.isTablet) 20.dp else 12.dp))
            OutlinedButton(onClick = {}, shape = RoundedCornerShape(50.dp)) {
                Text(stringResource(R.string.manage_account),
                    style = if (dims.isTablet) MaterialTheme.typography.titleSmall else MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun LogoutCard(dims: AppDimensions, onLogoutClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (dims.isTablet) 28.dp else 24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = if (dims.isTablet) 8.dp else 4.dp)) {
            ListItem(
                headlineContent = {
                    Text(stringResource(R.string.logout_button),
                        style = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge)
                },
                leadingContent = { Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error) },
                modifier = Modifier.clickable { onLogoutClick() },
                colors = ListItemDefaults.colors(headlineColor = MaterialTheme.colorScheme.error, containerColor = Color.Transparent)
            )
        }
    }
}

@Composable
private fun VersionText(dims: AppDimensions) {
    Text(stringResource(R.string.version_format),
        style = if (dims.isTablet) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), textAlign = TextAlign.Center)
}