package at.htlleonding.taskosaurus.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.questionText

@Composable
fun QuestionCard(question: String, dims: AppDimensions) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dims.questionCardRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Text(question, style = dims.questionText(), fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(dims.questionCardPadding),
            lineHeight = if (!dims.isLandscape) 36.sp else if (dims.isTablet) 28.sp else 22.sp)
    }
}