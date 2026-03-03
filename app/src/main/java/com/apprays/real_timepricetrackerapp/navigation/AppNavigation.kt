package com.apprays.real_timepricetrackerapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.apprays.real_timepricetrackerapp.ui.details.DetailsScreen
import com.apprays.real_timepricetrackerapp.ui.feed.FeedScreen
import com.apprays.real_timepricetrackerapp.ui.feed.FeedViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // FeedViewModel is created once and shared with both screens
    val feedViewModel: FeedViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "feed"
    ) {

        // Feed Screen
        composable(route = "feed") {
            FeedScreen(
                viewModel = feedViewModel,
                onSymbolClick = { symbol ->
                    navController.navigate("details/$symbol")
                }
            )
        }

        // Details Screen
        composable(
            route = "details/{symbol}",
            arguments = listOf(
                navArgument("symbol") { type = NavType.StringType }
            ),
        ) {
            DetailsScreen(
                feedViewModel = feedViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
