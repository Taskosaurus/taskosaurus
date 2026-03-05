package at.htlleonding.taskosaurus.view.components

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import at.htlleonding.taskosaurus.data.model.Question

@Composable
fun FloatingQuestionCards(
    infiniteTransition: InfiniteTransition,
    questions: List<Question>,
    isLandscape: Boolean,
    cardWidth: Dp,
    cardAlpha: Float
) {
    val portraitPositions = listOf(
        Offset(0.05f, 0.08f), Offset(0.70f, 0.06f),
        Offset(0.12f, 0.78f), Offset(0.72f, 0.72f),
    )

    val landscapePositions = listOf(
        Offset(0.04f, 0.08f), Offset(0.03f, 0.60f), Offset(0.72f, 0.06f),
        Offset(0.64f, 0.38f), Offset(0.40f, 0.75f), Offset(0.33f, 0.08f)
    )

    val positions = if (isLandscape) landscapePositions else portraitPositions

    if (questions.isEmpty()) return

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        positions.forEachIndexed { index, pos ->
            val question = questions[index % questions.size]

            FloatingQuestionCard(
                question = question,
                index = index,
                infiniteTransition = infiniteTransition,
                cardWidth = cardWidth,
                cardAlpha = cardAlpha,
                modifier = Modifier.offset(
                    x = maxWidth * pos.x,
                    y = maxHeight * pos.y
                )
            )
        }
    }
}