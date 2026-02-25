package at.htlleonding.taskosaurus.view.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.data.model.Group
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions
import at.htlleonding.taskosaurus.ui.theme.groupName
import at.htlleonding.taskosaurus.ui.theme.groupQuestion
import at.htlleonding.taskosaurus.ui.theme.labelText

@Composable
fun GroupListItem(
    group: Group,
    votedCount: Int = 0,
    totalCount: Int = 0,
    isAnswered: Boolean = false,
    shortenedQuestion: String? = null,
    currentLeader: String? = null,
    leaderVoteCount: Int = 0,
    animationDelay: Int = 0,
    onClick: () -> Unit
) {
    val dims = LocalAppDimensions.current
    val isDark = isSystemInDarkTheme()

    val infiniteTransition = rememberInfiniteTransition(label = "dot")
    val waveScale by infiniteTransition.animateFloat(
        1f, 3.5f,
        infiniteRepeatable(tween(1500, easing = LinearOutSlowInEasing), RepeatMode.Restart, StartOffset(animationDelay)),
        "waveScale"
    )
    val waveAlpha by infiniteTransition.animateFloat(
        0.8f, 0f,
        infiniteRepeatable(tween(1500, easing = LinearOutSlowInEasing), RepeatMode.Restart, StartOffset(animationDelay)),
        "waveAlpha"
    )

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White),
        border = BorderStroke(1.dp, if (isDark) Color(0xFF2C2C2C) else Color(0xFFEEEEEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(dims.groupCardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    if (isAnswered) {
                        val color = if (votedCount == totalCount)
                            (if (isDark) Color(0xFF81C784) else Color(0xFF43A047))
                        else MaterialTheme.colorScheme.primary
                        Icon(
                            if (votedCount == totalCount) Icons.Default.CheckCircle else Icons.Default.HourglassBottom,
                            null, tint = color, modifier = Modifier.size(dims.groupIconSize)
                        )
                    } else {
                        val dotBoxSize = dims.groupIconSize + 6.dp
                        Box(modifier = Modifier.size(dotBoxSize), contentAlignment = Alignment.Center) {
                            Box(modifier = Modifier
                                .size(dims.groupDotSize)
                                .graphicsLayer { scaleX = waveScale; scaleY = waveScale; alpha = waveAlpha }
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), CircleShape))
                            Box(modifier = Modifier
                                .size(dims.groupDotSize)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .border(1.5.dp, if (isDark) Color(0xFF1E1E1E) else Color.White, CircleShape))
                        }
                    }
                    Spacer(modifier = Modifier.width(if (dims.isTablet) 14.dp else 12.dp))
                    Text(
                        group.name,
                        style = dims.groupName(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    "$votedCount/$totalCount",
                    style = dims.labelText(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (!shortenedQuestion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(if (dims.isTablet) 10.dp else 8.dp))
                Text(
                    shortenedQuestion,
                    style = dims.groupQuestion(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2, overflow = TextOverflow.Ellipsis
                )
            }

            if (isAnswered && !currentLeader.isNullOrBlank() && votedCount > 0) {
                Spacer(modifier = Modifier.height(if (dims.isTablet) 16.dp else 12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dims.groupLeaderRadius),
                    color = if (isDark) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    border = if (isDark) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) else null
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = dims.groupLeaderPaddingH, vertical = dims.groupLeaderPaddingV),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(if (dims.isTablet) 14.dp else 12.dp)
                    ) {
                        Icon(
                            if (votedCount == totalCount) Icons.Default.EmojiEvents else Icons.Default.Leaderboard,
                            null,
                            tint = if (votedCount == totalCount) Color(0xFFFFD54F) else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(dims.groupLeaderIconSize)
                        )
                        Text(
                            currentLeader,
                            style = dims.groupQuestion(),
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        val pct = (leaderVoteCount.toFloat() / votedCount.toFloat() * 100).toInt()
                        Text(
                            "$pct%",
                            style = dims.labelText(),
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
