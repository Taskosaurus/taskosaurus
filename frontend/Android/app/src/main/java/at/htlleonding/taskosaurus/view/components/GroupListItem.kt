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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    animationDelay: Int = 0,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    // Tablet Portrait braucht größere Karten; im Landscape teilt der Split die Karten schon auf
    val isTabletPortrait = configuration.screenWidthDp >= 600 && !isLandscape

    val infiniteTransition = rememberInfiniteTransition(label = "waitingPuls")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 3.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(animationDelay)
        ),
        label = "waveScale"
    )
    val waveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(animationDelay)
        ),
        label = "waveAlpha"
    )

    // Größen abhängig vom Kontext
    val cardPadding = if (isTabletPortrait) 20.dp else 16.dp
    val iconSize = if (isTabletPortrait) 30.dp else 24.dp
    val dotSize = if (isTabletPortrait) 18.dp else 14.dp
    val dotBoxSize = if (isTabletPortrait) 30.dp else 24.dp
    val leaderIconSize = if (isTabletPortrait) 22.dp else 18.dp
    val spacerAfterIcon = if (isTabletPortrait) 14.dp else 12.dp
    val leaderRowSpacing = if (isTabletPortrait) 16.dp else 12.dp
    val leaderSurfaceSpacing = if (isTabletPortrait) 14.dp else 12.dp
    val questionSpacing = if (isTabletPortrait) 10.dp else 8.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) Color(0xFF2C2C2C) else Color(0xFFEEEEEE)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isAnswered) {
                        val statusColor = if (votedCount == totalCount) {
                            if (isDark) Color(0xFF81C784) else Color(0xFF43A047)
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                        Icon(
                            imageVector = if (votedCount == totalCount) Icons.Default.CheckCircle
                            else Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(iconSize)
                        )
                    } else {
                        Box(
                            modifier = Modifier.size(dotBoxSize),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(dotSize)
                                    .graphicsLayer {
                                        scaleX = waveScale
                                        scaleY = waveScale
                                        alpha = waveAlpha
                                    }
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(dotSize)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    .border(
                                        1.5.dp,
                                        if (isDark) Color(0xFF1E1E1E) else Color.White,
                                        CircleShape
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(spacerAfterIcon))

                    Text(
                        text = group.name,
                        style = if (isTabletPortrait) MaterialTheme.typography.titleLarge
                        else MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "$votedCount/$totalCount",
                    style = if (isTabletPortrait) MaterialTheme.typography.titleSmall
                    else MaterialTheme.typography.labelLarge,
                    color = if (isDark) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (!shortenedQuestion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(questionSpacing))
                Text(
                    text = shortenedQuestion,
                    style = if (isTabletPortrait) MaterialTheme.typography.bodyLarge
                    else MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isAnswered && !currentLeader.isNullOrBlank() && votedCount > 0) {
                Spacer(modifier = Modifier.height(leaderRowSpacing))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(if (isTabletPortrait) 14.dp else 10.dp),
                    color = if (isDark) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    border = if (isDark) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) else null
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = if (isTabletPortrait) 16.dp else 12.dp,
                            vertical = if (isTabletPortrait) 12.dp else 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(leaderSurfaceSpacing)
                    ) {
                        Icon(
                            imageVector = if (votedCount == totalCount) Icons.Default.EmojiEvents
                            else Icons.Default.Leaderboard,
                            contentDescription = null,
                            tint = if (votedCount == totalCount) Color(0xFFFFD54F)
                            else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(leaderIconSize)
                        )
                        Text(
                            text = currentLeader,
                            style = if (isTabletPortrait) MaterialTheme.typography.titleSmall
                            else MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        val percentage = (leaderVoteCount.toFloat() / votedCount.toFloat() * 100).toInt()
                        Text(
                            text = "$percentage%",
                            style = if (isTabletPortrait) MaterialTheme.typography.titleSmall
                            else MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
