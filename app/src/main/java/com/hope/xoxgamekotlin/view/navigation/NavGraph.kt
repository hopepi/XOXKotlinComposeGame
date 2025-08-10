package com.hope.xoxgamekotlin.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hope.xoxgamekotlin.view.gamescreen.GameScreen
import com.hope.xoxgamekotlin.view.mainmenuscreen.MainMenuScreen
import com.hope.xoxgamekotlin.viewmodel.GameViewModel
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.Menu
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = "root"
    ) {
        composable(Routes.Menu) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("root")
            }
            val viewModel: GameViewModel = hiltViewModel(parentEntry)

            MainMenuScreen(
                onStartGame = { navController.navigate(Routes.Game) },
                onDifficultyChange = { diff ->
                    viewModel.setDifficulty(diff)
                }
            )
        }

        composable(Routes.Game) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("root")
            }
            val viewModel: GameViewModel = hiltViewModel(parentEntry)

            GameScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}
