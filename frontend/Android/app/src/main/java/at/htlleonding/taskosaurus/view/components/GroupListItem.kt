package at.htlleonding.taskosaurus.view.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.data.model.Group

@Composable
fun GroupListItem(
    group: Group,
    votedCount: Int = 0,
    totalCount: Int = 0,
    isAnswered: Boolean = false,
    shortenedQuestion: String? = null,
    currentLeader: String? = null,
    leaderVoteCount: Int = 0,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waitingPuls")

    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 3.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveScale"
    )
    val waveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveAlpha"
    )


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Icon, Name, Vote Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Status Icon
                    if (isAnswered) {
                        if (votedCount == totalCount) {
                            // Every player has answered
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Runde beendet",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            // User has answered, but not every other player has yet answered
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = "Warten auf andere",
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else {
                        // User hasn't answered yet
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .graphicsLayer {
                                        scaleX = waveScale
                                        scaleY = waveScale
                                        alpha = waveAlpha
                                        clip = false
                                    }
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    .border(1.5.dp, Color.White, CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (totalCount > 0) {
                    Text(
                        text = "$votedCount/$totalCount",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Shortened Question
            if (!shortenedQuestion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = shortenedQuestion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Current Leader - Only show if answered
            if (isAnswered && !currentLeader.isNullOrBlank() && votedCount > 0) {
                Spacer(modifier = Modifier.height(12.dp))

                // Leader Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (votedCount == totalCount) {
                        // Trophy Icon - Winner was determined
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Gewinner",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        // Race-Flag Icon
                        Icon(
                            imageVector = Icons.Default.SportsScore,
                            contentDescription = "Race Flag",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Leader Name
                    Text(
                        text = currentLeader,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    // Percentage
                    val percentage = if (votedCount > 0) {
                        (leaderVoteCount.toFloat() / votedCount.toFloat() * 100).toInt()
                    } else {
                        0
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
