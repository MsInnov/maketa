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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mscode.presentation.R
import com.mscode.presentation.filter.viewmodel.FilterViewModel
import com.mscode.presentation.home.screen.lightBackground
import com.mscode.presentation.home.viewmodel.HomeViewModel
import com.mscode.presentation.login.viewmodel.LoginViewModel
import com.mscode.presentation.menu.model.UiEvent
import com.mscode.presentation.menu.model.UiState
import com.mscode.presentation.menu.viewmodel.MenuViewModel
import com.mscode.presentation.theme.Primary

typealias LoginDisconnect = com.mscode.presentation.login.model.UiEvent.Disconnect

@Composable
fun MenuAnimated(
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    filterViewModel: FilterViewModel,
    onClose: () -> Unit,
    onGoLogin: () -> Unit,
    onGoFavorite: () -> Unit,
    onGoSelling: () -> Unit,
    onGoFilter: () -> Unit,
    onGoCart: () -> Unit,
    onGoAccount: () -> Unit
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
        is UiState.Filter -> {
            menuViewModel.onEvent(UiEvent.Idle)
            onGoFilter()
            return
        }
        is UiState.Favorite -> {
            menuViewModel.onEvent(UiEvent.Idle)
            onGoFavorite()
            return
        }
        is UiState.Selling -> {
            menuViewModel.onEvent(UiEvent.Idle)
            onGoSelling()
            return
        }
        is UiState.Cart -> {
            menuViewModel.onEvent(UiEvent.Idle)
            onGoCart()
            return
        }
        is UiState.Account -> {
            menuViewModel.onEvent(UiEvent.Idle)
            onGoAccount()
            return
        }
        else -> Unit
    }

    LaunchedEffect(Unit) {
        visible = true
    }
    val account = stringResource(R.string.menu_account)
    val filter = stringResource(R.string.menu_filter)
    val cart = stringResource(R.string.menu_cart)
    val sell = stringResource(R.string.menu_sell)
    val favorite = stringResource(R.string.menu_favorite)
    val disconnect = stringResource(R.string.menu_disconnect)
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(animationSpec = tween(500)),
        exit = ExitTransition.None
    ) {
        MenuList(
            homeViewModel = homeViewModel,
            filterViewModel = filterViewModel,
            onClose = onClose,
            onItemClick = { selectedItem ->
                when (selectedItem) {
                    account -> menuViewModel.onEvent(UiEvent.Account)
                    filter -> menuViewModel.onEvent(UiEvent.Filter)
                    cart -> menuViewModel.onEvent(UiEvent.Cart)
                    sell -> menuViewModel.onEvent(UiEvent.Selling)
                    favorite -> menuViewModel.onEvent(UiEvent.Favorite)
                    disconnect -> menuViewModel.onEvent(UiEvent.Disconnect)
                }
            }
        )
    }
}


@Composable
fun MenuList(
    homeViewModel: HomeViewModel,
    filterViewModel: FilterViewModel,
    onClose: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val menuItems = listOf(
        stringResource(R.string.menu_account) to Icons.Default.Person,
        stringResource(R.string.menu_filter) to Icons.Default.FilterList,
        stringResource(R.string.menu_sell) to Icons.Default.Sell,
        stringResource(R.string.menu_cart) to Icons.Default.ShoppingCart,
        stringResource(R.string.menu_favorite) to Icons.Default.Favorite,
        stringResource(R.string.menu_disconnect) to Icons.Default.ExitToApp
    )
    val isFavoriteDisplayed = homeViewModel.uiStateFavorite.collectAsState()
    val isFilterDisplayed = filterViewModel.uiStateIsDisplayed.collectAsState()
    val isFavoriteHomeEnabledState = homeViewModel.uiStateFavoriteEnabled.collectAsState()
    val isCartHomeEnabledState = homeViewModel.uiStateCartEnabled.collectAsState()
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
                val isEnabled = when (title) {
                    stringResource(R.string.menu_favorite) -> isFavoriteHomeEnabledState.value
                    stringResource(R.string.menu_cart) -> isCartHomeEnabledState.value
                    else -> true
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = isEnabled) { onItemClick(title) }
                        .alpha(if (isEnabled) 1f else 0.4f),
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
                            tint = if (title == stringResource(R.string.menu_favorite)) {
                                if (isFavoriteDisplayed.value) Color.Red else Primary
                            } else {
                                if(title == stringResource(R.string.menu_filter)) {
                                    if (isFilterDisplayed.value) Color.Red else Primary
                                } else Primary
                            }
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
                Text(stringResource(R.string.menu_closed))
            }
        }
    }
}
