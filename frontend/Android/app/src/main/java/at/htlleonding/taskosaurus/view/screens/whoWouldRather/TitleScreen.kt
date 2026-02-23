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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import kotlin.random.Random

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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    Box(modifier = Modifier.fillMaxSize()) {
        // Hintergrund Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        )

        // Mehr Karten im Hintergrund, besser verteilt
        FloatingQuestionCards(infiniteTransition, questions, isLandscape)

        // Haupt-Content (Zentriert)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp), // Mehr Padding für Kompaktheit
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
                    .size(if (isLandscape) 80.dp else 100.dp) // Kleiner im Querformat
                    .scale(iconScale),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.extraLarge,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.People,
                        contentDescription = null,
                        modifier = Modifier.size(if (isLandscape) 40.dp else 50.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (isLandscape) 24.dp else 40.dp))

            Text(
                text = "Wer würde eher?",
                style = if (isLandscape) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Text-Breite limitieren für kompakten Umbruch im Querformat
            Text(
                text = "Entdecke, was deine Freunde wählen würden",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = if (isLandscape) 220.dp else 300.dp)
            )
        }

        // FAB Buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
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

    if (showCreateDialog) {
        CreateGroupDialog(
            groupName = groupName,
            onGroupNameChange = { groupName = it },
            isSubmitting = isSubmitting,
            onDismiss = { showCreateDialog = false },
            onConfirm = {
                isSubmitting = true
                viewModel.createGroup(groupName, { isSubmitting = false; showCreateDialog = false; onGameCreated(it.id) }, { isSubmitting = false })
            }
        )
    }
}

@Composable
fun FloatingQuestionCards(
    infiniteTransition: InfiniteTransition,
    questions: List<Question>,
    isLandscape: Boolean
) {
    val portraitPositions = listOf(
        Offset(0.1f, 0.15f),
        Offset(0.75f, 0.25f),
        Offset(0.15f, 0.75f),
        Offset(0.8f, 0.7f)
    )

    // Landscape: Radikaler Versatz für einen dynamischen Fluss
    val landscapePositions = listOf(
        // Links-Bereich
        Offset(0.08f, 0.15f), // Oben Links
        Offset(0.05f, 0.65f), // Unten Links (weit außen)

        // Rechts-Bereich nach deinen Wünschen:
        Offset(0.74f, 0.12f), // 1. Oben Rechts: "Relativ ganz oben rechts"
        Offset(0.64f, 0.40f), // 2. Mitte Rechts: "Weiter in die Mitte"
        Offset(0.42f, 0.77f), // 3. Unten Rechts: "Viel mehr nach links"

        // Füller
        Offset(0.35f, 0.10f)  // Oben Mitte
    )

    val finalPositions = if (isLandscape) landscapePositions else portraitPositions
    val displayQuestions = if (questions.isEmpty()) emptyList()
    else List(finalPositions.size) { questions[it % questions.size] }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenW = maxWidth
        val screenH = maxHeight

        displayQuestions.forEachIndexed { index, question ->
            val pos = finalPositions[index]

            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 30f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 3000 + index * 500, easing = EaseInOutCubic),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "cardFloat$index"
            )

            val rotation by infiniteTransition.animateFloat(
                initialValue = if (index % 2 == 0) -3f else 3f,
                targetValue = if (index % 2 == 0) 3f else -3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000 + index * 300, easing = EaseInOut),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "cardRotate$index"
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Card(
                    modifier = Modifier
                        .offset(
                            x = if (isLandscape) (screenW * pos.x) else (pos.x * 300).dp,
                            y = if (isLandscape) {
                                (screenH * pos.y) + offsetY.dp - 15.dp
                            } else {
                                (pos.y * 600).dp + offsetY.dp
                            }
                        )
                        .rotate(rotation)
                        .alpha(if (isLandscape) 0.15f else 0.3f)
                        .width(125.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
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
}

@Composable
private fun CreateGroupDialog(
    groupName: String,
    onGroupNameChange: (String) -> Unit,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Neues Spiel erstellen") },
        text = {
            Column {
                Text("Gib deiner Gruppe einen Namen.", Modifier.padding(bottom = 16.dp))
                OutlinedTextField(
                    value = groupName,
                    onValueChange = onGroupNameChange,
                    label = { Text("Gruppenname") },
                    singleLine = true,
                    enabled = !isSubmitting
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, enabled = groupName.isNotBlank() && !isSubmitting) {
                if (isSubmitting) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Erstellen")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) { Text("Abbrechen") }
        }
    )
}