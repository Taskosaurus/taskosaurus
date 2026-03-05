package at.htlleonding.taskosaurus.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.data.model.Player
import at.htlleonding.taskosaurus.ui.theme.AppDimensions

@Composable
fun VotingSection(
    players: List<Player>, selectedPlayer: Player?,
    modifier: Modifier, dims: AppDimensions,
    onPlayerSelect: (Player) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dims.questionCardRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = dims.itemSpacing / 2)) {
            items(players) { player ->
                PlayerCard(player, selectedPlayer == player, dims) { onPlayerSelect(player) }
                if (player != players.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = dims.playerItemPaddingH),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}