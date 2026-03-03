package com.apprays.real_timepricetrackerapp.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apprays.real_timepricetrackerapp.data.model.StockItem
import com.apprays.real_timepricetrackerapp.data.websocket.WebSocketManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {

    val webSocketManager = WebSocketManager()

    // All 25 stock symbols with descriptions
    private val initialStocks = listOf(
        StockItem("AAPL", 150.0, 150.0, "Apple Inc. - Consumer electronics and software company."),
        StockItem("GOOG", 2800.0, 2800.0, "Alphabet Inc. - Parent company of Google."),
        StockItem("TSLA", 700.0, 700.0, "Tesla Inc. - Electric vehicle and clean energy company."),
        StockItem("AMZN", 3400.0, 3400.0, "Amazon.com Inc. - E-commerce and cloud computing giant."),
        StockItem("MSFT", 290.0, 290.0, "Microsoft Corp. - Software and cloud services company."),
        StockItem("NVDA", 400.0, 400.0, "NVIDIA Corp. - Graphics and AI chip manufacturer."),
        StockItem("META", 320.0, 320.0, "Meta Platforms - Social media and VR company."),
        StockItem("NFLX", 500.0, 500.0, "Netflix Inc. - Streaming entertainment service."),
        StockItem("BABA", 90.0, 90.0, "Alibaba Group - Chinese e-commerce and cloud company."),
        StockItem("AMD", 110.0, 110.0, "Advanced Micro Devices - Semiconductor company."),
        StockItem("INTC", 35.0, 35.0, "Intel Corp. - Semiconductor chip manufacturer."),
        StockItem("PYPL", 65.0, 65.0, "PayPal Holdings - Digital payments platform."),
        StockItem("UBER", 40.0, 40.0, "Uber Technologies - Ride-hailing and delivery platform."),
        StockItem("LYFT", 12.0, 12.0, "Lyft Inc. - Ride-sharing company."),
        StockItem("SNAP", 10.0, 10.0, "Snap Inc. - Social media and camera company."),
        StockItem("TWTR", 54.0, 54.0, "Twitter Inc. - Social media platform."),
        StockItem("SPOT", 130.0, 130.0, "Spotify Technology - Music streaming service."),
        StockItem("SQ", 65.0, 65.0, "Block Inc. - Financial technology company."),
        StockItem("SHOP", 60.0, 60.0, "Shopify Inc. - E-commerce platform provider."),
        StockItem("ZOOM", 70.0, 70.0, "Zoom Video Communications - Video conferencing platform."),
        StockItem("CRM", 210.0, 210.0, "Salesforce Inc. - Cloud-based CRM software company."),
        StockItem("ORCL", 115.0, 115.0, "Oracle Corp. - Database and cloud software company."),
        StockItem("IBM", 145.0, 145.0, "IBM Corp. - IT and consulting services company."),
        StockItem("CSCO", 48.0, 48.0, "Cisco Systems - Networking hardware and software company."),
        StockItem("QCOM", 130.0, 130.0, "Qualcomm Inc. - Wireless technology and semiconductor company.")
    )

    // UI State
    private val _stocks = MutableStateFlow(initialStocks)
    val stocks: StateFlow<List<StockItem>> = _stocks

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    init {
        // Listen to connection status
        viewModelScope.launch {
            webSocketManager.isConnected.collect { connected ->
                _isConnected.value = connected
            }
        }

        // Listen to incoming WebSocket messages and update stock prices
        viewModelScope.launch {
            webSocketManager.messages.collect { message ->
                handleMessage(message)
            }
        }
    }

    private fun handleMessage(message: String) {
        // Message format: "AAPL:175.43"
        val parts = message.split(":")
        if (parts.size == 2) {
            val symbol = parts[0]
            val newPrice = parts[1].toDoubleOrNull() ?: return

            _stocks.update { currentList ->
                currentList.map { stock ->
                    if (stock.symbol == symbol) {
                        stock.copy(
                            previousPrice = stock.price,
                            price = newPrice
                        )
                    } else stock
                }.sortedByDescending { it.price } // highest price at top
            }
        }
    }

    fun startFeed() {
        if (_isRunning.value) return
        _isRunning.value = true
        webSocketManager.connect()

        viewModelScope.launch {
            while (_isRunning.value) {
                val currentStocks = _stocks.value.toList() // immutable snapshot
                currentStocks.forEach { stock ->
                    val randomPrice = stock.price * (0.98 + Math.random() * 0.04)
                    val formattedPrice = String.format("%.2f", randomPrice)
                    webSocketManager.sendMessage("${stock.symbol}:$formattedPrice")
                    delay(50)
                }
                delay(2000)
            }
        }
    }

    fun stopFeed() {
        _isRunning.value = false
        webSocketManager.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}
