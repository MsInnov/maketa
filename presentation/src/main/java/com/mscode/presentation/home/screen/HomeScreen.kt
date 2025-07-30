package com.mscode.presentation.home.screen

import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mscode.presentation.common.ErrorScreen
import com.mscode.presentation.home.model.UiProducts
import com.mscode.presentation.home.model.UiState
import com.mscode.presentation.home.viewmodel.HomeViewModel
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
        is UiState.Products -> ProductsScreen(
            products = state.products
        )
    }
}

@Composable
fun ProductsScreen(
    products: List<UiProducts>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(products.size) { index ->
                val product = products[index]
                ProductItem(
                    product = product
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProductItem(
    product: UiProducts
) {
    var expanded by remember { mutableStateOf(false) }

    val scalePurchase by animateFloatAsState(
        targetValue = if (product.isCart) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "PurchaseScale"
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
            Column(
                modifier = Modifier
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