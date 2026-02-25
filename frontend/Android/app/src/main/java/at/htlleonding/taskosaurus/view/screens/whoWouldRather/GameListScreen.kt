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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.Group
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions
import at.htlleonding.taskosaurus.ui.theme.heading1
import at.htlleonding.taskosaurus.ui.theme.labelText
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
    val dims = LocalAppDimensions.current
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
                val raw = barcode.rawValue ?: ""
                val groupId = raw.substringAfterLast("/").toIntOrNull()
                if (groupId != null) onGroupClick(groupId)
                else scope.launch { snackbarHostState.showSnackbar("Ungültiger QR-Code") }
            }
            .addOnFailureListener { scope.launch { snackbarHostState.showSnackbar("Scan fehlgeschlagen") } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gruppen", style = dims.heading1()) },
                windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { startQrScanner() }, modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Filled.QrCodeScanner, null,
                    modifier = if (dims.isTablet) Modifier.size(dims.fabIconSize) else Modifier)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())) {
            if (!hasConnection) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text("Keine Verbindung zum Server", modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }

            if (dims.isLandscape && !isTabletSideBar) {
                // Tablet Landscape — zwei Spalten nebeneinander
                Row(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = dims.screenPaddingH, vertical = dims.screenPaddingV),
                    horizontalArrangement = Arrangement.spacedBy(dims.sectionSpacing)
                ) {
                    GroupSectionBox("Nicht beantwortet", unansweredGroups, questions, false,
                        "Alles erledigt!", Modifier.weight(1f), dims, onGroupClick)
                    GroupSectionBox("Beantwortet", answeredGroups, questions, true,
                        "Noch keine Antworten.", Modifier.weight(1f), dims, onGroupClick)
                }
            } else {
                // Portrait oder SideBar — Liste untereinander
                // groupListSpacing = Gap zwischen den einzelnen Karten!
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(dims.groupListSpacing),
                    contentPadding = PaddingValues(
                        horizontal = if (dims.isTablet) 20.dp else 12.dp,
                        vertical = if (dims.isTablet) 16.dp else 10.dp
                    )
                ) {
                    if (unansweredGroups.isEmpty() && answeredGroups.isEmpty()) {
                        item {
                            Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Keine Gruppen gefunden", style = dims.labelText())
                            }
                        }
                    }
                    if (unansweredGroups.isNotEmpty()) {
                        item { SectionHeader("Nicht beantwortet", dims) }
                        itemsIndexed(unansweredGroups) { index, group ->
                            GroupItemWrapper(group, questions, false, index * 200, onGroupClick)
                        }
                    }
                    if (answeredGroups.isNotEmpty()) {
                        item { SectionHeader("Beantwortet", dims) }
                        itemsIndexed(answeredGroups) { index, group ->
                            GroupItemWrapper(group, questions, true, index * 200, onGroupClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupSectionBox(
    title: String, groups: List<Group>, questions: Map<Int, Question>,
    isAnswered: Boolean, emptyText: String, modifier: Modifier,
    dims: at.htlleonding.taskosaurus.ui.theme.AppDimensions, onGroupClick: (Int) -> Unit
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                title,
                style = if (dims.isTablet) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(
                    start = dims.screenPaddingH + 4.dp, end = dims.screenPaddingH + 4.dp,
                    top = if (dims.isTablet) 12.dp else 8.dp, bottom = if (dims.isTablet) 6.dp else 4.dp
                )
            )
            if (groups.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(emptyText, style = dims.labelText(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                // Gap zwischen den Karten in der Spalten-Ansicht
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = if (dims.isTablet) 12.dp else 8.dp),
                    verticalArrangement = Arrangement.spacedBy(dims.groupListSpacing),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(groups) { index, group ->
                        GroupItemWrapper(group, questions, isAnswered, index * 200, onGroupClick)
                    }
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
fun SectionHeader(text: String, dims: at.htlleonding.taskosaurus.ui.theme.AppDimensions) {
    Text(
        text = text,
        style = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 2.dp)
    )
}
