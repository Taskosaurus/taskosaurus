package at.htlleonding.taskosaurus.view.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.*
import at.htlleonding.taskosaurus.data.model.Screen
import at.htlleonding.taskosaurus.view.components.MainNavigation
import at.htlleonding.taskosaurus.view.screens.auth.LoginRegisterView
import at.htlleonding.taskosaurus.view.screens.general.SettingsScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.*
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: ViewModel = viewModel()

    // Check if player exists - if not, show login
    val player by viewModel.player.collectAsState()
    val isReady by viewModel.isReady.collectAsState()


    Scaffold(
        bottomBar = {
            // Only show bottom nav if logged in
            if (player != null) {
                MainNavigation(navController)
            }
        }
    ) { paddingValues ->
        if (isReady) {
            val startDestination = if (player != null) Screen.Games.route else "auth"
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
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
                    LaunchedEffect(Unit) {
                        viewModel.startAutoRefresh()
                    }

                    TitleScreen(
                        onOpenGameList = {
                            navController.navigate("game_list")
                        },
                        onGameCreated = { groupId ->
                            // Navigiert nach Erstellung direkt zur Mitgliederliste/QR-Code
                            navController.navigate("group_info/$groupId")
                        },
                        viewModel = viewModel
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
                        // Hier die Funktion übergeben, um zum Info-Screen zu kommen
                        onNavigateToGroupInfo = { id ->
                            navController.navigate("group_info/$id")
                        }
                    )
                }

                // NEU: Group Info Screen mit Deep Link Unterstützung
                composable(
                    route = "group_info/{groupId}",
                    arguments = listOf(navArgument("groupId") { type = NavType.IntType }),
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "https://taskosaurus.at/group/{groupId}"
                        }
                    )
                ) { backStackEntry ->
                    val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
                    GroupInfoScreen(
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
}