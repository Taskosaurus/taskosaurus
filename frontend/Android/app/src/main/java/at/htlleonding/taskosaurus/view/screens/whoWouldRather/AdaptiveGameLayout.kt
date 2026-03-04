package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun AdaptiveGameLayout(
    viewModel: ViewModel,
    isTabletLandscape: Boolean,
    onNavigateToGame: (Int) -> Unit
) {
    var selectedGroupId by remember { mutableStateOf<Int?>(null) }
    var showInfoRightSide by remember { mutableStateOf(false) }

    if (isTabletLandscape) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1.1f)) {
                GameListScreen(
                    viewModel = viewModel,
                    onGroupClick = { id ->
                        selectedGroupId = id
                        showInfoRightSide = false
                    },
                    isTabletSideBar = true
                )
            }

            VerticalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Box(modifier = Modifier.weight(2.5f)) {
                val currentId = selectedGroupId
                if (currentId != null) {
                    if (showInfoRightSide) {
                        GroupInfoScreen(
                            groupId = currentId,
                            viewModel = viewModel,
                            isTabletMode = true,
                            onBackToGame = { showInfoRightSide = false }
                        )
                    } else {
                        GameScreen(
                            groupId = currentId,
                            viewModel = viewModel,
                            onNavigateToGroupInfo = { showInfoRightSide = true },
                            isTabletMode = true
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.select_group_placeholder),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    } else {
        // Handy-Modus
        GameListScreen(viewModel = viewModel, onGroupClick = onNavigateToGame)
    }
}