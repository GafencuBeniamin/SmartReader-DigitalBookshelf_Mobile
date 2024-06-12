package com.example.smartreader.ui.mainActivity.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartreader.ui.loginActivity.screens.StartScreen
import com.example.smartreader.ui.mainActivity.screens.AccountScreen
import com.example.smartreader.ui.mainActivity.screens.BookDetailsScreen
import com.example.smartreader.ui.mainActivity.screens.CreateBookScreen
import com.example.smartreader.ui.mainActivity.screens.DashboardScreen
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel

@Composable
fun MainNavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            BackHandler(true) {
                // Do nothing when Back is clicked
            }
            DashboardScreen(navController,viewModel)
        }
        composable(
            "book_details/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            if (bookId != null) {
                BookDetailsScreen(bookId = bookId, viewModel = viewModel)
            }
        }
        composable("account") {
            BackHandler(true) {
                // Do nothing when Back is clicked
            }
            AccountScreen(viewModel)
        }
        composable("createBook"){
            CreateBookScreen(navController,viewModel)
        }
    }
}
