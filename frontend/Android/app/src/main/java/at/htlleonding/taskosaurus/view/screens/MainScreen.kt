package at.htlleonding.taskosaurus.view.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import at.htlleonding.taskosaurus.data.model.Screen
import at.htlleonding.taskosaurus.view.components.MainNavigation
import at.htlleonding.taskosaurus.view.screens.general.SettingsScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.GameListScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.TitleScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            MainNavigation(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Games.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            navigation(
                startDestination = "title",
                route = "games"
            ) {

                composable("title") {
                    TitleScreen(
                        onOpenGameList = {
                            navController.navigate("game_list")
                        }
                    )
                }

                composable("game_list") {
                    GameListScreen()
                }
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}