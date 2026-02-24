package at.htlleonding.taskosaurus.view.screens

import android.app.Activity
import android.view.Surface
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.*
import at.htlleonding.taskosaurus.data.model.Screen
import at.htlleonding.taskosaurus.view.components.MainNavigation
import at.htlleonding.taskosaurus.view.components.MainNavigationRail
import at.htlleonding.taskosaurus.view.screens.auth.LoginRegisterView
import at.htlleonding.taskosaurus.view.screens.general.SettingsScreen
import at.htlleonding.taskosaurus.view.screens.whoWouldRather.*
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(activity: Activity) {
    val navController = rememberNavController()
    val viewModel: ViewModel = viewModel()
    val player by viewModel.player.collectAsState()
    val isReady by viewModel.isReady.collectAsState()

    val windowSizeClass = calculateWindowSizeClass(activity)
    val useNavRail = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    // Rotation und System-UI Logik
    val context = LocalContext.current
    val rotation = context.display?.rotation ?: Surface.ROTATION_0

    // Fall 1: Handy nach links gekippt -> Nav-Leiste ist LINKS (Rotation 270)
    val isNavBarLeft = rotation == Surface.ROTATION_270
    // Fall 2: Handy nach rechts gekippt -> Nav-Leiste ist RECHTS (Rotation 90)
    val isNavBarRight = rotation == Surface.ROTATION_90

    // Die Breite der Rail muss exakt der in MainNavigationRail entsprechen
    val baseRailWidth = 80.dp
    val railWidthWithSystem = if (isNavBarLeft) baseRailWidth + 48.dp else baseRailWidth

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            bottomBar = {
                // BottomBar nur im Portrait-Modus (Compact)
                if (!useNavRail && player != null && isReady) {
                    MainNavigation(navController)
                }
            }
        ) { paddingValues ->
            if (isReady) {
                val startDestination = if (player != null) Screen.Games.route else "auth"

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                        .padding(
                            // Dynamisches Padding für die Rail
                            start = if (useNavRail && player != null) railWidthWithSystem else 0.dp,
                            // Dynamisches Padding für die System-Leiste, wenn sie RECHTS liegt
                            end = if (isNavBarRight) 48.dp else 0.dp
                        )
                ) {
                    composable("auth") {
                        LoginRegisterView(viewModel, onSuccess = {
                            navController.navigate(Screen.Games.route) { popUpTo("auth") { inclusive = true } }
                        })
                    }

                    navigation(startDestination = "title", route = Screen.Games.route) {
                        composable("title") {
                            LaunchedEffect(Unit) { viewModel.startAutoRefresh() }
                            TitleScreen(
                                onOpenGameList = { navController.navigate("game_list") },
                                onGameCreated = { navController.navigate("group_info/$it") },
                                viewModel = viewModel
                            )
                        }
                        composable("game_list") {
                            GameListScreen(viewModel) { navController.navigate("game/$it") }
                        }
                        composable(
                            route = "game/{groupId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                        ) {
                            GameScreen(
                                groupId = it.arguments?.getInt("groupId") ?: 0,
                                viewModel = viewModel,
                                onNavigateToGroupInfo = { id -> navController.navigate("group_info/$id") }
                            )
                        }
                        composable(
                            route = "group_info/{groupId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                        ) {
                            GroupInfoScreen(
                                groupId = it.arguments?.getInt("groupId") ?: 0,
                                viewModel = viewModel
                            )
                        }
                    }

                    composable(Screen.Settings.route) {
                        SettingsScreen(
                            viewModel = viewModel,
                            onLogout = {
                                viewModel.logout()
                                navController.navigate("auth") { popUpTo(0) { inclusive = true } }
                            },
                            onNavigateToLogin = { navController.navigate("auth") }
                        )
                    }
                }
            }
        }

        // Die Rail liegt als Box-Overlay über dem Scaffold
        if (useNavRail && player != null && isReady) {
            MainNavigationRail(navController)
        }
    }
}