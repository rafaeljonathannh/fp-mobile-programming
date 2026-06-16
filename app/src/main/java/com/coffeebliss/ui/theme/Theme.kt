package com.coffeebliss.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Coffee color palette
val CoffeeBrown = Color(0xFF4A2C2A)
val CoffeeLight = Color(0xFF7B5147)
val CoffeeCream = Color(0xFFF5E6D3)
val CoffeeGold = Color(0xFFD4A017)
val CoffeeWhite = Color(0xFFFFFBF7)
val CoffeeDark = Color(0xFF2C1810)
val CoffeeAccent = Color(0xFFE8C49A)

private val CoffeeLightColorScheme = lightColorScheme(
    primary = CoffeeBrown,
    onPrimary = Color.White,
    primaryContainer = CoffeeCream,
    onPrimaryContainer = CoffeeDark,
    secondary = CoffeeLight,
    onSecondary = Color.White,
    secondaryContainer = CoffeeAccent,
    onSecondaryContainer = CoffeeDark,
    tertiary = CoffeeGold,
    background = CoffeeWhite,
    onBackground = CoffeeDark,
    surface = Color.White,
    onSurface = CoffeeDark,
    surfaceVariant = CoffeeCream,
    onSurfaceVariant = CoffeeLight
)

@Composable
fun CoffeeBlissTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoffeeLightColorScheme,
        typography = Typography(),
        content = content
    )
}
