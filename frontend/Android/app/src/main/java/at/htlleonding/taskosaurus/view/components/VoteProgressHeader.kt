package at.htlleonding.taskosaurus.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.progressText

@Composable
fun VoteProgressHeader(votedCount: Int, totalCount: Int, dims: AppDimensions) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dims.questionCardRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(dims.progressCardPadding)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(stringResource(R.string.game_voted_label), style = dims.progressText(), color = MaterialTheme.colorScheme.primary)
                Text("$votedCount/$totalCount", style = dims.progressText(), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(dims.itemSpacing))
            LinearProgressIndicator(
                progress = { if (totalCount > 0) votedCount.toFloat() / totalCount.toFloat() else 0f },
                modifier = Modifier.fillMaxWidth().height(dims.progressBarHeight).clip(CircleShape)
            )
        }
    }
}