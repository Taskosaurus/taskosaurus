package at.htlleonding.taskosaurus.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions

@Composable
fun SectionHeader(text: String) {
    val dims = LocalAppDimensions.current
    Text(
        text = text,
        style = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 2.dp)
    )
}