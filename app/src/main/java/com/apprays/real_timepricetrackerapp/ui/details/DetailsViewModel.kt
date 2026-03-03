package com.apprays.real_timepricetrackerapp.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apprays.real_timepricetrackerapp.data.model.StockItem
import com.apprays.real_timepricetrackerapp.ui.feed.FeedViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    feedViewModel: FeedViewModel
) : ViewModel() {

    // Get the symbol passed via navigation (e.g. "AAPL")
    private val symbol: String = checkNotNull(savedStateHandle["symbol"])

    // Observe the same stock list from FeedViewModel and filter for this symbol
    val stock: StateFlow<StockItem?> = feedViewModel.stocks
        .map { list -> list.find { it.symbol == symbol } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
