package at.htlleonding.taskosaurus.view.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxHeight()
            .width(if (isExpanded) 240.dp else 120.dp)
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Start + WindowInsetsSides.Bottom))
        ) {
            IconButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .align(Alignment.CenterHorizontally)
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
                        isExpanded = false

                        if (screen == Screen.Games) {
                            navController.navigate("title") {
                                popUpTo(Screen.Games.route) {
                                    inclusive = false
                                    saveState = false
                                }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() },
        shape = CircleShape,
        color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = screen.icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
            )

            if (isExpanded) {
                Spacer(Modifier.width(12.dp))
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