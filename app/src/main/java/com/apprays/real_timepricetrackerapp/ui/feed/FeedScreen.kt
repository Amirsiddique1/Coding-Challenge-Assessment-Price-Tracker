package com.apprays.real_timepricetrackerapp.ui.feed

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apprays.real_timepricetrackerapp.data.model.PriceDirection
import com.apprays.real_timepricetrackerapp.data.model.StockItem
import com.apprays.real_timepricetrackerapp.data.model.priceDirection
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    onSymbolClick: (String) -> Unit
) {
    val stocks by viewModel.stocks.collectAsStateWithLifecycle()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val isRunning by viewModel.isRunning.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Price Tracker") },
                navigationIcon = {
                    // Connection status indicator
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(if (isConnected) Color.Green else Color.Red)
                    )
                },
                actions = {
                    Button(
                        onClick = {
                            if (isRunning) viewModel.stopFeed()
                            else viewModel.startFeed()
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(if (isRunning) "Stop" else "Start")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(stocks, key = { it.symbol }) { stock ->
                StockRow(
                    stock = stock,
                    onClick = { onSymbolClick(stock.symbol) }
                )
            }
        }
    }
}

@Composable
fun StockRow(
    stock: StockItem,
    onClick: () -> Unit
) {
    val direction = stock.priceDirection()

    // Flash color state
    var flashColor by remember { mutableStateOf(Color.Transparent) }

    // Trigger flash when price changes
    LaunchedEffect(stock.price) {
        flashColor = when (direction) {
            PriceDirection.UP -> Color(0xFF1B5E20) // dark green
            PriceDirection.DOWN -> Color(0xFFB71C1C) // dark red
            PriceDirection.SAME -> Color.Transparent
        }
        delay(1000)
        flashColor = Color.Transparent
    }

    val animatedColor by animateColorAsState(
        targetValue = flashColor,
        animationSpec = tween(durationMillis = 500),
        label = "flashAnim"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(animatedColor)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Symbol name
            Text(
                text = stock.symbol,
                fontSize = 18.sp,
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
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = "$${String.format("%.2f", stock.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
