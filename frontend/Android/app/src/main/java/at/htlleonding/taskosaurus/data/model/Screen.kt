package at.htlleonding.taskosaurus.data.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import at.htlleonding.taskosaurus.R


sealed class Screen(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    object Games : Screen(
        route = "games",
        title = R.string.screen_games,
        icon = Icons.Default.SportsEsports
    )

    object Settings : Screen(
        route = "settings",
        title = R.string.screen_settings,
        icon = Icons.Default.Settings
    )
}