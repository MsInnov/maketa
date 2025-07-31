package com.mscode.presentation.home.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mscode.presentation.common.ErrorScreen
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiEvent.UpdateFavorite
import com.mscode.presentation.home.model.UiProducts
import com.mscode.presentation.home.model.UiState
import com.mscode.presentation.home.viewmodel.HomeViewModel
import com.mscode.presentation.login.component.LoginPanel
import com.mscode.presentation.login.model.UiState.Logged
import com.mscode.presentation.login.viewmodel.LoginViewModel
import com.mscode.presentation.menu.component.MenuAnimated
import com.mscode.presentation.register.component.RegisterPanel
import com.mscode.presentation.register.model.UiState.Registered
import com.mscode.presentation.register.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay

val lightBackground = Color(0xFFF5F5F5)

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val uiState = homeViewModel.uiState.collectAsState()
    when (val state = uiState.value) {
        is UiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        is UiState.Error -> ErrorScreen()
        is UiState.Products -> ProductsScreenWithSidePanel(
            homeViewModel = homeViewModel,
            products = state.products,
            goToHome = {
                homeViewModel.onEvent(UiEvent.LoadProduct)
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProductsScreenWithSidePanel(
    homeViewModel: HomeViewModel,
    products: List<UiProducts>,
    goToHome: () -> Unit
) {
    var panelOpen by remember { mutableStateOf(false) }
    var panelContentState by remember { mutableStateOf(PanelContentState.LOGIN) }
    val panelWidth = 320.dp
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(products.size) { index ->
                val product = products[index]
                ProductItem(
                    homeViewModel = homeViewModel,
                    product = product,
                    onFavoriteClick = { clickedFavoriteProduct ->
                        homeViewModel.onEvent(UpdateFavorite(clickedFavoriteProduct))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(40.dp)
                .height(120.dp)
                .background(
                    Color(0xFF6200EE),
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
                .clickable {
                    panelOpen = true
                    panelContentState = PanelContentState.LOGIN
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Menu",
                color = Color.White,
                modifier = Modifier.rotate(-90f),
                style = MaterialTheme.typography.button
            )
        }

        val offsetX by animateDpAsState(
            targetValue = if (panelOpen) 0.dp else panelWidth,
            animationSpec = tween(durationMillis = 300),
            label = "PanelSlide"
        )
        val registerViewModel: RegisterViewModel = hiltViewModel()
        val loginViewModel: LoginViewModel = hiltViewModel()

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(panelWidth)
                .align(Alignment.CenterEnd)
                .offset(x = offsetX)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
                .shadow(8.dp)
                .padding(16.dp)
        ) {
            AnimatedContent(
                targetState = panelContentState,
                transitionSpec = {
                    if (targetState == PanelContentState.REGISTER) {
                        slideInHorizontally { width -> width } with slideOutHorizontally { width -> -width }
                    } else {
                        slideInHorizontally { width -> -width } with slideOutHorizontally { width -> width }
                    }
                },
                label = "PanelSlideAnimation"
            ) { state ->
                when (state) {
                    PanelContentState.LOGIN -> LoginPanel(
                        viewModel = loginViewModel,
                        onClose = { panelOpen = false },
                        onRegisterClick = { panelContentState = PanelContentState.REGISTER },
                        onGoToMenu = { panelContentState = PanelContentState.MENU }
                    )

                    PanelContentState.REGISTER -> RegisterPanel(
                        viewModel = registerViewModel,
                        onClose = { panelOpen = false },
                        onBackToLogin = { panelContentState = PanelContentState.LOGIN }
                    )

                    PanelContentState.MENU -> {
                        MenuAnimated(
                            homeViewModel = homeViewModel,
                            loginViewModel = loginViewModel,
                            onClose = {
                                homeViewModel.onEvent(UiEvent.DisplayFavorites)
                                panelOpen = false
                            },
                            onGoLogin = {
                                homeViewModel.onEvent(UiEvent.DisplayFavorites)
                                goToHome()
                                panelContentState = PanelContentState.LOGIN
                            },
                            onGoFavorite = {
                                panelOpen = false
                                homeViewModel.onEvent(UiEvent.LoadProductsFavorites)
                            }
                        )
                    }
                }
            }
        }
        val context = LocalContext.current
        val uiStateRegister = registerViewModel.uiState.collectAsState()
        val uiStateLogin = loginViewModel.uiState.collectAsState()
        LaunchedEffect(uiStateRegister.value, uiStateLogin.value) {
            if (uiStateRegister.value == Registered) {
                repeat(2) { // 2 x 4s = 8s environ
                    Toast.makeText(
                        context,
                        "Vous êtes inscrits mais l'api ne permet d'avoir de nouveau utilisateurs",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    delay(4000) // attendre que le toast se termine
                }
            }
            if (uiStateLogin.value == Logged) {
                repeat(2) { // 2 x 4s = 8s environ
                    Toast.makeText(context, "Vous êtes loggé avec succès", Toast.LENGTH_LONG)
                        .show()
                    delay(4000) // attendre que le toast se termine
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    homeViewModel: HomeViewModel,
    product: UiProducts,
    onFavoriteClick: (UiProducts) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val isFavoriteIsVisible = homeViewModel.uiStateFavoriteDisplay.collectAsState().value

    val scaleFavorite by animateFloatAsState(
        targetValue = if (product.isFavorite) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "HeartScale"
    )

    val imageHeight by animateDpAsState(
        targetValue = if (expanded) 400.dp else 200.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ImageHeightAnimation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = 6.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (content, heartIcon) = createRefs()

            if (isFavoriteIsVisible) {
                IconButton(
                    onClick = { onFavoriteClick(product) },
                    modifier = Modifier
                        .constrainAs(heartIcon) {
                            top.linkTo(parent.top, margin = 8.dp)
                            end.linkTo(parent.end, margin = 8.dp)
                            bottom.linkTo(content.top)
                        }
                        .zIndex(1f)
                ) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Ajouter aux favoris",
                        tint = if (product.isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.scale(scaleFavorite)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .constrainAs(content) {
                        top.linkTo(heartIcon.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = product.category.uppercase(),
                    style = MaterialTheme.typography.overline,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.body2,
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = product.image,
                    contentDescription = null,
                    placeholder = painterResource(id = com.mscode.presentation.R.drawable.placeholder),
                    error = painterResource(id = com.mscode.presentation.R.drawable.placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clickable { expanded = !expanded }
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

enum class PanelContentState {
    LOGIN, MENU, REGISTER
}