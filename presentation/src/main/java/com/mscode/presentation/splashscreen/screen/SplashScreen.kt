package com.mscode.presentation.splashscreen.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mscode.presentation.R
import com.mscode.presentation.splashscreen.model.UiState
import com.mscode.presentation.splashscreen.viewmodel.SplashScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashScreenViewModel: SplashScreenViewModel,
    onTimeout: (error: Boolean) -> Unit
) {
    val uiState by splashScreenViewModel.uiState.collectAsState()
    var hasWaited by remember { mutableStateOf(false) }

    // Lance un délai de 3 secondes
    LaunchedEffect(Unit) {
        delay(3000)
        hasWaited = true
    }

    LaunchedEffect(uiState, hasWaited) {
        if (hasWaited) {
            when (uiState) {
                is UiState.Success -> onTimeout(false)
                is UiState.Error -> onTimeout(true)
                else -> Unit
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.maketa),
                contentDescription = "Maketa Logo",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alphaAnim)
            )
        }
    }
}
