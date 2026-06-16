package com.coffeebliss.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object MemberCard : Screen("member_card")
    object Transaction : Screen("transaction")
    object History : Screen("history")
    object Redeem : Screen("redeem")
    object RedeemHistory : Screen("redeem_history")
}
