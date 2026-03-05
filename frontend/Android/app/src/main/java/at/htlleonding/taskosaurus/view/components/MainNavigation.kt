package at.htlleonding.taskosaurus.view.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import at.htlleonding.taskosaurus.data.model.Screen

@Composable
fun MainNavigation(navController: NavController) {
    val items = listOf(
        Screen.Games,
        Screen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val labelText = stringResource(id = screen.title)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
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
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = labelText
                    )
                },
                label = {
                    Text(labelText)
                }
            )
        }
    }
}