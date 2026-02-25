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
    onGroupClick: (Int) -> Unit,
    isTabletSideBar: Boolean = false
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val hasConnection by viewModel.hasConnection.collectAsState()
    val groups by viewModel.groups.collectAsState()
    val questions by viewModel.latestQuestions.collectAsState()

    // Filterung der Gruppen nach Status
    val unansweredGroups = remember(groups, questions) {
        groups.filter { g -> questions[g.id]?.answered == false }
    }
    val answeredGroups = remember(groups, questions) {
        groups.filter { g -> questions[g.id]?.answered == true }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scanner = remember { GmsBarcodeScanning.getClient(context) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp &&!isTabletSideBar
    val isTabletPortrait = configuration.screenWidthDp >= 600 && !isLandscape

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
                Icon(Icons.Filled.QrCodeScanner, contentDescription = "Gruppe beitreten")
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
                    Text(
                        text = "Keine Verbindung zum Server",
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (isLandscape) {
                // --- HANDY QUERFORMAT ---
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Nur anzeigen wenn nicht leer
                    if (unansweredGroups.isNotEmpty()) {
                        GroupSectionBox("Nicht beantwortet", unansweredGroups, questions, false, Modifier.weight(1f), onGroupClick)
                    }
                    // Beantwortet nimmt den restlichen Platz ein
                    GroupSectionBox("Beantwortet", answeredGroups, questions, true, Modifier.weight(1f), onGroupClick)
                }
            } else {
                // --- TABLET SIDEBAR / HOCHFORMAT ---
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(if (isTabletPortrait) 12.dp else 16.dp),
                    contentPadding = PaddingValues(if (isTabletPortrait) 24.dp else 16.dp)
                ) {
                    if (unansweredGroups.isEmpty() && answeredGroups.isEmpty()) {
                        item {
                            Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    "Keine Gruppen gefunden",
                                    style = if (isTabletPortrait) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    if (unansweredGroups.isNotEmpty()) {
                        item {
                            GroupSectionBoxStandalone("Nicht beantwortet", unansweredGroups, questions, false, onGroupClick)
                        }
                    }
                    if (answeredGroups.isNotEmpty()) {
                        item {
                            GroupSectionBoxStandalone(
                                title = "Beantwortet",
                                groups = answeredGroups,
                                questions = questions,
                                isAnswered = true,
                                onGroupClick = onGroupClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupSectionBoxStandalone(
    title: String,
    groups: List<Group>,
    questions: Map<Int, Question>,
    isAnswered: Boolean,
    onGroupClick: (Int) -> Unit,
    emptyText: String = ""
) {
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        // Border wurde hier entfernt
    ) {
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 8.dp)
            )

            if (groups.isEmpty() && emptyText.isNotEmpty()) {
                Box(Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.Center) {
                    Text(text = emptyText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    groups.forEachIndexed { index, group ->
                        GroupItemWrapper(group, questions, isAnswered, index * 100, onGroupClick)
                        if (index < groups.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        }
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
    modifier: Modifier,
    onGroupClick: (Int) -> Unit
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 4.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                itemsIndexed(groups) { index, group ->
                    GroupItemWrapper(group, questions, isAnswered, index * 200, onGroupClick)
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
fun SectionHeader(text: String, isTabletPortrait: Boolean = false) {
    Text(
        text = text,
        style = if (isTabletPortrait) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
    )
}