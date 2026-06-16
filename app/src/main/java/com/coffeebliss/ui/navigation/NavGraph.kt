package com.coffeebliss.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coffeebliss.ui.screens.*
import com.coffeebliss.viewmodel.CoffeeBlissViewModel

@Composable
fun CoffeeBlissNavGraph(
    navController: NavHostController,
    viewModel: CoffeeBlissViewModel
) {
    val memberId by viewModel.currentMemberId.collectAsState()
    val startDestination = if (memberId != -1) Screen.Home.route else Screen.Welcome.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onMemberCardClick = { navController.navigate(Screen.MemberCard.route) },
                onTransactionClick = { navController.navigate(Screen.Transaction.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onRedeemClick = { navController.navigate(Screen.Redeem.route) },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MemberCard.route) {
            MemberCardScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Transaction.route) {
            TransactionScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Redeem.route) {
            RedeemScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onRedeemHistoryClick = { navController.navigate(Screen.RedeemHistory.route) }
            )
        }

        composable(Screen.RedeemHistory.route) {
            RedeemHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
