package at.htlleonding.taskosaurus.view.components

import android.view.Surface
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import at.htlleonding.taskosaurus.data.model.Screen
import at.htlleonding.taskosaurus.ui.theme.AppDimensions
import at.htlleonding.taskosaurus.ui.theme.LocalAppDimensions

@Composable
fun MainNavigationRail(navController: NavController) {
    val dims = LocalAppDimensions.current
    var isExpanded by remember { mutableStateOf(false) }
    val items = listOf(Screen.Games, Screen.Settings)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val baseWidth = if (isExpanded) dims.railWidthExpanded else dims.railWidth
    val totalWidth = baseWidth

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxHeight().width(totalWidth).animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
                .padding(start = 0.dp)
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(if (dims.isTablet) 52.dp else 40.dp)
                    .clip(CircleShape)
                    .clickable { isExpanded = !isExpanded },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(dims.railIconSize))
            }
            Spacer(modifier = Modifier.height(20.dp))
            items.forEach { screen ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                NavRailItem(screen, isSelected, isExpanded, dims) {
                    val route = if (screen == Screen.Games) "title" else screen.route
                    navController.navigate(route) {
                        if (screen == Screen.Games) popUpTo(Screen.Games.route) { inclusive = false }
                        else { popUpTo(navController.graph.startDestinationId) { saveState = true }; restoreState = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

@Composable
fun NavRailItem(screen: Screen, isSelected: Boolean, isExpanded: Boolean, dims: AppDimensions, onClick: () -> Unit) {
    val labelText = stringResource(id = screen.title)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 10.dp)
            .height(dims.railItemHeight)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = if (isExpanded) 16.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
        ) {
            Icon(
                screen.icon, null,
                modifier = Modifier.size(dims.railIconSize),
                tint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
            )
            if (isExpanded) {
                Spacer(Modifier.width(16.dp))
                Text(
                    labelText,
                    style = if (dims.isTablet) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
