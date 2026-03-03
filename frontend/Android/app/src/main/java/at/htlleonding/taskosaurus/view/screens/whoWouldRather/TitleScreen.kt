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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions
import at.htlleonding.taskosaurus.ui.theme.displayTitle
import at.htlleonding.taskosaurus.ui.theme.subtitleText
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import kotlin.random.Random

@Composable
fun TitleScreen(
    onOpenGameList: () -> Unit,
    onGameCreated: (Int) -> Unit,
    viewModel: ViewModel = viewModel()
) {
    val dims = LocalAppDimensions.current

    var showCreateDialog by remember { mutableStateOf(false) }
    var groupName by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val questions by viewModel.randomQuestions.collectAsState()
    val infiniteTransition = rememberInfiniteTransition(label = "bg")

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                MaterialTheme.colorScheme.surface
            ))
        ))

        FloatingQuestionCards(infiniteTransition, questions, dims.isLandscape, dims.floatingCardWidth, dims.floatingCardAlpha)

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val iconScale by infiniteTransition.animateFloat(
                1f, 1.1f,
                infiniteRepeatable(tween(2000, easing = EaseInOutCubic), RepeatMode.Reverse), "iconPulse"
            )
            Surface(
                modifier = Modifier.size(dims.titleIconSurface).scale(iconScale),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.extraLarge,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.People, null,
                        modifier = Modifier.size(dims.titleIconInner),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            Spacer(modifier = Modifier.height(dims.titleSpacerAfterIcon))

            Text("Wer würde eher?",
                style = dims.displayTitle(), fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(dims.titleSpacerAfterSubtitle))

            Text("Entdecke, was deine Freunde wählen würden",
                style = dims.subtitleText(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = dims.titleMaxWidth))
        }

        Column(
            modifier = Modifier.align(Alignment.BottomEnd).padding(dims.fabPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.fabSpacing)
        ) {
            val playScale by infiniteTransition.animateFloat(
                1f, 1.05f,
                infiniteRepeatable(tween(1000, easing = EaseInOut), RepeatMode.Reverse), "playPulse"
            )
            ExtendedFloatingActionButton(
                onClick = onOpenGameList,
                icon = { Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(dims.fabIconSize)) },
                text = { Text("Jetzt spielen",
                    style = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.scale(playScale).height(dims.fabHeight)
            )
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                icon = { Icon(Icons.Default.Add, null, modifier = Modifier.size(dims.fabIconSize)) },
                text = { Text("Spiel erstellen",
                    style = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge) },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.height(dims.fabHeight)
            )
        }
    }

    if (showCreateDialog) {
        CreateGroupDialog(groupName, { groupName = it }, isSubmitting,
            onDismiss = { showCreateDialog = false },
            onConfirm = {
                isSubmitting = true
                viewModel.createGroup(groupName,
                    { isSubmitting = false; showCreateDialog = false; onGameCreated(it.id) },
                    { isSubmitting = false })
            }
        )
    }
}

@Composable
fun FloatingQuestionCards(
    infiniteTransition: InfiniteTransition,
    questions: List<Question>,
    isLandscape: Boolean,
    cardWidth: Dp,
    cardAlpha: Float
) {
    val portraitPositions = listOf(
        Offset(0.05f, 0.08f),
        Offset(0.70f, 0.06f),
        Offset(0.12f, 0.78f),
        Offset(0.72f, 0.72f),
    )

    val landscapePositions = listOf(
        Offset(0.04f, 0.08f),
        Offset(0.03f, 0.60f),
        Offset(0.72f, 0.06f),
        Offset(0.64f, 0.38f),
        Offset(0.40f, 0.75f),
        Offset(0.33f, 0.08f)
    )

    val positions = if (isLandscape) landscapePositions else portraitPositions
    val displayQuestions = if (questions.isEmpty()) emptyList()
    else List(positions.size) { questions[it % questions.size] }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenW = maxWidth
        val screenH = maxHeight

        displayQuestions.forEachIndexed { index, question ->
            val pos = positions[index]
            val offsetY by infiniteTransition.animateFloat(
                0f, 28f,
                infiniteRepeatable(tween(3000 + index * 500, easing = EaseInOutCubic), RepeatMode.Reverse),
                "float$index"
            )
            val rotation by infiniteTransition.animateFloat(
                if (index % 2 == 0) -3f else 3f,
                if (index % 2 == 0) 3f else -3f,
                infiniteRepeatable(tween(2000 + index * 300, easing = EaseInOut), RepeatMode.Reverse),
                "rotate$index"
            )
            Box(modifier = Modifier.fillMaxSize()) {
                Card(
                    modifier = Modifier
                        .offset(x = screenW * pos.x, y = screenH * pos.y + offsetY.dp)
                        .rotate(rotation)
                        .alpha(cardAlpha)
                        .width(cardWidth),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Text(
                        text = question.shortenedQuestion,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        maxLines = 3, overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateGroupDialog(
    groupName: String, onGroupNameChange: (String) -> Unit,
    isSubmitting: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Neues Spiel erstellen") },
        text = {
            Column {
                Text("Gib deiner Gruppe einen Namen.", Modifier.padding(bottom = 16.dp))
                OutlinedTextField(value = groupName, onValueChange = onGroupNameChange,
                    label = { Text("Gruppenname") }, singleLine = true, enabled = !isSubmitting)
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, enabled = groupName.isNotBlank() && !isSubmitting) {
                if (isSubmitting) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Erstellen")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss, enabled = !isSubmitting) { Text("Abbrechen") } }
    )
}
