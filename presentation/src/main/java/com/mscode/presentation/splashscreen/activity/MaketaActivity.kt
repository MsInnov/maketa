package com.mscode.presentation.splashscreen.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mscode.presentation.navigation.AppNavGraph
import com.mscode.presentation.splashscreen.viewmodel.SplashScreenViewModel
import com.mscode.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MaketaActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        val splashViewModel: SplashScreenViewModel by viewModels()

        splashScreen.setKeepOnScreenCondition {
            !splashViewModel.isReady.value
        }

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppNavGraph()
            }
        }
    }
}