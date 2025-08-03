package com.mscode.presentation.filter.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mscode.presentation.filter.model.UiEvent
import com.mscode.presentation.filter.model.UiState
import com.mscode.presentation.filter.viewmodel.FilterViewModel
import com.mscode.presentation.home.model.UiProduct
import com.mscode.presentation.home.screen.lightBackground

enum class SortOption {
    PRICE_ASC, PRICE_DESC
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPanel(
    products: List<UiProduct>,
    onClose: () -> Unit,
    viewModel: FilterViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("NONE") }
    var selectedSortOption by remember { mutableStateOf<SortOption?>(null) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(UiEvent.GetCategory)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Filtres",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Choix de la catégorie

                when (val state = uiState) {
                    is UiState.CategoryProducts -> {
                        val categories = state.list

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedCategory.ifEmpty { "Choisir une catégorie" },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Catégorie") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category) },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    else -> CircularProgressIndicator()
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tri par prix
                Text("Trier par", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedSortOption == SortOption.PRICE_DESC,
                            onClick = {
                                selectedSortOption = SortOption.PRICE_DESC
                                viewModel.onEvent(UiEvent.SortByPriceDescending)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Prix décroissant")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedSortOption == SortOption.PRICE_ASC,
                            onClick = {
                                selectedSortOption = SortOption.PRICE_ASC
                                viewModel.onEvent(UiEvent.SortByPriceAscending)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Prix croissant")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Boutons (nouvelle disposition verticale)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onClose,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Annuler")
                    }

                    ElevatedButton(
                        onClick = {
                            viewModel.onEvent(
                                UiEvent.FilterByCategory(
                                    selectedCategory,
                                    products
                                )
                            )
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Appliquer")
                    }
                }
            }
        }
    }
}
