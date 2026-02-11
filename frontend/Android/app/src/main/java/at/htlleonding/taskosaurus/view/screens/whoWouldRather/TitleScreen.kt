package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun TitleScreen(
    onOpenGameList: () -> Unit,
    onGameCreated: (Int) -> Unit,
    viewModel: ViewModel = viewModel()
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var groupName by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val questions by viewModel.randomQuestions.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "background")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val playButtonScale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = EaseInOut),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "playPulse"
                )

                ExtendedFloatingActionButton(
                    onClick = { onOpenGameList() },
                    icon = { Icon(Icons.Default.PlayArrow, null) },
                    text = { Text("Jetzt spielen") },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.scale(playButtonScale)
                )

                ExtendedFloatingActionButton(
                    onClick = { showCreateDialog = true },
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text("Spiel erstellen") },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val gradientColors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                MaterialTheme.colorScheme.surface
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = gradientColors
                        )
                    )
            )

            FloatingQuestionCards(infiniteTransition, questions)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val iconScale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = EaseInOutCubic),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "iconPulse"
                )

                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(iconScale),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.extraLarge,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.People,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Wer würde eher?",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Entdecke was deine Freunde wählen würden",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { if (!isSubmitting) showCreateDialog = false },
            title = { Text("Neues Spiel erstellen") },
            text = {
                Column {
                    Text(
                        "Gib deiner Gruppe einen Namen, um zu starten.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        label = { Text("Gruppenname") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSubmitting
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = groupName.isNotBlank() && !isSubmitting,
                    onClick = {
                        isSubmitting = true
                        viewModel.createGroup(
                            name = groupName,
                            onSuccess = { newGroup ->
                                isSubmitting = false
                                showCreateDialog = false
                                onGameCreated(newGroup.id)
                            },
                            onError = { isSubmitting = false }
                        )
                    }
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Erstellen")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCreateDialog = false },
                    enabled = !isSubmitting
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }
}

@Composable
fun FloatingQuestionCards(
    infiniteTransition: InfiniteTransition,
    questions: List<Question>
) {

    questions.forEachIndexed { index, question ->
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 30f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 3000 + index * 500,
                    easing = EaseInOutCubic
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "cardFloat$index"
        )

        val rotation by infiniteTransition.animateFloat(
            initialValue = -2f,
            targetValue = 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000 + index * 300,
                    easing = EaseInOut
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "cardRotate$index"
        )

        val positions = listOf(
            Offset(0.1f, 0.15f),
            Offset(0.75f, 0.25f),
            Offset(0.15f, 0.75f),
            Offset(0.8f, 0.7f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .offset(
                        x = (positions[index].x * 300).dp,
                        y = (positions[index].y * 600).dp + offsetY.dp
                    )
                    .rotate(rotation)
                    .alpha(0.3f)
                    .width(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = question.shortenedQuestion,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
