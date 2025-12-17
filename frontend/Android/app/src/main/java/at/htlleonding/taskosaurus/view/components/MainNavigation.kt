package at.htlleonding.taskosaurus.view.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import at.htlleonding.taskosaurus.model.Screen
import at.htlleonding.taskosaurus.view.utility.currentRouteHelper

@Composable
fun MainNavigation(navController: NavController) {
    val items = listOf(
        Screen.Games,
        Screen.Settings
    )

    NavigationBar {
        val currentRoute = currentRouteHelper(navController)

        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(screen.title)
                }
            )
        }
    }
}