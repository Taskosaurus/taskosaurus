package at.htlleonding.taskosaurus.view.components

import android.R.attr.translationY
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htlleonding.taskosaurus.data.model.Question

@Composable
fun FloatingQuestionCard(
    question: Question,
    index: Int,
    infiniteTransition: InfiniteTransition,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 160.dp,
    cardAlpha: Float = 1f
) {
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable<Float>(
            animation = tween(3000 + index * 500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y_$index"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = if (index % 2 == 0) -3f else 3f,
        targetValue = if (index % 2 == 0) 3f else -3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000 + index * 300, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotate_$index"
    )

    Card(
        modifier = modifier
            .graphicsLayer {
                translationY = offsetY.dp.toPx()
                rotationZ = rotation
                alpha = cardAlpha
            }
            .width(cardWidth),
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