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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.*
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    groupId: Int,
    viewModel: ViewModel = viewModel(),
    onNavigateToGroupInfo: (Int) -> Unit
) {
    val groups by viewModel.groups.collectAsState()
    val questions by viewModel.latestQuestions.collectAsState()
    val player by viewModel.player.collectAsState()
    val isReady by viewModel.isReady.collectAsState()

    val group = groups.find { it.id == groupId }
    val question = questions[groupId]

    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    LaunchedEffect(player, isReady, group) {
        if (isReady && player != null && group == null) {
            viewModel.joinGroup(groupId = groupId, onSuccess = {
                scope.launch { snackbarHostState.showSnackbar("Erfolgreich beigetreten!") }
            })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(group?.name ?: "Lade Gruppe...", style = MaterialTheme.typography.titleMedium) },
                windowInsets = WindowInsets(top = 0.dp)
            )
        },
        floatingActionButton = {
            if (group != null && question != null) {
                FloatingActionButton(
                    onClick = { onNavigateToGroupInfo(groupId) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(8.dp)
                ) { Icon(Icons.Filled.Groups, contentDescription = null) }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (group == null || question == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = if (group == null) "Trete Gruppe bei..." else "Lade Fragen...", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                if (isLandscape) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 4.dp, end = 4.dp, top = 0.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(0.4f).fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuestionCard(question = question.question)
                            VoteProgressHeader(
                                votedCount = question.answers.sumOf { it.count },
                                totalCount = group.players?.size ?: 0
                            )
                        }

                        Box(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
                            InteractionArea(
                                question = question,
                                group = group,
                                selectedPlayer = selectedPlayer,
                                onPlayerSelect = { selectedPlayer = it },
                                onVoteSubmit = {
                                    selectedPlayer?.let { p ->
                                        viewModel.submitVote(groupId, p.id, { selectedPlayer = null }, {})
                                    }
                                }
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        VoteProgressHeader(
                            votedCount = question.answers.sumOf { it.count },
                            totalCount = group.players?.size ?: 0
                        )
                        QuestionCard(question = question.question)
                        Box(modifier = Modifier.weight(1f)) {
                            InteractionArea(
                                question = question,
                                group = group,
                                selectedPlayer = selectedPlayer,
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
    question: Question,
    group: Group,
    selectedPlayer: Player?,
    onPlayerSelect: (Player) -> Unit,
    onVoteSubmit: () -> Unit
) {
    if (question.answered) {
        ResultsPodium(
            question = question,
            votedCount = question.answers.sumOf { it.count },
            totalCount = group.players?.size ?: 0
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            VotingSection(
                players = group.players ?: emptyList(),
                selectedPlayer = selectedPlayer,
                modifier = Modifier.weight(1f),
                onPlayerSelect = onPlayerSelect
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onVoteSubmit,
                enabled = selectedPlayer != null,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Abstimmen", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun VotingSection(
    players: List<Player>,
    selectedPlayer: Player?,
    modifier: Modifier = Modifier,
    onPlayerSelect: (Player) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(players) { player ->
                PlayerCard(
                    player = player,
                    isSelected = selectedPlayer == player,
                    onClick = { onPlayerSelect(player) }
                )
                if (player != players.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultsPodium(question: Question, votedCount: Int, totalCount: Int) {
    val sortedAnswers = question.answers.sortedByDescending { it.count }
    val top3 = sortedAnswers.take(3)
    val infiniteTransition = rememberInfiniteTransition(label = "podium")
    val confettiParticles = remember {
        List(25) {
            ConfettiParticle(
                initialX = Random.nextFloat(),
                initialY = -0.1f - Random.nextFloat() * 0.2f,
                speed = Random.nextFloat() * 2f + 1f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 4f - 2f,
                color = listOf(Color(0xFFFFD700), Color(0xFFFF6B9D), Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800), Color(0xFF9C27B0)).random(),
                size = Random.nextFloat() * 6f + 4f
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ConfettiAnimation(particles = confettiParticles, transition = infiniteTransition)
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (votedCount == totalCount) "🏆 Die Gewinner 🏆" else "Zwischenstand",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                if (top3.size >= 2) PodiumPlace(
                    top3[1].answeredName,
                    top3[1].count,
                    votedCount,
                    2,
                    100.dp,
                    Color(0xFFC0C0C0),
                    infiniteTransition
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (top3.isNotEmpty()) PodiumPlace(
                    top3[0].answeredName,
                    top3[0].count,
                    votedCount,
                    1,
                    140.dp,
                    Color(0xFFFFD700),
                    infiniteTransition
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (top3.size >= 3) PodiumPlace(
                    top3[2].answeredName,
                    top3[2].count,
                    votedCount,
                    3,
                    80.dp,
                    Color(0xFFCD7F32),
                    infiniteTransition
                )
            }
        }
    }
}

@Composable
private fun PodiumPlace(
    name: String,
    votes: Int,
    totalVotes: Int,
    place: Int,
    height: Dp,
    color: Color,
    infiniteTransition: InfiniteTransition
) {
    val scale by infiniteTransition.animateFloat(
        1f, if (place == 1) 1.05f else 1f,
        infiniteRepeatable(
            tween(1500, easing = EaseInOut),
            RepeatMode.Reverse
        ), "scale$place"
    )
    val medalColor = when (place) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.primary
    }
    Column(
        modifier = Modifier
            .width(85.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .scale(if (place == 1) scale else 1f)
        ) {
            Text(
                when (place) {
                    1 -> "🥇"
                    2 -> "🥈"
                    3 -> "🥉"
                    else -> "" },
                fontSize = 24.sp,
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .align(Alignment.TopCenter)
            )
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(medalColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    name.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Text(
            name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = "${votes*1.0/totalVotes * 100}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Card(
            modifier = Modifier
                .width(80.dp)
                .height(height),
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            colors = CardDefaults.cardColors(containerColor = medalColor.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    place.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = medalColor.copy(alpha = 0.3f))
            }
        }
    }
}

@Composable
private fun PlayerCard(player: Player, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color =
            if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            else Color.Transparent, modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    player.name.take(1).uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color =
                        if (isSelected) MaterialTheme.onPrimary()
                        else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Text(
                player.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight =
                    if (isSelected) FontWeight.Bold
                    else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun VoteProgressHeader(votedCount: Int, totalCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier =
                Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Abgestimmt",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "$votedCount/$totalCount",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(
                modifier = Modifier.height(6.dp)
            )
            LinearProgressIndicator(
                progress = { if (totalCount > 0) votedCount.toFloat() / totalCount.toFloat() else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun QuestionCard(question: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp), lineHeight = 22.sp)
    }
}

@Composable
private fun ConfettiAnimation(particles: List<ConfettiParticle>, transition: InfiniteTransition) {
    val time by transition.animateFloat(0f, 1000f, infiniteRepeatable(tween(8000, easing = LinearEasing)), "confetti")
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val progress = (time * particle.speed / 100f) % 1.5f
            if (progress <= 1.0f) {
                val x = size.width * particle.initialX
                val y = size.height * progress
                rotate(particle.rotation + time * particle.rotationSpeed, Offset(x, y)) {
                    drawRect(particle.color.copy(alpha = if (progress > 0.8f) (1f - progress) * 5f else 1f), Offset(x - particle.size / 2, y - particle.size / 2), androidx.compose.ui.geometry.Size(particle.size, particle.size))
                }
            }
        }
    }
}

@Composable
fun MaterialTheme.onPrimary() = MaterialTheme.colorScheme.onPrimary

data class ConfettiParticle(val initialX: Float, val initialY: Float, val speed: Float, val rotation: Float, val rotationSpeed: Float, val color: Color, val size: Float)