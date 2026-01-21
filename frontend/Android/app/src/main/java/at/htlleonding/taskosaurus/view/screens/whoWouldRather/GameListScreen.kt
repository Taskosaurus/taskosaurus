package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.view.components.GroupListItem
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(
    viewModel: ViewModel = viewModel(),
    onGroupClick: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val hasConnection by viewModel.hasConnection.collectAsState()
    val questions by viewModel.latestQuestions.collectAsState()

    // Use computed properties from ViewModel
    val unansweredGroups = viewModel.unAnsweredGroups
    val answeredGroups = viewModel.answeredGroups

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Gruppen",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Zurück"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // TODO: insert QR Code Scanner
                    }) {
                        Icon(
                            imageVector = Icons.Filled.QrCodeScanner,
                            contentDescription = "QR-Code scannen"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            if (!hasConnection) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Keine Verbindung zum Server",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            // Nicht beantwortet Section
            if (unansweredGroups.isNotEmpty()) {
                item {
                    Text(
                        text = "Nicht beantwortet",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
                    )
                }

                items(unansweredGroups) { group ->
                    val question = questions[group.id]
                    val votedCount = question?.answers?.sumOf { it.count } ?: 0
                    val totalCount = group.players?.size ?: 0

                    GroupListItem(
                        group = group,
                        votedCount = votedCount,
                        totalCount = totalCount,
                        isAnswered = false,
                        onClick = { onGroupClick(group.id) }
                    )
                }
            }

            // Beantwortet Section
            if (answeredGroups.isNotEmpty()) {
                item {
                    Text(
                        text = "Beantwortet",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp, top = 16.dp, bottom = 4.dp)
                    )
                }

                items(answeredGroups) { group ->
                    val question = questions[group.id]
                    val votedCount = question?.answers?.sumOf { it.count } ?: 0
                    val totalCount = group.players?.size ?: 0

                    GroupListItem(
                        group = group,
                        votedCount = votedCount,
                        totalCount = totalCount,
                        isAnswered = true,
                        onClick = { onGroupClick(group.id) }
                    )
                }
            }
        }
    }
}