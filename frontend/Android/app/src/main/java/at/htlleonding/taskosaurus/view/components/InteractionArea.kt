package at.htlleonding.taskosaurus.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.data.model.Group
import at.htlleonding.taskosaurus.data.model.Player
import at.htlleonding.taskosaurus.data.model.Question
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.voteButtonText

@Composable
fun InteractionArea(
    question: Question, group: Group, selectedPlayer: Player?,
    dims: AppDimensions, isPhoneLandscape: Boolean,
    onPlayerSelect: (Player) -> Unit, onVoteSubmit: () -> Unit
) {
    if (question.answered) {
        ResultsPodium(question, question.answers.sumOf { it.count }, group.players?.size ?: 0, dims)
    } else {
        Column(Modifier.fillMaxSize()) {
            VotingSection(group.players ?: emptyList(), selectedPlayer, Modifier.weight(1f), dims, onPlayerSelect)
            Spacer(Modifier.height(dims.itemSpacing))
            Button(
                onClick = onVoteSubmit,
                enabled = selectedPlayer != null,
                modifier = Modifier.fillMaxWidth().height(dims.voteButtonHeight),
                shape = RoundedCornerShape(dims.questionCardRadius)
            ) {
                Text(stringResource(R.string.game_btn_vote), style = dims.voteButtonText(), fontWeight = FontWeight.Bold)
            }
        }
    }
}