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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions
import at.htlleonding.taskosaurus.ui.theme.displayTitle
import at.htlleonding.taskosaurus.ui.theme.subtitleText
import at.htlleonding.taskosaurus.view.components.FloatingQuestionCards
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel
import at.htlleonding.taskosaurus.view.components.dialog.CreateGroupDialog

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

            Text(
                text = stringResource(R.string.title_screen_headline),
                style = dims.displayTitle(), fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(dims.titleSpacerAfterSubtitle))

            Text(
                text = stringResource(R.string.title_screen_subtitle),
                style = dims.subtitleText(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = dims.titleMaxWidth)
            )
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
                text = { Text(
                    text = stringResource(R.string.btn_play_now),
                    style = if (!dims.isLandscape) MaterialTheme.typography.labelLarge else MaterialTheme.typography.titleMedium
                ) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.scale(playScale).height(dims.fabHeight)
            )
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                icon = { Icon(Icons.Default.Add, null, modifier = Modifier.size(dims.fabIconSize)) },
                text = { Text(
                    text = stringResource(R.string.btn_create_game),
                    style = if (!dims.isLandscape) MaterialTheme.typography.labelLarge else MaterialTheme.typography.titleMedium
                ) },
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