package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun TitleScreen(
    onOpenGameList: () -> Unit,
    onGameCreated: (Int) -> Unit, // Callback to navigate to the new game
    viewModel: ViewModel = viewModel()
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var groupName by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Illustration
        Surface(
            modifier = Modifier.size(120.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.People,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Wer würde eher?",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Entdecke was deine Freunde wählen würden",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 48.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Play Button
        FilledTonalButton(
            onClick = { onOpenGameList() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp)
        ) {
            Icon(Icons.Default.PlayArrow, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Jetzt spielen")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Create Button - Now opens the Dialog
        OutlinedButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp)
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Spiel erstellen")
        }
    }

    // --- Create Game Dialog ---
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { if (!isSubmitting) showCreateDialog = false },
            title = { Text("Neues Spiel erstellen") },
            text = {
                Column {
                    Text(
                        "Gib deiner Gruppe einen Namen, um zu starten.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        label = { Text("Gruppenname") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSubmitting
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = groupName.isNotBlank() && !isSubmitting,
                    onClick = {
                        isSubmitting = true
                        viewModel.createGroup(
                            name = groupName,
                            onSuccess = { newGroup ->
                                isSubmitting = false
                                showCreateDialog = false
                                onGameCreated(newGroup.id)
                            },
                            onError = {
                                isSubmitting = false
                                // Handle error (e.g., show Toast)
                            }
                        )
                    }
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Erstellen")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCreateDialog = false },
                    enabled = !isSubmitting
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }
}