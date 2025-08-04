package com.mscode.presentation.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import com.mscode.presentation.cart.model.UiEvent
import com.mscode.presentation.cart.model.UiState
import com.mscode.presentation.cart.viewmodel.CartViewModel
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.res.stringResource
import com.mscode.presentation.R
import com.mscode.presentation.home.model.UiProduct

@Composable
fun CartPanel(
    viewModel: CartViewModel,
    onCloseRequest: () -> Unit,
    toBank: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val maxListHeight = 500.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (val state = uiState) {
            is UiState.DisplayCart -> {
                val items = state.uiCart

                LaunchedEffect(items) {
                    if (items.isEmpty()) {
                        onCloseRequest()
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = maxListHeight),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = items,
                        key = { it.id to it.hashCode() }
                    ) { purchase ->
                        SwipeToDeleteItem(
                            item = purchase,
                            onDelete = {
                                viewModel.onEvent(UiEvent.DeleteItem(purchase))
                            }
                        )
                    }
                }
            }

            else -> Unit
        }

        Button(
            onClick = { toBank() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.cart_buy))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDeleteItem(
    item: UiProduct.Cart,
    onDelete: () -> Unit
) {
    val dismissKey = remember(item.id, item.hashCode()) { mutableStateOf(0) }

    key(dismissKey.value) {
        val dismissState = rememberDismissState(
            confirmStateChange = { value ->
                if (value == DismissValue.DismissedToStart || value == DismissValue.DismissedToEnd) {
                    onDelete()
                    true
                } else false
            }
        )

        SwipeToDismiss(
            state = dismissState,
            background = {
                val color = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd,
                    DismissDirection.EndToStart -> MaterialTheme.colorScheme.errorContainer
                    null -> MaterialTheme.colorScheme.surface
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.cart_delete),
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            },
            directions = setOf(DismissDirection.EndToStart)
        ) {
            Item(item)
        }
    }
}

@Composable
fun Item(cart: UiProduct.Cart) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cart.image,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cart.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = cart.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${cart.price} €",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}