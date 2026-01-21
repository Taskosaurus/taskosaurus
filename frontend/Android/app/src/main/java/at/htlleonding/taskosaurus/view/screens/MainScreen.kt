package at.htlleonding.taskosaurus.view.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import at.htlleonding.taskosaurus.data.model.Screen
import at.htlleonding.taskosaurus.view.components.MainNavigation
import at.htlleonding.taskosaurus.view.screens.general.SettingsScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.GameListScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.GameScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.TitleScreen
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: ViewModel = viewModel()

    // Sprint Demo: Kein Login, direkt zu Games
    val startDestination = Screen.Games.route

    // 🔥 START AUTO-REFRESH SOFORT BEIM APP-START!
    LaunchedEffect(Unit) {
        viewModel.startAutoRefresh()
    }

    // Stop auto-refresh when leaving
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopAutoRefresh()
        }
    }

    Scaffold(
        bottomBar = {
            // Sprint Demo: Bottom Nav immer zeigen
            MainNavigation(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Games Navigation
            navigation(
                startDestination = "title",
                route = Screen.Games.route
            ) {
                composable("title") {
                    TitleScreen(
                        onOpenGameList = {
                            navController.navigate("game_list")
                        }
                    )
                }

                composable("game_list") {
                    GameListScreen(
                        viewModel = viewModel,
                        onGroupClick = { groupId ->
                            navController.navigate("game/$groupId")
                        }
                    )
                }

                composable(
                    route = "game/{groupId}",
                    arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
                    GameScreen(
                        groupId = groupId,
                        viewModel = viewModel,
                        onNavigateBack = {
                            navController.navigateUp()
                        }
                    )
                }
            }

            // Settings Route
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onLogout = {
                        viewModel.logout()
                    },
                    onNavigateToLogin = {
                        // Sprint Demo: Keine Login-Navigation
                    }
                )
            }
        }
    }
}