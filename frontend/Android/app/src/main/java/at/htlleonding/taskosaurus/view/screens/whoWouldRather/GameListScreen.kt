package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.view.components.GroupListItem
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun GameListScreen(
    viewModel: ViewModel = viewModel(),
    onGroupClick: (Int) -> Unit
) {
    val hasConnection by viewModel.hasConnection.collectAsState()
    val questions by viewModel.latestQuestions.collectAsState()

    // Use computed properties from ViewModel
    val unansweredGroups = viewModel.unAnsweredGroups
    val answeredGroups = viewModel.answeredGroups

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (!hasConnection) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Keine Verbindung zum Server",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Nicht beantwortet Section
        if (unansweredGroups.isNotEmpty()) {
            item {
                Text(
                    text = "Nicht beantwortet",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
                )
            }

            items(unansweredGroups) { group ->
                val question = questions[group.id]
                val votedCount = question?.answers?.sumOf { it.count } ?: 0
                val totalCount = group.players?.size ?: 0

                GroupListItem(
                    group = group,
                    votedCount = votedCount,
                    totalCount = totalCount,
                    isAnswered = false,
                    onClick = { onGroupClick(group.id) }
                )
            }
        }

        // Beantwortet Section
        if (answeredGroups.isNotEmpty()) {
            item {
                Text(
                    text = "Beantwortet",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, top = 16.dp, bottom = 4.dp)
                )
            }

            items(answeredGroups) { group ->
                val question = questions[group.id]
                val votedCount = question?.answers?.sumOf { it.count } ?: 0
                val totalCount = group.players?.size ?: 0

                GroupListItem(
                    group = group,
                    votedCount = votedCount,
                    totalCount = totalCount,
                    isAnswered = true,
                    onClick = { onGroupClick(group.id) }
                )
            }
        }
    }
}