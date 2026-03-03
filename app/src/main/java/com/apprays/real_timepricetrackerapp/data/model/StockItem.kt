package com.apprays.real_timepricetrackerapp.data.model

data class StockItem(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
    val description: String
)

enum class PriceDirection { UP, DOWN, SAME }

fun StockItem.priceDirection(): PriceDirection {
    return when {
        price > previousPrice -> PriceDirection.UP
        price < previousPrice -> PriceDirection.DOWN
        else -> PriceDirection.SAME
    }
}
