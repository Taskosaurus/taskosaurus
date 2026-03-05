package at.htlleonding.taskosaurus.view.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.data.model.RankedGroup
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.labelText
import at.htlleonding.taskosaurus.ui.theme.podiumAvatarLetter
import at.htlleonding.taskosaurus.ui.theme.podiumBarNumber
import at.htlleonding.taskosaurus.ui.theme.podiumName

@Composable
fun PodiumColumn(
    rankedGroup: RankedGroup,
    votedCount: Int,
    barHeight: Dp,
    infiniteTransition: InfiniteTransition,
    dims: AppDimensions,
    colWidth: Dp,
    avatarSize: Dp,
    tinyAvatarSize: Dp,
    medalSize: TextUnit
) {
    val place = rankedGroup.place
    val medalColor = when (place) { 1 -> Color(0xFFFFD700); 2 -> Color(0xFFC0C0C0); 3 -> Color(0xFFCD7F32); else -> MaterialTheme.colorScheme.primary }
    val medalEmoji = when (place) { 1 -> "🥇"; 2 -> "🥈"; 3 -> "🥉"; else -> "" }

    val scale by infiniteTransition.animateFloat(
        1f, if (place == 1) 1.04f else 1f,
        infiniteRepeatable(tween(1500, easing = EaseInOut), RepeatMode.Reverse), "scale$place"
    )

    Column(
        modifier = Modifier.width(colWidth).scale(if (place == 1) scale else 1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(medalEmoji, fontSize = medalSize,
            modifier = Modifier.padding(bottom = if (!dims.isLandscape) 10.dp else if (dims.isTablet) 4.dp else 2.dp))

        if (rankedGroup.entries.size == 1) {
            Box(modifier = Modifier.size(avatarSize).clip(CircleShape).background(medalColor), contentAlignment = Alignment.Center) {
                Text(rankedGroup.entries[0].answeredName.take(1).uppercase(),
                    style = dims.podiumAvatarLetter(), fontWeight = FontWeight.Bold, color = Color.White)
            }
        } else {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.widthIn(max = colWidth)) {
                rankedGroup.entries.take(3).forEachIndexed { idx, entry ->
                    if (idx > 0) Spacer(Modifier.width(if (dims.isTablet) 4.dp else 2.dp))
                    Box(modifier = Modifier.size(tinyAvatarSize).clip(CircleShape).background(medalColor), contentAlignment = Alignment.Center) {
                        Text(entry.answeredName.take(1).uppercase(), style = dims.labelText(), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        Spacer(Modifier.height(if (!dims.isLandscape) 12.dp else if (dims.isTablet) 6.dp else 4.dp))

        rankedGroup.entries.forEach { entry ->
            Text(entry.answeredName, style = dims.podiumName(), fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = colWidth),
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        if (votedCount > 0) {
            Text("%.0f%%".format(rankedGroup.entries[0].count.toFloat() / votedCount.toFloat() * 100),
                style = dims.labelText(), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(Modifier.height(if (!dims.isLandscape) 14.dp else if (dims.isTablet) 8.dp else 4.dp))

        Card(
            modifier = Modifier.width(colWidth - 6.dp).height(barHeight),
            shape = RoundedCornerShape(topStart = dims.cardRadius, topEnd = dims.cardRadius),
            colors = CardDefaults.cardColors(containerColor = medalColor.copy(alpha = 0.2f))
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(place.toString(), style = dims.podiumBarNumber(), fontWeight = FontWeight.Black, color = medalColor.copy(alpha = 0.35f))
            }
        }
    }
}