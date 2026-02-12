package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.view.components.GroupListItem
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(
    viewModel: ViewModel = viewModel(),
    onGroupClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val hasConnection by viewModel.hasConnection.collectAsState()
    val questions by viewModel.latestQuestions.collectAsState()

    val unansweredGroups = viewModel.unAnsweredGroups
    val answeredGroups = viewModel.answeredGroups

    val snackbarHostState = remember { SnackbarHostState() }

    val scanner = remember { GmsBarcodeScanning.getClient(context) }

    val startQrScanner = {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue = barcode.rawValue ?: ""
                val groupId = rawValue.substringAfterLast("/").toIntOrNull()

                if (groupId != null) {
                    onGroupClick(groupId)
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Ungültiger QR-Code für Taskosaurus")
                    }
                }
            }
            .addOnFailureListener {
                scope.launch {
                    snackbarHostState.showSnackbar("Scan abgebrochen oder fehlgeschlagen")
                }
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gruppen",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                startQrScanner()
            }) {
                Icon(
                    imageVector = Icons.Filled.QrCodeScanner,
                    contentDescription = "QR-Code scannen"
                )
            }
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
                        shortenedQuestion = question?.shortenedQuestion,
                        currentLeader = null,
                        leaderVoteCount = 0,
                        onClick = { onGroupClick(group.id) }
                    )
                }
            }

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

                    val leaderVoteCount = question?.answers
                        ?.maxByOrNull { it.count }
                        ?.count ?: 0

                    GroupListItem(
                        group = group,
                        votedCount = votedCount,
                        totalCount = totalCount,
                        isAnswered = true,
                        shortenedQuestion = question?.shortenedQuestion,
                        currentLeader = question?.currentLeader,
                        leaderVoteCount = leaderVoteCount,
                        onClick = { onGroupClick(group.id) }
                    )
                }
            }
        }
    }
}