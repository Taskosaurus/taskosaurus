package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import at.htlleonding.taskosaurus.data.model.Player
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    groupId: Int,
    viewModel: ViewModel = viewModel()
) {
    val groups by viewModel.groups.collectAsState()
    val questions by viewModel.latestQuestions.collectAsState()

    val group = groups.find { it.id == groupId }
    val question = questions[groupId]

    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = group?.name ?: "Gruppe",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            if (group != null && question != null) {
                FloatingActionButton(
                    onClick = { /* TODO: Mitgliederliste */ },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.offset(
                        x = if (!question.answered) (-15).dp else 0.dp,
                        y = 0.dp
                    )
                ) {
                    Icon(Icons.Filled.Groups, contentDescription = "Mitglieder")
                }
            }
        },
        bottomBar = {
            if (question != null && !question.answered) {
                Box(modifier = Modifier.padding(14.dp)) {
                    Button(
                        onClick = {
                            selectedPlayer?.let { player ->
                                viewModel.submitVote(
                                    groupId = groupId,
                                    answeredPlayerId = player.id,
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
                        Text(
                            text = "Abstimmen",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (group == null || question == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
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
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
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
                    Color(0xFFFFD700),
                    Color(0xFFFF6B9D),
                    Color(0xFF4CAF50),
                    Color(0xFF2196F3),
                    Color(0xFFFF9800),
                    Color(0xFF9C27B0)
                ).random(),
                size = Random.nextFloat() * 8f + 4f
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ConfettiAnimation(
            particles = confettiParticles,
            transition = infiniteTransition
        )

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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                if (top3.size >= 2) {
                    PodiumPlace(
                        name = top3[1].answeredName,
                        votes = top3[1].count,
                        place = 2,
                        height = 140.dp,
                        color = Color(0xFFC0C0C0),
                        infiniteTransition = infiniteTransition
                    )
                } else {
                    Spacer(modifier = Modifier.width(100.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                if (top3.isNotEmpty()) {
                    PodiumPlace(
                        name = top3[0].answeredName,
                        votes = top3[0].count,
                        place = 1,
                        height = 180.dp,
                        color = Color(0xFFFFD700),
                        infiniteTransition = infiniteTransition
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                if (top3.size >= 3) {
                    PodiumPlace(
                        name = top3[2].answeredName,
                        votes = top3[2].count,
                        place = 3,
                        height = 100.dp,
                        color = Color(0xFFCD7F32),
                        infiniteTransition = infiniteTransition
                    )
                } else {
                    Spacer(modifier = Modifier.width(100.dp))
                }
            }
        }
    }
}

@Composable
private fun PodiumPlace(
    name: String,
    votes: Int,
    place: Int,
    height: Dp,
    color: Color,
    infiniteTransition: InfiniteTransition
) {
    val scale by infiniteTransition.animateFloat(
        initialValue = if (place == 1) 1f else 1f,
        targetValue = if (place == 1) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale$place"
    )

    val medalColor = when (place) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.scale(if (place == 1) scale else 1f)
        ) {
            Text(
                text = when (place) {
                    1 -> "🥇"
                    2 -> "🥈"
                    3 -> "🥉"
                    else -> ""
                },
                fontSize = 32.sp,
                modifier = Modifier
                    .offset(y = (-25).dp)
                    .align(Alignment.TopCenter)
            )

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(medalColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "$votes Stimmen",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .width(100.dp)
                .height(height),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = medalColor.copy(alpha = 0.3f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = place.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = medalColor.copy(alpha = 0.6f)
                )
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
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(
                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    player.name.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                player.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun VoteProgressHeader(votedCount: Int, totalCount: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Abgestimmt: $votedCount/$totalCount",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { votedCount.toFloat() / totalCount.coerceAtLeast(1).toFloat() },
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        )
    }
}

@Composable
private fun ConfettiAnimation(
    particles: List<ConfettiParticle>,
    transition: InfiniteTransition
) {
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiTime"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val progress = (time * particle.speed / 100f) % 1.5f

            if (progress <= 1.0f) {
                val x = size.width * particle.initialX
                val y = size.height * progress
                val rotation = particle.rotation + time * particle.rotationSpeed

                val alpha = if (progress > 0.8f) (1.0f - progress) * 5f else 1f

                rotate(
                    degrees = rotation,
                    pivot = Offset(x, y)
                ) {
                    drawRect(
                        color = particle.color.copy(alpha = alpha.coerceIn(0f, 1f)),
                        topLeft = Offset(x - particle.size / 2, y - particle.size / 2),
                        size = androidx.compose.ui.geometry.Size(particle.size, particle.size)
                    )
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