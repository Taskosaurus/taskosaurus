package at.htlleonding.taskosaurus.view.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                        .padding(start = if (useNavRail && player != null) 120.dp else 0.dp)
                ) {
                    composable("auth") {
                        LoginRegisterView(viewModel, onSuccess = {
                            navController.navigate(Screen.Games.route) { popUpTo("auth") { inclusive = true } }
                        })
                    }
                    navigation(startDestination = "title", route = Screen.Games.route) {
                        composable("title") {
                            LaunchedEffect(Unit) { viewModel.startAutoRefresh() }
                            TitleScreen({ navController.navigate("game_list") }, { navController.navigate("group_info/$it") }, viewModel)
                        }
                        composable("game_list") { GameListScreen(viewModel) { navController.navigate("game/$it") } }
                        composable("game/{groupId}", arguments = listOf(navArgument("groupId") { type = NavType.IntType })) {
                            GameScreen(it.arguments?.getInt("groupId") ?: 0, viewModel) { id -> navController.navigate("group_info/$id") }
                        }
                        composable("group_info/{groupId}", arguments = listOf(navArgument("groupId") { type = NavType.IntType })) {
                            GroupInfoScreen(it.arguments?.getInt("groupId") ?: 0, viewModel)
                        }
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(viewModel, { viewModel.logout(); navController.navigate("auth") { popUpTo(0) { inclusive = true } } }, { navController.navigate("auth") })
                    }
                }
            }
        }

        // Die Rail liegt ÜBER dem Scaffold in der Root-Box.
        // Da sie links oben ausgerichtet ist, überlappt sie beim Ausklappen den Content.
        if (useNavRail && player != null && isReady) {
            MainNavigationRail(navController)
        }
    }
}