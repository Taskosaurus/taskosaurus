package at.htlleonding.taskosaurus.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Games : Screen(
        route = "games",
        title = "Spielen",
        icon = Icons.Default.SportsEsports
    )

    object Settings : Screen(
        route = "settings",
        title = "Einstellungen",
        icon = Icons.Default.Settings
    )
}