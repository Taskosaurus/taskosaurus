package at.htlleonding.taskosaurus.view.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import at.htlleonding.taskosaurus.view.screens.auth.LoginRegisterView
import at.htlleonding.taskosaurus.view.screens.general.SettingsScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.GameListScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.GameScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.TitleScreen
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: ViewModel = viewModel()

    // Check if player exists - if not, show login
    val player by viewModel.player.collectAsState()
    val startDestination = if (player == null) "auth" else Screen.Games.route

    Scaffold(
        bottomBar = {
            // Only show bottom nav if logged in
            if (player != null) {
                MainNavigation(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            // Auth Route
            composable("auth") {
                LoginRegisterView(
                    viewModel = viewModel,
                    onSuccess = {
                        navController.navigate(Screen.Games.route) {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                )
            }

            // Games Navigation
            navigation(
                startDestination = "title",
                route = Screen.Games.route
            ) {
                composable("title") {
                    // ✅ START AUTO-REFRESH HERE!
                    LaunchedEffect(Unit) {
                        viewModel.startAutoRefresh()
                    }

                    TitleScreen(
                        onOpenGameList = {
                            navController.navigate("game_list")
                        },
                        onGameCreated = { groupId ->
                            navController.navigate("game/$groupId")
                            // TODO: navigate to the player list view, not the game view with question
                        },
                        viewModel = viewModel
                    )
                }

                composable("game_list") {
                    // Auto-refresh is already running from title screen
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
                        viewModel = viewModel
                    )
                }
            }

            // Settings Route
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onLogout = {
                        viewModel.logout()
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate("auth")
                    }
                )
            }
        }
    }
}