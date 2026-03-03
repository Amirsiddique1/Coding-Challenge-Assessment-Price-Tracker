package com.apprays.real_timepricetrackerapp.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.apprays.real_timepricetrackerapp.data.model.PriceDirection
import com.apprays.real_timepricetrackerapp.data.model.priceDirection
import com.apprays.real_timepricetrackerapp.ui.feed.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    feedViewModel: FeedViewModel,
    onBack: () -> Unit
) {
    val detailsViewModel: DetailsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                DetailsViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    feedViewModel = feedViewModel
                )
            }
        }
    )

    val stock by detailsViewModel.stock.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stock?.symbol ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (stock == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val currentStock = stock!!
            val direction = currentStock.priceDirection()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Symbol
                Text(
                    text = currentStock.symbol,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                // Price + direction
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (direction) {
                            PriceDirection.UP -> "↑"
                            PriceDirection.DOWN -> "↓"
                            PriceDirection.SAME -> "→"
                        },
                        color = when (direction) {
                            PriceDirection.UP -> Color.Green
                            PriceDirection.DOWN -> Color.Red
                            PriceDirection.SAME -> Color.Gray
                        },
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "$${String.format("%.2f", currentStock.price)}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                HorizontalDivider()

                // Description
                Text(
                    text = "About",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentStock.description,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 24.sp
                )
            }
        }
    }
}
