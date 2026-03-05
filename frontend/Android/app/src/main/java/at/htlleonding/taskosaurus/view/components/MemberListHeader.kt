package at.htlleonding.taskosaurus.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.R
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.heading2
import at.htlleonding.taskosaurus.ui.theme.labelText

@Composable
fun MemberListHeader(count: Int, dims: AppDimensions) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(R.string.info_members_header), style = dims.heading2(), fontWeight = FontWeight.Bold)
        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) {
            Text("$count",
                modifier = Modifier.padding(
                    horizontal = if (dims.isTablet) 14.dp else 10.dp,
                    vertical = if (dims.isTablet) 4.dp else 2.dp),
                style = dims.labelText(), fontWeight = FontWeight.Bold)
        }
    }
}