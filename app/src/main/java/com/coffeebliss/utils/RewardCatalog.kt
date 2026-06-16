package com.coffeebliss.utils

data class Reward(
    val name: String,
    val pointsRequired: Int,
    val description: String,
    val icon: String
)

object RewardCatalog {
    val rewards = listOf(
        Reward("Espresso", 50, "Secangkir espresso hangat pilihan barista", "☕"),
        Reward("Cappuccino", 100, "Cappuccino creamy dengan foam sempurna", "🍵"),
        Reward("Latte Gratis", 150, "Latte premium susu segar pilihan", "🥛")
    )
}
