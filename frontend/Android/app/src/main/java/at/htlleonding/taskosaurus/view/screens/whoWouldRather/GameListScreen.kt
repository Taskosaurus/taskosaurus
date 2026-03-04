package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.data.model.Group
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions
import at.htlleonding.taskosaurus.ui.theme.heading1
import at.htlleonding.taskosaurus.ui.theme.labelText
import at.htlleonding.taskosaurus.view.components.GroupListItem
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    val unansweredGroups by viewModel.unAnsweredGroups.collectAsState()
    val answeredGroups by viewModel.answeredGroups.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scanner = remember { GmsBarcodeScanning.getClient(context) }

    val invalidQrMsg = stringResource(R.string.scanner_invalid_qr)
    val scanFailedMsg = stringResource(R.string.scanner_failed)

    val startQrScanner = {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val raw = barcode.rawValue ?: ""
                val groupId = raw.substringAfterLast("/").toIntOrNull()
                if (groupId != null) onGroupClick(groupId)
                else scope.launch { snackbarHostState.showSnackbar(invalidQrMsg) }
            }
            .addOnFailureListener { scope.launch { snackbarHostState.showSnackbar(scanFailedMsg) } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.list_title), style = dims.heading1()) },
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
                    Text(
                        text = stringResource(R.string.list_error_no_connection),
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (dims.isLandscape && !isTabletSideBar) {
                // Tablet Landscape — 2 columns
                Row(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = dims.screenPaddingH, vertical = dims.screenPaddingV),
                    horizontalArrangement = Arrangement.spacedBy(dims.sectionSpacing)
                ) {
                    GroupSectionBox(
                        title = stringResource(R.string.list_section_unanswered),
                        groups = unansweredGroups,
                        questions = questions,
                        isAnswered = false,
                        emptyText = stringResource(R.string.list_empty_all_done),
                        modifier = Modifier.weight(1f),
                        onGroupClick = onGroupClick
                    )
                    GroupSectionBox(
                        title = stringResource(R.string.list_section_answered),
                        groups = answeredGroups,
                        questions = questions,
                        isAnswered = true,
                        emptyText = stringResource(R.string.list_empty_no_answers),
                        modifier = Modifier.weight(1f),
                        onGroupClick = onGroupClick
                    )
                }
            } else {
                // Portrait or fragment view
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
                                Text(stringResource(R.string.list_empty_no_groups), style = dims.labelText())
                            }
                        }
                    }

                    if (unansweredGroups.isNotEmpty()) {
                        item(key = "header_unanswered") { SectionHeader(stringResource(R.string.list_section_unanswered)) }
                        itemsIndexed(
                            items = unansweredGroups,
                            key = { _, group -> "unanswered_${group.id}" }
                        ) { index, group ->
                            GroupItemWrapper(
                                group = group,
                                questions = questions,
                                isAnswered = false,
                                animationDelay = index * 100,
                                onGroupClick = onGroupClick
                            )
                        }
                    }

                    if (answeredGroups.isNotEmpty()) {
                        item(key = "header_answered") { SectionHeader(stringResource(R.string.list_section_answered)) }
                        itemsIndexed(
                            items = answeredGroups,
                            key = { _, group -> "answered_${group.id}" }
                        ) { index, group ->
                            GroupItemWrapper(
                                group = group,
                                questions = questions,
                                isAnswered = true,
                                animationDelay = index * 100,
                                onGroupClick = onGroupClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
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
    val dims = LocalAppDimensions.current
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
                modifier = Modifier.padding(12.dp)
            )
            if (groups.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(emptyText, style = dims.labelText(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(dims.groupListSpacing),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(
                        items = groups,
                        key = { _, group -> group.id }
                    ) { index, group ->
                        GroupItemWrapper(
                            group = group,
                            questions = questions,
                            isAnswered = isAnswered,
                            animationDelay = index * 100,
                            onGroupClick = onGroupClick
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.GroupItemWrapper(
    group: Group,
    questions: Map<Int, Question>,
    isAnswered: Boolean,
    animationDelay: Int,
    onGroupClick: (Int) -> Unit
) {
    val question = questions[group.id]

    Box() {
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
}

@Composable
fun SectionHeader(text: String) {
    val dims = LocalAppDimensions.current
    Text(
        text = text,
        style = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 2.dp)
    )
}