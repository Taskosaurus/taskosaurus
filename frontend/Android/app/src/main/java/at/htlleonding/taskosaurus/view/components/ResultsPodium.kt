package at.htlleonding.taskosaurus.view.components

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.data.model.Answer
import at.htlleonding.taskosaurus.data.model.ConfettiParticle
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.data.model.RankedGroup
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.podiumTitle
import kotlin.random.Random

@Composable
fun ResultsPodium(
    question: Question,
    votedCount: Int,
    totalCount: Int,
    dims: AppDimensions
) {
    val rankedGroups = buildOlympicRanks(question.answers.sortedByDescending { it.count })
    val rank1 = rankedGroups.find { it.place == 1 }
    val rank2 = rankedGroups.find { it.place == 2 }
    val rank3 = rankedGroups.find { it.place == 3 }

    val infiniteTransition = rememberInfiniteTransition(label = "podium")
    val confettiParticles = remember {
        List(30) {
            ConfettiParticle(
                Random.nextFloat(),
                -0.1f - Random.nextFloat() * 0.2f,
                Random.nextFloat() * 2f + 1f,
                Random.nextFloat() * 360f,
                Random.nextFloat() * 4f - 2f,
                listOf(
                    Color(0xFFFFD700), Color(0xFFFF6B9D), Color(0xFF4CAF50),
                    Color(0xFF2196F3), Color(0xFFFF9800), Color(0xFF9C27B0)
                ).random(),
                Random.nextFloat() * 8f + 4f
            )
        }
    }

    val podiumRowH  = if (dims.isLandscape && !dims.isTablet) 160.dp else dims.podiumRowHeight
    val bar1        = if (dims.isLandscape && !dims.isTablet) 90.dp  else dims.podiumBar1
    val bar2        = if (dims.isLandscape && !dims.isTablet) 65.dp  else dims.podiumBar2
    val bar3        = if (dims.isLandscape && !dims.isTablet) 50.dp  else dims.podiumBar3
    val colW        = if (dims.isLandscape && !dims.isTablet) 70.dp  else dims.podiumColWidth
    val avatarSz    = if (dims.isLandscape && !dims.isTablet) 28.dp  else dims.podiumAvatarSize
    val tinyAvatSz  = if (dims.isLandscape && !dims.isTablet) 20.dp  else dims.podiumTinyAvatarSize
    val medalSz     = if (dims.isLandscape && !dims.isTablet) 18.sp  else dims.podiumMedalSize
    val colSpacing  = if (dims.isLandscape && !dims.isTablet) 6.dp   else dims.podiumColSpacing

    Box(modifier = Modifier.fillMaxSize()) {
        ConfettiAnimation(confettiParticles, infiniteTransition)
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (votedCount == totalCount) stringResource(R.string.game_results_winners) else stringResource(R.string.game_results_intermediate),
                style = dims.podiumTitle(), fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = if (!dims.isLandscape) 32.dp else if (!dims.isTablet) 4.dp else if (dims.isTablet) 16.dp else 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(podiumRowH),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                if (rank2 != null) {
                    PodiumColumn(rank2, votedCount, bar2, infiniteTransition, dims, colW, avatarSz, tinyAvatSz, medalSz)
                    Spacer(Modifier.width(colSpacing))
                }
                if (rank1 != null) PodiumColumn(rank1, votedCount, bar1, infiniteTransition, dims, colW, avatarSz, tinyAvatSz, medalSz)
                if (rank3 != null) {
                    Spacer(Modifier.width(colSpacing))
                    PodiumColumn(rank3, votedCount, bar3, infiniteTransition, dims, colW, avatarSz, tinyAvatSz, medalSz)
                }
            }
        }
    }
}

private fun buildOlympicRanks(sortedAnswers: List<Answer>): List<RankedGroup> {
    if (sortedAnswers.isEmpty()) return emptyList()
    val result = mutableListOf<RankedGroup>()
    var currentPlace = 1; var i = 0
    while (i < sortedAnswers.size && currentPlace <= 3) {
        val currentCount = sortedAnswers[i].count
        val tied = mutableListOf<Answer>(); var j = i
        while (j < sortedAnswers.size && sortedAnswers[j].count == currentCount) { tied.add(sortedAnswers[j]); j++ }
        result.add(RankedGroup(currentPlace, tied))
        currentPlace += tied.size; i = j
    }
    return result
}

