package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.data.model.*
import at.htlleonding.taskosaurus.ui.theme.*
import at.htlleonding.taskosaurus.view.components.InteractionArea
import at.htlleonding.taskosaurus.view.components.QuestionCard
import at.htlleonding.taskosaurus.view.components.VoteProgressHeader
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    groupId: Int,
    viewModel: ViewModel = viewModel(),
    onNavigateToGroupInfo: (Int) -> Unit,
    isTabletMode: Boolean = false
) {
    val dims = LocalAppDimensions.current

    val groups by viewModel.groups.collectAsState()
    val questions by viewModel.latestQuestions.collectAsState()
    val player by viewModel.player.collectAsState()
    val isReady by viewModel.isReady.collectAsState()

    val group = groups.find { it.id == groupId }
    val question = questions[groupId]

    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val joinSuccessText = stringResource(R.string.game_join_success)

    LaunchedEffect(player, isReady, group) {
        if (isReady && player != null && group == null) {
            viewModel.joinGroup(groupId = groupId, onSuccess = {
                scope.launch { snackbarHostState.showSnackbar(joinSuccessText) }
            })
        }
    }

    Scaffold(
        topBar = {
            if (!isTabletMode) {
                TopAppBar(
                    title = { Text(group?.name ?: stringResource(R.string.game_loading_group), style = dims.heading2()) },
                    windowInsets = WindowInsets(top = 0.dp)
                )
            }
        },
        floatingActionButton = {
            if (group != null && question != null) {
                FloatingActionButton(
                    onClick = { onNavigateToGroupInfo(groupId) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(Icons.Filled.Groups, null,
                        modifier = if (dims.isTablet) Modifier.size(dims.fabIconSize) else Modifier)
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val topPad = if (isTabletMode) 16.dp else paddingValues.calculateTopPadding()
        Box(modifier = Modifier.padding(top = topPad, bottom = paddingValues.calculateBottomPadding()).fillMaxSize()) {
            if (group == null || question == null) {
                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = if (group == null) stringResource(R.string.game_joining_group) else stringResource(R.string.game_loading_questions),
                        style = dims.bodyText()
                    )
                }
            } else {
                if (dims.isLandscape) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = dims.screenPaddingH, vertical = dims.screenPaddingV),
                        horizontalArrangement = Arrangement.spacedBy(dims.sectionSpacing)
                    ) {
                        Column(
                            modifier = Modifier.weight(0.4f).fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(dims.sectionSpacing)
                        ) {
                            QuestionCard(question.question, dims)
                            VoteProgressHeader(question.answers.sumOf { it.count }, group.players?.size ?: 0, dims)
                        }
                        Box(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
                            InteractionArea(question, group, selectedPlayer, dims, !dims.isTablet,
                                onPlayerSelect = { selectedPlayer = it },
                                onVoteSubmit = {
                                    // TRIGGER: ViewModel Update
                                    selectedPlayer?.let { p ->
                                        viewModel.submitVote(groupId, p.id, { selectedPlayer = null }, {})
                                    }
                                }
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = dims.screenPaddingH, vertical = dims.screenPaddingV),
                        verticalArrangement = Arrangement.spacedBy(dims.sectionSpacing)
                    ) {
                        VoteProgressHeader(question.answers.sumOf { it.count }, group.players?.size ?: 0, dims)
                        QuestionCard(question.question, dims)
                        Box(modifier = Modifier.weight(1f)) {
                            InteractionArea(question, group, selectedPlayer, dims, false,
                                onPlayerSelect = { selectedPlayer = it },
                                onVoteSubmit = {
                                    selectedPlayer?.let { p ->
                                        viewModel.submitVote(groupId, p.id, { selectedPlayer = null }, {})
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}