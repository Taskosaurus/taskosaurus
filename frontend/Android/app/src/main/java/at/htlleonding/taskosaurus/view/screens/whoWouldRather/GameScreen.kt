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
    onNavigateToGroupInfo: (Int) -> Unit,
    isTabletMode: Boolean = false // NEU: Steuerung für Master-Detail Ansicht
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

    // Wir nutzen das Landscape-Layout nur auf dem Handy.
    // Am Tablet (in der rechten Spalte) nutzen wir das vertikale Layout.
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
            // TopBar ausblenden, wenn wir in der rechten Spalte des Tablets sind
            if (!isTabletMode) {
                TopAppBar(
                    title = {
                        Text(
                            group?.name ?: "Lade Gruppe...",
                            style = if (configuration.screenWidthDp >= 600) MaterialTheme.typography.titleLarge
                            else MaterialTheme.typography.titleMedium
                        )
                    },
                    windowInsets = WindowInsets(top = 0.dp)
                )
            }
        },
        floatingActionButton = {
            // FAB nur zeigen, wenn Daten da sind. Positionierung am Tablet ggf. anpassen.
            if (group != null && question != null) {
                FloatingActionButton(
                    onClick = { onNavigateToGroupInfo(groupId) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Groups,
                        contentDescription = null,
                        modifier = if (configuration.screenWidthDp >= 600) Modifier.size(28.dp) else Modifier
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        // Dynamisches Padding für Tablet-Modus
        val contentPadding = if (isTabletMode) 16.dp else paddingValues.calculateTopPadding()

        Box(modifier = Modifier
            .padding(top = contentPadding, bottom = paddingValues.calculateBottomPadding())
            .fillMaxSize()
        ) {
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
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                val isTablet = configuration.screenWidthDp >= 600
                if (isLandscape) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = if (isTablet) 16.dp else 4.dp,
                                end = if (isTablet) 16.dp else 4.dp,
                                top = 0.dp,
                                bottom = if (isTablet) 12.dp else 8.dp
                            ),
                        horizontalArrangement = Arrangement.spacedBy(if (isTablet) 20.dp else 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(0.4f).fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(if (isTablet) 16.dp else 12.dp)
                        ) {
                            QuestionCard(question = question.question, isTabletPortrait = isTablet)
                            VoteProgressHeader(
                                votedCount = question.answers.sumOf { it.count },
                                totalCount = group.players?.size ?: 0,
                                isTabletPortrait = isTablet
                            )
                        }
                        Box(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
                            InteractionArea(
                                question = question,
                                group = group,
                                selectedPlayer = selectedPlayer,
                                isTabletPortrait = isTablet,
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
                    // Portrait — Handy original, Tablet größer
                    val isTabletPortrait = isTablet
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = if (isTabletPortrait) 28.dp else 8.dp,
                                vertical = if (isTabletPortrait) 16.dp else 4.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(if (isTabletPortrait) 20.dp else 12.dp)
                    ) {
                        VoteProgressHeader(
                            votedCount = question.answers.sumOf { it.count },
                            totalCount = group.players?.size ?: 0,
                            isTabletPortrait = isTabletPortrait
                        )
                        QuestionCard(question = question.question, isTabletPortrait = isTabletPortrait)
                        Box(modifier = Modifier.weight(1f)) {
                            InteractionArea(
                                question = question,
                                group = group,
                                selectedPlayer = selectedPlayer,
                                isTabletPortrait = isTabletPortrait,
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
    isTabletPortrait: Boolean,
    onPlayerSelect: (Player) -> Unit,
    onVoteSubmit: () -> Unit
) {
    if (question.answered) {
        ResultsPodium(
            question = question,
            votedCount = question.answers.sumOf { it.count },
            totalCount = group.players?.size ?: 0,
            isTabletPortrait = isTabletPortrait
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            VotingSection(
                players = group.players ?: emptyList(),
                selectedPlayer = selectedPlayer,
                modifier = Modifier.weight(1f),
                isTabletPortrait = isTabletPortrait,
                onPlayerSelect = onPlayerSelect
            )
            Spacer(modifier = Modifier.height(if (isTabletPortrait) 16.dp else 12.dp))
            Button(
                onClick = onVoteSubmit,
                enabled = selectedPlayer != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isTabletPortrait) 72.dp else 48.dp),
                shape = RoundedCornerShape(if (isTabletPortrait) 16.dp else 12.dp)
            ) {
                Text(
                    "Abstimmen",
                    style = if (isTabletPortrait) MaterialTheme.typography.headlineSmall
                    else MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun VotingSection(
    players: List<Player>,
    selectedPlayer: Player?,
    modifier: Modifier = Modifier,
    isTabletPortrait: Boolean,
    onPlayerSelect: (Player) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTabletPortrait) 20.dp else 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = if (isTabletPortrait) 8.dp else 4.dp)
        ) {
            items(players) { player ->
                PlayerCard(
                    player = player,
                    isSelected = selectedPlayer == player,
                    isTabletPortrait = isTabletPortrait,
                    onClick = { onPlayerSelect(player) }
                )
                if (player != players.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = if (isTabletPortrait) 20.dp else 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

// ─── Olympische Rang-Logik ───────────────────────────────────────────────────

private data class RankedGroup(val place: Int, val entries: List<Answer>)

private fun buildOlympicRanks(sortedAnswers: List<Answer>): List<RankedGroup> {
    if (sortedAnswers.isEmpty()) return emptyList()
    val result = mutableListOf<RankedGroup>()
    var currentPlace = 1
    var i = 0
    while (i < sortedAnswers.size && currentPlace <= 3) {
        val currentCount = sortedAnswers[i].count
        val tied = mutableListOf<Answer>()
        var j = i
        while (j < sortedAnswers.size && sortedAnswers[j].count == currentCount) {
            tied.add(sortedAnswers[j])
            j++
        }
        result.add(RankedGroup(currentPlace, tied))
        currentPlace += tied.size
        i = j
    }
    return result
}

// ─── Podium ──────────────────────────────────────────────────────────────────

@Composable
private fun ResultsPodium(
    question: Question,
    votedCount: Int,
    totalCount: Int,
    isTabletPortrait: Boolean
) {
    val sortedAnswers = question.answers.sortedByDescending { it.count }
    val rankedGroups = buildOlympicRanks(sortedAnswers)

    val rank1 = rankedGroups.find { it.place == 1 }
    val rank2 = rankedGroups.find { it.place == 2 }
    val rank3 = rankedGroups.find { it.place == 3 }

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

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    val isTabletLandscape = isTabletPortrait && isLandscape

    val podiumRowHeight = when {
        isTabletPortrait && !isLandscape -> 520.dp
        isTabletLandscape               -> 300.dp
        else                            -> 240.dp
    }
    val bar1Height = when {
        isTabletPortrait && !isLandscape -> 310.dp
        isTabletLandscape               -> 185.dp
        else                            -> 140.dp
    }
    val bar2Height = when {
        isTabletPortrait && !isLandscape -> 225.dp
        isTabletLandscape               -> 135.dp
        else                            -> 100.dp
    }
    val bar3Height = when {
        isTabletPortrait && !isLandscape -> 175.dp
        isTabletLandscape               -> 105.dp
        else                            -> 80.dp
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ConfettiAnimation(particles = confettiParticles, transition = infiniteTransition)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (votedCount == totalCount) "🏆 Die Gewinner 🏆" else "Zwischenstand",
                style = when {
                    isTabletPortrait && !isLandscape -> MaterialTheme.typography.headlineLarge
                    isTabletLandscape               -> MaterialTheme.typography.headlineMedium
                    else                            -> MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = when {
                    isTabletPortrait && !isLandscape -> 32.dp
                    isTabletLandscape               -> 16.dp
                    else                            -> 12.dp
                })
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(podiumRowHeight),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                if (rank2 != null) {
                    PodiumColumn(
                        rankedGroup = rank2,
                        votedCount = votedCount,
                        barHeight = bar2Height,
                        infiniteTransition = infiniteTransition,
                        isTablet = isTabletPortrait,
                        isTabletLandscape = isTabletLandscape
                    )
                    Spacer(modifier = Modifier.width(when {
                        isTabletPortrait && !isLandscape -> 24.dp
                        isTabletLandscape               -> 14.dp
                        else                            -> 8.dp
                    }))
                }
                if (rank1 != null) {
                    PodiumColumn(
                        rankedGroup = rank1,
                        votedCount = votedCount,
                        barHeight = bar1Height,
                        infiniteTransition = infiniteTransition,
                        isTablet = isTabletPortrait,
                        isTabletLandscape = isTabletLandscape
                    )
                }
                if (rank3 != null) {
                    Spacer(modifier = Modifier.width(when {
                        isTabletPortrait && !isLandscape -> 24.dp
                        isTabletLandscape               -> 14.dp
                        else                            -> 8.dp
                    }))
                    PodiumColumn(
                        rankedGroup = rank3,
                        votedCount = votedCount,
                        barHeight = bar3Height,
                        infiniteTransition = infiniteTransition,
                        isTablet = isTabletPortrait,
                        isTabletLandscape = isTabletLandscape
                    )
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
    isTablet: Boolean,
    isTabletLandscape: Boolean = false
) {
    val place = rankedGroup.place
    val medalColor = when (place) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.primary
    }
    val medalEmoji = when (place) { 1 -> "🥇"; 2 -> "🥈"; 3 -> "🥉"; else -> "" }

    val scale by infiniteTransition.animateFloat(
        1f, if (place == 1) 1.04f else 1f,
        infiniteRepeatable(tween(1500, easing = EaseInOut), RepeatMode.Reverse),
        "podiumScale$place"
    )

    val colWidth = when {
        !isTablet         -> 90.dp
        isTabletLandscape -> 120.dp
        else              -> 160.dp
    }
    val avatarSize = when {
        !isTablet         -> 40.dp
        isTabletLandscape -> 52.dp
        else              -> 76.dp
    }
    val tinyAvatarSize = when {
        !isTablet         -> 28.dp
        isTabletLandscape -> 36.dp
        else              -> 50.dp
    }
    val medailleSize = when {
        !isTablet         -> 24.sp
        isTabletLandscape -> 30.sp
        else              -> 44.sp
    }

    Column(
        modifier = Modifier
            .width(colWidth)
            .scale(if (place == 1) scale else 1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = medalEmoji,
            fontSize = medailleSize,
            modifier = Modifier.padding(bottom = when {
                !isTablet         -> 2.dp
                isTabletLandscape -> 4.dp
                else              -> 10.dp
            })
        )

        if (rankedGroup.entries.size == 1) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(medalColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rankedGroup.entries[0].answeredName.take(1).uppercase(),
                    style = when {
                        !isTablet         -> MaterialTheme.typography.titleMedium
                        isTabletLandscape -> MaterialTheme.typography.titleLarge
                        else              -> MaterialTheme.typography.headlineMedium
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.widthIn(max = colWidth)
            ) {
                rankedGroup.entries.take(3).forEachIndexed { idx, entry ->
                    if (idx > 0) Spacer(Modifier.width(if (isTablet) 4.dp else 2.dp))
                    Box(
                        modifier = Modifier
                            .size(tinyAvatarSize)
                            .clip(CircleShape)
                            .background(medalColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = entry.answeredName.take(1).uppercase(),
                            style = when {
                                !isTablet         -> MaterialTheme.typography.labelSmall
                                isTabletLandscape -> MaterialTheme.typography.bodyMedium
                                else              -> MaterialTheme.typography.titleSmall
                            },
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(when {
            !isTablet         -> 4.dp
            isTabletLandscape -> 6.dp
            else              -> 12.dp
        }))

        rankedGroup.entries.forEach { entry ->
            Text(
                text = entry.answeredName,
                style = when {
                    !isTablet         -> MaterialTheme.typography.labelMedium
                    isTabletLandscape -> MaterialTheme.typography.bodyMedium
                    else              -> MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = colWidth)
            )
        }

        if (votedCount > 0) {
            Text(
                text = "%.0f%%".format(rankedGroup.entries[0].count.toFloat() / votedCount.toFloat() * 100),
                style = when {
                    !isTablet         -> MaterialTheme.typography.labelSmall
                    isTabletLandscape -> MaterialTheme.typography.bodySmall
                    else              -> MaterialTheme.typography.bodyMedium
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(when {
            !isTablet         -> 4.dp
            isTabletLandscape -> 8.dp
            else              -> 14.dp
        }))

        Card(
            modifier = Modifier
                .width(colWidth - 6.dp)
                .height(barHeight),
            shape = RoundedCornerShape(
                topStart = if (isTablet) 16.dp else 8.dp,
                topEnd = if (isTablet) 16.dp else 8.dp
            ),
            colors = CardDefaults.cardColors(containerColor = medalColor.copy(alpha = 0.2f))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = place.toString(),
                    style = when {
                        !isTablet         -> MaterialTheme.typography.titleLarge
                        isTabletLandscape -> MaterialTheme.typography.headlineMedium
                        else              -> MaterialTheme.typography.displayMedium
                    },
                    fontWeight = FontWeight.Black,
                    color = medalColor.copy(alpha = 0.35f)
                )
            }
        }
    }
}

@Composable
private fun PlayerCard(
    player: Player,
    isSelected: Boolean,
    isTabletPortrait: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
        else Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (isTabletPortrait) 20.dp else 12.dp,
                    vertical = if (isTabletPortrait) 18.dp else 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(if (isTabletPortrait) 52.dp else 34.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    player.name.take(1).uppercase(),
                    style = if (isTabletPortrait) MaterialTheme.typography.titleLarge
                    else MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.onPrimary()
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(if (isTabletPortrait) 16.dp else 12.dp))
            Text(
                player.name,
                style = if (isTabletPortrait) MaterialTheme.typography.titleMedium
                else MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun VoteProgressHeader(votedCount: Int, totalCount: Int, isTabletPortrait: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTabletPortrait) 16.dp else 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(if (isTabletPortrait) 20.dp else 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Abgestimmt",
                    style = if (isTabletPortrait) MaterialTheme.typography.titleMedium
                    else MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "$votedCount/$totalCount",
                    style = if (isTabletPortrait) MaterialTheme.typography.titleMedium
                    else MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(if (isTabletPortrait) 12.dp else 6.dp))
            LinearProgressIndicator(
                progress = { if (totalCount > 0) votedCount.toFloat() / totalCount.toFloat() else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isTabletPortrait) 12.dp else 6.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun QuestionCard(question: String, isTabletPortrait: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTabletPortrait) 20.dp else 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Text(
            text = question,
            style = if (isTabletPortrait) MaterialTheme.typography.headlineSmall
            else MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(if (isTabletPortrait) 24.dp else 16.dp),
            lineHeight = if (isTabletPortrait) 36.sp else 22.sp
        )
    }
}

@Composable
private fun ConfettiAnimation(particles: List<ConfettiParticle>, transition: InfiniteTransition) {
    val time by transition.animateFloat(
        0f, 1000f,
        infiniteRepeatable(tween(8000, easing = LinearEasing)),
        "confetti"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
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

@Composable
fun MaterialTheme.onPrimary() = MaterialTheme.colorScheme.onPrimary

data class ConfettiParticle(
    val initialX: Float,
    val initialY: Float,
    val speed: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val color: Color,
    val size: Float
)
