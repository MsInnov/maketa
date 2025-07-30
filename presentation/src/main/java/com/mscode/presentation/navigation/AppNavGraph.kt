package com.mscode.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mscode.presentation.common.ErrorScreen
import com.mscode.presentation.home.screen.HomeScreen
import com.mscode.presentation.home.viewmodel.HomeViewModel
import com.mscode.presentation.splashscreen.model.UiState
import com.mscode.presentation.splashscreen.screen.SplashScreen
import com.mscode.presentation.splashscreen.viewmodel.SplashScreenViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val splashViewModel: SplashScreenViewModel = hiltViewModel()
    val uiState by splashViewModel.uiState.collectAsState()
    val isSplashDone by splashViewModel.isReady.collectAsState()

    val startDestination = if (isSplashDone) {
        when (uiState) {
            is UiState.Error -> "error"
            is UiState.Success -> "home"
            else -> "splash"
        }
    } else {
        "splash"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("splash") {
            SplashScreen(
                splashScreenViewModel = splashViewModel,
                onTimeout = { /* Ne rien faire ici */ }
            )
        }

        composable("home") {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(homeViewModel)
        }

        composable("error") {
            ErrorScreen()
        }
    }
}

