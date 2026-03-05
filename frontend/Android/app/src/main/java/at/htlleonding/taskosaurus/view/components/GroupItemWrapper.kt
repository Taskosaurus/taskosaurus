package at.htlleonding.taskosaurus.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import at.htlleonding.taskosaurus.data.model.Group
import at.htlleonding.taskosaurus.data.model.Question

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupItemWrapper(
    group: Group,
    questions: Map<Int, Question>,
    isAnswered: Boolean,
    animationDelay: Int,
    onGroupClick: (Int) -> Unit
) {
    val question = questions[group.id]

    Box() {
        GroupListItem(
            group = group,
            votedCount = question?.answers?.sumOf { it.count } ?: 0,
            totalCount = group.players?.size ?: 0,
            isAnswered = isAnswered,
            shortenedQuestion = question?.shortenedQuestion,
            currentLeader = if (isAnswered) question?.currentLeader else null,
            leaderVoteCount = if (isAnswered) question?.answers?.maxByOrNull { it.count }?.count ?: 0 else 0,
            animationDelay = animationDelay,
            onClick = { onGroupClick(group.id) }
        )
    }
}