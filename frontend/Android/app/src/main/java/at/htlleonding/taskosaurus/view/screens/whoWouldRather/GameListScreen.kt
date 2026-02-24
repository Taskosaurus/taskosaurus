package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.Group
import at.htlleonding.taskosaurus.data.model.Question
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    val startQrScanner = {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue = barcode.rawValue ?: ""
                val groupId = rawValue.substringAfterLast("/").toIntOrNull()
                if (groupId != null) onGroupClick(groupId)
                else scope.launch { snackbarHostState.showSnackbar("Ungültiger QR-Code") }
            }
            .addOnFailureListener {
                scope.launch { snackbarHostState.showSnackbar("Scan fehlgeschlagen") }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gruppen") },
                windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { startQrScanner() }, modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            if (!hasConnection) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(text = "Keine Verbindung zum Server", modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GroupSectionBox("Nicht beantwortet", unansweredGroups, questions, false, "Alles erledigt!", Modifier.weight(1f), onGroupClick)
                    GroupSectionBox("Beantwortet", answeredGroups, questions, true, "Noch keine Antworten.", Modifier.weight(1f), onGroupClick)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    if (unansweredGroups.isEmpty() && answeredGroups.isEmpty()) {
                        item { Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) { Text("Keine Gruppen gefunden") } }
                    }
                    if (unansweredGroups.isNotEmpty()) {
                        item { SectionHeader("Nicht beantwortet") }
                        itemsIndexed(unansweredGroups) { index, group -> GroupItemWrapper(group, questions, false, index * 200, onGroupClick) }
                    }
                    if (answeredGroups.isNotEmpty()) {
                        item { SectionHeader("Beantwortet") }
                        itemsIndexed(answeredGroups) { index, group -> GroupItemWrapper(group, questions, true, index * 200, onGroupClick) }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupSectionBox(
    title: String,
    groups: List<Group>,
    questions: Map<Int, Question>,
    isAnswered: Boolean,
    emptyText: String,
    modifier: Modifier,
    onGroupClick: (Int) -> Unit
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp))
            if (groups.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = emptyText, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                ) {
                    itemsIndexed(groups) { index, group -> GroupItemWrapper(group, questions, isAnswered, index * 200, onGroupClick) }
                }
            }
        }
    }
}

@Composable
fun GroupItemWrapper(group: Group, questions: Map<Int, Question>, isAnswered: Boolean, animationDelay: Int, onGroupClick: (Int) -> Unit) {
    val question = questions[group.id]
    GroupListItem(
        group = group,
        votedCount = question?.answers?.sumOf { it.count } ?: 0,
        totalCount = group.players?.size ?: 0,
        isAnswered = isAnswered,
        shortenedQuestion = question?.shortenedQuestion,
        currentLeader = if (isAnswered) question?.currentLeader else null,
        leaderVoteCount = if (isAnswered) question?.answers?.maxByOrNull { it.count }?.count ?: 0 else 0,
        animationDelay = animationDelay,
        onClick = { onGroupClick(group.id) }
    )
}

@Composable
fun SectionHeader(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp))
}