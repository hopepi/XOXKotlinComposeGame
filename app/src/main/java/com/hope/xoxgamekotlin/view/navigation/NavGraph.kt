package com.hope.xoxgamekotlin.view.navigation

import androidx.compose.runtime.Composable
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
    startDestination : String = Routes.Menu
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
        ){
        composable (Routes.Menu) {
            MainMenuScreen(
                onStartGame = { navController.navigate(Routes.Game) }
            )
        }
        composable (Routes.Game) {
            val viewModel : GameViewModel = hiltViewModel()
            GameScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}