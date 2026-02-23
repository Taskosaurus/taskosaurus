package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
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

    LaunchedEffect(player, isReady, group) {
        if (isReady && player != null && group == null) {
            viewModel.joinGroup(
                groupId = groupId,
                onSuccess = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Erfolgreich beigetreten!")
                    }
                }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = group?.name ?: "Lade Gruppe...",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Box(modifier = Modifier.weight(1f)) {
                if (group == null || question == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (group == null) "Trete Gruppe bei..." else "Lade Fragen...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        VoteProgressHeader(
                            votedCount = question.answers.sumOf { it.count },
                            totalCount = group.players?.size ?: 0
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        QuestionCard(question = question.question)
                        Spacer(modifier = Modifier.height(24.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (question.answered) {
                                ResultsPodium(question = question)
                            } else {
                                VotingSection(
                                    players = group.players ?: emptyList(),
                                    selectedPlayer = selectedPlayer,
                                    onPlayerSelect = { selectedPlayer = it }
                                )
                            }
                        }

                        if (question != null && !question.answered) {
                            Box(modifier = Modifier.padding(vertical = 16.dp)) {
                                Button(
                                    onClick = {
                                        selectedPlayer?.let { p ->
                                            viewModel.submitVote(
                                                groupId = groupId,
                                                answeredPlayerId = p.id,
                                                onSuccess = { selectedPlayer = null },
                                                onError = { }
                                            )
                                        }
                                    },
                                    enabled = selectedPlayer != null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Abstimmen", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (group != null && question != null) {
            FloatingActionButton(
                onClick = { onNavigateToGroupInfo(groupId) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(Icons.Filled.Groups, contentDescription = "Mitglieder & QR-Code")
            }
        }
    }
}

@Composable
private fun VotingSection(
    players: List<Player>,
    selectedPlayer: Player?,
    onPlayerSelect: (Player) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultsPodium(question: Question) {
    val sortedAnswers = question.answers.sortedByDescending { it.count }
    val top3 = sortedAnswers.take(3)
    val infiniteTransition = rememberInfiniteTransition(label = "podium")

    val confettiParticles = remember {
        List(30) {
            ConfettiParticle(
                initialX = Random.nextFloat(),
                initialY = -0.1f - Random.nextFloat() * 0.2f,
                speed = Random.nextFloat() * 2f + 1f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 4f - 2f,
                color = listOf(
                    Color(0xFFFFD700), Color(0xFFFF6B9D), Color(0xFF4CAF50),
                    Color(0xFF2196F3), Color(0xFFFF9800), Color(0xFF9C27B0)
                ).random(),
                size = Random.nextFloat() * 8f + 4f
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ConfettiAnimation(particles = confettiParticles, transition = infiniteTransition)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏆 Die Gewinner 🏆",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                if (top3.size >= 2) {
                    PodiumPlace(top3[1].answeredName, top3[1].count, 2, 140.dp, Color(0xFFC0C0C0), infiniteTransition)
                } else Spacer(modifier = Modifier.width(100.dp))

                Spacer(modifier = Modifier.width(12.dp))

                if (top3.isNotEmpty()) {
                    PodiumPlace(top3[0].answeredName, top3[0].count, 1, 180.dp, Color(0xFFFFD700), infiniteTransition)
                }

                Spacer(modifier = Modifier.width(12.dp))

                if (top3.size >= 3) {
                    PodiumPlace(top3[2].answeredName, top3[2].count, 3, 100.dp, Color(0xFFCD7F32), infiniteTransition)
                } else Spacer(modifier = Modifier.width(100.dp))
            }
        }
    }
}

@Composable
private fun PodiumPlace(name: String, votes: Int, place: Int, height: Dp, color: Color, infiniteTransition: InfiniteTransition) {
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = if (place == 1) 1.05f else 1f,
        animationSpec = infiniteRepeatable(tween(1500, easing = EaseInOut), RepeatMode.Reverse),
        label = "scale$place"
    )

    val medalColor = when (place) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.primary
    }

    Column(modifier = Modifier.width(100.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.scale(if (place == 1) scale else 1f)) {
            Text(text = when (place) { 1 -> "🥇" 2 -> "🥈" 3 -> "🥉" else -> "" }, fontSize = 32.sp, modifier = Modifier.offset(y = (-25).dp).align(Alignment.TopCenter))
            Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(medalColor), contentAlignment = Alignment.Center) {
                Text(name.take(1).uppercase(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)
        Text("$votes Stimmen", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.width(100.dp).height(height),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            colors = CardDefaults.cardColors(containerColor = medalColor.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(place.toString(), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = medalColor.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
private fun PlayerCard(player: Player, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(player.name.take(1).uppercase(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(player.name, style = MaterialTheme.typography.bodyLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun VoteProgressHeader(votedCount: Int, totalCount: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Abgestimmt: $votedCount/$totalCount", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { if (totalCount > 0) votedCount.toFloat() / totalCount.toFloat() else 0f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun QuestionCard(question: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(question, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(24.dp))
    }
}

@Composable
private fun ConfettiAnimation(particles: List<ConfettiParticle>, transition: InfiniteTransition) {
    val time by transition.animateFloat(0f, 1000f, infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart), label = "confettiTime")
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val progress = (time * particle.speed / 100f) % 1.5f
            if (progress <= 1.0f) {
                val x = size.width * particle.initialX
                val y = size.height * progress
                val rotation = particle.rotation + time * particle.rotationSpeed
                val alpha = if (progress > 0.8f) (1.0f - progress) * 5f else 1f
                rotate(degrees = rotation, pivot = Offset(x, y)) {
                    drawRect(color = particle.color.copy(alpha = alpha.coerceIn(0f, 1f)), topLeft = Offset(x - particle.size / 2, y - particle.size / 2), size = androidx.compose.ui.geometry.Size(particle.size, particle.size))
                }
            }
        }
    }
}

data class ConfettiParticle(
    val initialX: Float,
    val initialY: Float,
    val speed: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val color: Color,
    val size: Float
)