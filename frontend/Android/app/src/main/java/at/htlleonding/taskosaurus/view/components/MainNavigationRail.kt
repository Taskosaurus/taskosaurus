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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import at.htlleonding.taskosaurus.data.model.Screen

@Composable
fun MainNavigationRail(navController: NavController) {
    var isExpanded by remember { mutableStateOf(false) }
    val items = listOf(Screen.Games, Screen.Settings)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Erkennung der Rotation
    val context = LocalContext.current
    val display = context.display
    val rotation = display?.rotation ?: Surface.ROTATION_0

    // Wenn ROTATION_270 vorliegt, ist die Navigation Bar LINKS
    val needsLeftPadding = rotation == Surface.ROTATION_270

    // Die Basisbreite der Rail
    val baseWidth = if (isExpanded) 220.dp else 80.dp
    // Wir addieren 48dp (Standardgröße der Nav Bar), wenn sie links liegt
    val totalWidth = if (needsLeftPadding) baseWidth + 48.dp else baseWidth

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxHeight()
            .width(totalWidth)
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
                // Hier der entscheidende Fix:
                // Padding nur links, wenn die Bar links ist, damit der Inhalt nach rechts rutscht
                .padding(start = if (needsLeftPadding) 48.dp else 0.dp)
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Toggle Menu")
            }

            items.forEach { screen ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                NavigationItemRow(
                    screen = screen,
                    isSelected = isSelected,
                    isExpanded = isExpanded,
                    onClick = {
                        // Navigation Logik (unverändert)
                        val route = if (screen == Screen.Games) "title" else screen.route
                        navController.navigate(route) {
                            if (screen == Screen.Games) {
                                popUpTo(Screen.Games.route) { inclusive = false }
                            } else {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                restoreState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NavigationItemRow(
    screen: Screen,
    isSelected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .height(52.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isExpanded) 16.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
        ) {
            Icon(
                imageVector = screen.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
            )

            if (isExpanded) {
                Spacer(Modifier.width(16.dp))
                Text(
                    text = screen.title,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}