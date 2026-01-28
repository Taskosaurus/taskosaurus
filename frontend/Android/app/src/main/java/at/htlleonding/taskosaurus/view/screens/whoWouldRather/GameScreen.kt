package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.Player
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

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
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = group?.name ?: "Gruppe",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Group detail */ }) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Gruppenmitglieder"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (group == null || question == null) {
                // Loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Vote Progress Header
                    VoteProgressHeader(
                        votedCount = question.answers.sumOf { it.count },
                        totalCount = group.players?.size ?: 0
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Question Card
                    QuestionCard(question = question.question)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Content based on state
                    if (question.answered) {
                        // Show Results
                        ResultsSection(question = question)
                    } else {
                        // Show Voting UI
                        VotingSection(
                            players = group.players ?: emptyList(),
                            selectedPlayer = selectedPlayer,
                            onPlayerSelect = { selectedPlayer = it },
                            onVoteSubmit = {
                                selectedPlayer?.let { player ->
                                    viewModel.submitVote(
                                        groupId = groupId,
                                        answeredPlayerId = player.id,
                                        onSuccess = {
                                            selectedPlayer = null
                                        },
                                        onError = { error ->
                                            // Show error
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VoteProgressHeader(votedCount: Int, totalCount: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Abgestimmt: $votedCount/$totalCount",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { votedCount.toFloat() / totalCount.coerceAtLeast(1).toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun QuestionCard(question: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )
    }
}

@Composable
private fun VotingSection(
    players: List<Player>,
    selectedPlayer: Player?,
    onPlayerSelect: (Player) -> Unit,
    onVoteSubmit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Player List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(players) { player ->
                PlayerCard(
                    player = player,
                    isSelected = selectedPlayer == player,
                    onClick = { onPlayerSelect(player) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vote Button
        Button(
            onClick = onVoteSubmit,
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

@Composable
private fun PlayerCard(
    player: Player,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = player.name.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = player.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ResultsSection(question: Question) {
    val sortedAnswers = question.answers.sortedByDescending { it.count }
    val maxCount = sortedAnswers.firstOrNull()?.count ?: 1

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Ergebnisse",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                sortedAnswers.forEach { answer ->
                    ResultBar(
                        name = answer.answeredName,
                        count = answer.count,
                        maxCount = maxCount
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultBar(
    name: String,
    count: Int,
    maxCount: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(count.toFloat() / maxCount.coerceAtLeast(1).toFloat())
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}