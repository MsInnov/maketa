package com.mscode.presentation.menu.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mscode.presentation.home.screen.lightBackground
import com.mscode.presentation.login.viewmodel.LoginViewModel
import com.mscode.presentation.menu.model.UiEvent
import com.mscode.presentation.menu.model.UiState
import com.mscode.presentation.menu.viewmodel.MenuViewModel
import com.mscode.presentation.theme.Primary

typealias LoginDisconnect = com.mscode.presentation.login.model.UiEvent.Disconnect

@Composable
fun MenuAnimated(
    loginViewModel: LoginViewModel,
    onClose: () -> Unit,
    onGoLogin: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    val menuViewModel: MenuViewModel = hiltViewModel()
    val uiState = menuViewModel.uiState.collectAsState()
    when (uiState.value) {
        is UiState.Disconnected -> {
            menuViewModel.onEvent(UiEvent.Idle)
            loginViewModel.onEvent(LoginDisconnect)
            onGoLogin()
            return
        }
        else -> Unit
    }
    // Lance l'animation une fois que le composable est affiché
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(animationSpec = tween(500)),
        exit = ExitTransition.None
    ) {
        MenuList(
            onClose = onClose,
            onItemClick = { selectedItem ->
                when (selectedItem) {
                    "Se déconnecter" -> menuViewModel.onEvent(UiEvent.Disconnect)
                }
            }
        )
    }
}


@Composable
fun MenuList(
    onClose: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val menuItems = listOf(
        "Se déconnecter" to Icons.Default.ExitToApp
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(lightBackground)
                .align(Alignment.Center)
        ) {
            menuItems.forEach { (title, icon) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable{ onItemClick(title) }
                        .alpha(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = Primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = title,
                            style = typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Fermer")
            }
        }
    }
}
