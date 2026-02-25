package at.htlleonding.taskosaurus.view.screens

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.Surface
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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

    // Device & Orientation Check
    val windowSizeClass = calculateWindowSizeClass(activity)
    val configuration = LocalConfiguration.current
    val useNavRail = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    // Bedingung für Master-Detail: Tablet-Breite UND Querformat
    val isTabletLandscape = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
            && configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val context = LocalContext.current
    val rotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        context.display?.rotation ?: Surface.ROTATION_0
    } else {
        @Suppress("DEPRECATION")
        (context.getSystemService(android.content.Context.WINDOW_SERVICE) as android.view.WindowManager).defaultDisplay.rotation
    }

    val isNavBarLeft = rotation == Surface.ROTATION_270
    val isNavBarRight = rotation == Surface.ROTATION_90

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isTablet = configuration.screenWidthDp >= 600

    // Tablet bekommt breitere Rail — muss mit MainNavigationRail übereinstimmen
    val baseRailWidth = if (isTablet) 96.dp else 80.dp
    val railWidthWithSystem = if (isNavBarLeft) baseRailWidth + 48.dp else baseRailWidth

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surface,
            bottomBar = {
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
                            top = if (currentRoute == Screen.Settings.route || currentRoute == "auth") 0.dp
                            else paddingValues.calculateTopPadding(),
                            bottom = if (currentRoute == Screen.Settings.route || currentRoute == "auth") 0.dp
                            else paddingValues.calculateBottomPadding()
                        )
                        .padding(
                            start = if (useNavRail && player != null) railWidthWithSystem else 0.dp,
                            end = if (isNavBarRight) 48.dp else 0.dp
                        )
                        .imePadding()
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
                            // Hier wird entschieden: Master-Detail (Tablet) oder Liste (Handy)
                            AdaptiveGameLayout(
                                viewModel = viewModel,
                                isTabletLandscape = isTabletLandscape,
                                onNavigateToGame = { groupId ->
                                    // Navigation nur für Handy-Modus
                                    navController.navigate("game/$groupId")
                                }
                            )
                        }

                        // Diese Routen werden nur im Handy-Modus oder bei explizitem Aufruf (Vollbild) genutzt
                        composable(
                            route = "game/{groupId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
                            GameScreen(
                                groupId = groupId,
                                viewModel = viewModel,
                                onNavigateToGroupInfo = { id -> navController.navigate("group_info/$id") },
                                isTabletMode = false // Vollbildmodus
                            )
                        }

                        composable(
                            route = "group_info/{groupId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
                            GroupInfoScreen(
                                groupId = groupId,
                                viewModel = viewModel,
                                isTabletMode = false // Vollbildmodus
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

        if (useNavRail && player != null && isReady) {
            MainNavigationRail(navController)
        }
    }
}
