package com.mscode.presentation.sell.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mscode.presentation.sell.model.SellProductUi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.mscode.presentation.sell.model.UiEvent
import com.mscode.presentation.sell.viewmodel.SellViewModel
import com.mscode.presentation.R

@Composable
fun SellPanel(
    onClose: () -> Unit,
    onSubmit: (SellProductUi) -> Unit,
    sellViewModel: SellViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    val isPriceValid = price.toDoubleOrNull()?.let { it >= 0.0 } == true
    val isFormValid = title.isNotBlank() &&
            description.isNotBlank() &&
            price.isNotBlank() &&
            category.isNotBlank() &&
            imageUrl.isNotBlank() &&
            isPriceValid

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(min = 300.dp, max = 600.dp), // Limite haute
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text("Créer un produit", style = MaterialTheme.typography.headlineSmall)
                }

                item {
                    TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                }
                item {
                    TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                }
                item {
                    TextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
                    if (!isPriceValid && price.isNotBlank()) {
                        Text(
                            text = "Le prix doit être un nombre valide",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                item {
                    TextField(value = category, onValueChange = { category = it }, label = { Text("Category") })
                }
                item {
                    TextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Image URL") })
                }
                if (imageUrl.isNotBlank()) {
                    item {
                        ProductImageView(imageUrl = imageUrl)
                    }
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onClose,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Annuler")
                        }

                        Button(
                            onClick = {
                                val productData = SellProductUi(
                                    title = title,
                                    description = description,
                                    price = price.toDouble(),
                                    category = category,
                                    image = imageUrl
                                )
                                sellViewModel.onEvent(UiEvent.SellingProduct(productData))
                                onSubmit(productData)
                            },
                            enabled = isFormValid,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ProductImageView(imageUrl: String) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Product Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        placeholder = painterResource(id = R.drawable.placeholder),
        error = painterResource(id = R.drawable.placeholder)
    )
}