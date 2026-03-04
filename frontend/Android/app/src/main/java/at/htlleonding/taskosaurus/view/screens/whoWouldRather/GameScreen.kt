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

    // Automatisches Beitreten, falls man den Link/QR nutzt aber noch nicht drin ist
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

@Composable
private fun InteractionArea(
    question: Question, group: Group, selectedPlayer: Player?,
    dims: AppDimensions, isPhoneLandscape: Boolean,
    onPlayerSelect: (Player) -> Unit, onVoteSubmit: () -> Unit
) {
    if (question.answered) {
        ResultsPodium(question, question.answers.sumOf { it.count }, group.players?.size ?: 0, dims)
    } else {
        Column(Modifier.fillMaxSize()) {
            VotingSection(group.players ?: emptyList(), selectedPlayer, Modifier.weight(1f), dims, onPlayerSelect)
            Spacer(Modifier.height(dims.itemSpacing))
            Button(
                onClick = onVoteSubmit,
                enabled = selectedPlayer != null,
                modifier = Modifier.fillMaxWidth().height(dims.voteButtonHeight),
                shape = RoundedCornerShape(dims.questionCardRadius)
            ) {
                Text(stringResource(R.string.game_btn_vote), style = dims.voteButtonText(), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun VotingSection(
    players: List<Player>, selectedPlayer: Player?,
    modifier: Modifier, dims: AppDimensions,
    onPlayerSelect: (Player) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dims.questionCardRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = dims.itemSpacing / 2)) {
            items(players) { player ->
                PlayerCard(player, selectedPlayer == player, dims) { onPlayerSelect(player) }
                if (player != players.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = dims.playerItemPaddingH),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

private data class RankedGroup(val place: Int, val entries: List<Answer>)

private fun buildOlympicRanks(sortedAnswers: List<Answer>): List<RankedGroup> {
    if (sortedAnswers.isEmpty()) return emptyList()
    val result = mutableListOf<RankedGroup>()
    var currentPlace = 1; var i = 0
    while (i < sortedAnswers.size && currentPlace <= 3) {
        val currentCount = sortedAnswers[i].count
        val tied = mutableListOf<Answer>(); var j = i
        while (j < sortedAnswers.size && sortedAnswers[j].count == currentCount) { tied.add(sortedAnswers[j]); j++ }
        result.add(RankedGroup(currentPlace, tied))
        currentPlace += tied.size; i = j
    }
    return result
}

@Composable
private fun ResultsPodium(
    question: Question,
    votedCount: Int,
    totalCount: Int,
    dims: AppDimensions
) {
    val rankedGroups = buildOlympicRanks(question.answers.sortedByDescending { it.count })
    val rank1 = rankedGroups.find { it.place == 1 }
    val rank2 = rankedGroups.find { it.place == 2 }
    val rank3 = rankedGroups.find { it.place == 3 }

    val infiniteTransition = rememberInfiniteTransition(label = "podium")
    val confettiParticles = remember {
        List(30) {
            ConfettiParticle(
                Random.nextFloat(), -0.1f - Random.nextFloat() * 0.2f,
                Random.nextFloat() * 2f + 1f, Random.nextFloat() * 360f, Random.nextFloat() * 4f - 2f,
                listOf(Color(0xFFFFD700), Color(0xFFFF6B9D), Color(0xFF4CAF50),
                    Color(0xFF2196F3), Color(0xFFFF9800), Color(0xFF9C27B0)).random(),
                Random.nextFloat() * 8f + 4f
            )
        }
    }

    // Handy-Landscape bekommt kompakte Werte, damit das Podium sichtbar bleibt
    val podiumRowH  = if (dims.isLandscape && !dims.isTablet) 160.dp else dims.podiumRowHeight
    val bar1        = if (dims.isLandscape && !dims.isTablet) 90.dp  else dims.podiumBar1
    val bar2        = if (dims.isLandscape && !dims.isTablet) 65.dp  else dims.podiumBar2
    val bar3        = if (dims.isLandscape && !dims.isTablet) 50.dp  else dims.podiumBar3
    val colW        = if (dims.isLandscape && !dims.isTablet) 70.dp  else dims.podiumColWidth
    val avatarSz    = if (dims.isLandscape && !dims.isTablet) 28.dp  else dims.podiumAvatarSize
    val tinyAvatSz  = if (dims.isLandscape && !dims.isTablet) 20.dp  else dims.podiumTinyAvatarSize
    val medalSz     = if (dims.isLandscape && !dims.isTablet) 18.sp  else dims.podiumMedalSize
    val colSpacing  = if (dims.isLandscape && !dims.isTablet) 6.dp   else dims.podiumColSpacing

    Box(modifier = Modifier.fillMaxSize()) {
        ConfettiAnimation(confettiParticles, infiniteTransition)
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (votedCount == totalCount) stringResource(R.string.game_results_winners) else stringResource(R.string.game_results_intermediate),
                style = dims.podiumTitle(), fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = if (!dims.isLandscape) 32.dp else if (!dims.isTablet) 4.dp else if (dims.isTablet) 16.dp else 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(podiumRowH),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                if (rank2 != null) {
                    PodiumColumn(rank2, votedCount, bar2, infiniteTransition, dims, colW, avatarSz, tinyAvatSz, medalSz)
                    Spacer(Modifier.width(colSpacing))
                }
                if (rank1 != null) PodiumColumn(rank1, votedCount, bar1, infiniteTransition, dims, colW, avatarSz, tinyAvatSz, medalSz)
                if (rank3 != null) {
                    Spacer(Modifier.width(colSpacing))
                    PodiumColumn(rank3, votedCount, bar3, infiniteTransition, dims, colW, avatarSz, tinyAvatSz, medalSz)
                }
            }
        }
    }
}

@Composable
private fun PodiumColumn(
    rankedGroup: RankedGroup,
    votedCount: Int,
    barHeight: Dp,
    infiniteTransition: InfiniteTransition,
    dims: AppDimensions,
    colWidth: Dp,
    avatarSize: Dp,
    tinyAvatarSize: Dp,
    medalSize: TextUnit
) {
    val place = rankedGroup.place
    val medalColor = when (place) { 1 -> Color(0xFFFFD700); 2 -> Color(0xFFC0C0C0); 3 -> Color(0xFFCD7F32); else -> MaterialTheme.colorScheme.primary }
    val medalEmoji = when (place) { 1 -> "🥇"; 2 -> "🥈"; 3 -> "🥉"; else -> "" }

    val scale by infiniteTransition.animateFloat(
        1f, if (place == 1) 1.04f else 1f,
        infiniteRepeatable(tween(1500, easing = EaseInOut), RepeatMode.Reverse), "scale$place"
    )

    Column(
        modifier = Modifier.width(colWidth).scale(if (place == 1) scale else 1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(medalEmoji, fontSize = medalSize,
            modifier = Modifier.padding(bottom = if (!dims.isLandscape) 10.dp else if (dims.isTablet) 4.dp else 2.dp))

        if (rankedGroup.entries.size == 1) {
            Box(modifier = Modifier.size(avatarSize).clip(CircleShape).background(medalColor), contentAlignment = Alignment.Center) {
                Text(rankedGroup.entries[0].answeredName.take(1).uppercase(),
                    style = dims.podiumAvatarLetter(), fontWeight = FontWeight.Bold, color = Color.White)
            }
        } else {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.widthIn(max = colWidth)) {
                rankedGroup.entries.take(3).forEachIndexed { idx, entry ->
                    if (idx > 0) Spacer(Modifier.width(if (dims.isTablet) 4.dp else 2.dp))
                    Box(modifier = Modifier.size(tinyAvatarSize).clip(CircleShape).background(medalColor), contentAlignment = Alignment.Center) {
                        Text(entry.answeredName.take(1).uppercase(), style = dims.labelText(), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        Spacer(Modifier.height(if (!dims.isLandscape) 12.dp else if (dims.isTablet) 6.dp else 4.dp))

        rankedGroup.entries.forEach { entry ->
            Text(entry.answeredName, style = dims.podiumName(), fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = colWidth),
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        if (votedCount > 0) {
            Text("%.0f%%".format(rankedGroup.entries[0].count.toFloat() / votedCount.toFloat() * 100),
                style = dims.labelText(), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(Modifier.height(if (!dims.isLandscape) 14.dp else if (dims.isTablet) 8.dp else 4.dp))

        Card(
            modifier = Modifier.width(colWidth - 6.dp).height(barHeight),
            shape = RoundedCornerShape(topStart = dims.cardRadius, topEnd = dims.cardRadius),
            colors = CardDefaults.cardColors(containerColor = medalColor.copy(alpha = 0.2f))
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(place.toString(), style = dims.podiumBarNumber(), fontWeight = FontWeight.Black, color = medalColor.copy(alpha = 0.35f))
            }
        }
    }
}

@Composable
private fun PlayerCard(player: Player, isSelected: Boolean, dims: AppDimensions, onClick: () -> Unit) {
    Surface(onClick = onClick,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) else Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = dims.playerItemPaddingH, vertical = dims.playerItemPaddingV),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(dims.playerAvatarSize).clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(player.name.take(1).uppercase(),
                    style = if (dims.isTablet) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.width(if (dims.isTablet) 16.dp else 12.dp))
            Text(player.name, style = dims.heading2(), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
private fun VoteProgressHeader(votedCount: Int, totalCount: Int, dims: AppDimensions) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dims.questionCardRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(dims.progressCardPadding)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(stringResource(R.string.game_voted_label), style = dims.progressText(), color = MaterialTheme.colorScheme.primary)
                Text("$votedCount/$totalCount", style = dims.progressText(), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(dims.itemSpacing))
            LinearProgressIndicator(
                progress = { if (totalCount > 0) votedCount.toFloat() / totalCount.toFloat() else 0f },
                modifier = Modifier.fillMaxWidth().height(dims.progressBarHeight).clip(CircleShape)
            )
        }
    }
}

@Composable
private fun QuestionCard(question: String, dims: AppDimensions) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dims.questionCardRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Text(question, style = dims.questionText(), fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(dims.questionCardPadding),
            lineHeight = if (!dims.isLandscape) 36.sp else if (dims.isTablet) 28.sp else 22.sp)
    }
}

@Composable
private fun ConfettiAnimation(particles: List<ConfettiParticle>, transition: InfiniteTransition) {
    val time by transition.animateFloat(0f, 1000f, infiniteRepeatable(tween(8000, easing = LinearEasing)), "confetti")
    Canvas(Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val progress = (time * particle.speed / 100f) % 1.5f
            if (progress <= 1.0f) {
                val x = size.width * particle.initialX
                val y = size.height * progress
                rotate(particle.rotation + time * particle.rotationSpeed, Offset(x, y)) {
                    drawRect(
                        particle.color.copy(alpha = if (progress > 0.8f) (1f - progress) * 5f else 1f),
                        Offset(x - particle.size / 2, y - particle.size / 2),
                        androidx.compose.ui.geometry.Size(particle.size, particle.size)
                    )
                }
            }
        }
    }
}

data class ConfettiParticle(
    val initialX: Float, val initialY: Float, val speed: Float,
    val rotation: Float, val rotationSpeed: Float, val color: Color, val size: Float
)