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
import com.example.smartreader.ui.mainActivity.screens.CreateNoteScreen
import com.example.smartreader.ui.mainActivity.screens.DashboardScreen
import com.example.smartreader.ui.mainActivity.screens.EditBookScreen
import com.example.smartreader.ui.mainActivity.screens.EditNoteScreen
import com.example.smartreader.ui.mainActivity.screens.NoteDetailsScreen
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel

@Composable
fun MainNavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = "dashboard") {
        //MAIN
        composable("dashboard") {
            BackHandler(true) {
                // Do nothing when Back is clicked
            }
            DashboardScreen(navController,viewModel)
        }
        composable("account") {
            BackHandler(true) {
                // Do nothing when Back is clicked
            }
            AccountScreen(viewModel)
        }
        //BOOKS
        composable(
            "book_details/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            if (bookId != null) {
                BookDetailsScreen(bookId = bookId, viewModel = viewModel, navController = navController)
            }
        }
        composable("createBook"){
            CreateBookScreen(navController,viewModel)
        }
        composable(
            "editBook/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            if (bookId != null) {
                EditBookScreen(bookId = bookId, navController = navController, viewModel = viewModel)
            }
        }
        //NOTES
        composable(
            "noteDetails/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            if (noteId != null) {
                NoteDetailsScreen(noteId = noteId, viewModel = viewModel, navController = navController)
            }
        }
        composable(
            "createNote/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ){ backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            if (bookId != null) {
                CreateNoteScreen(bookId= bookId, viewModel = viewModel, navController = navController)
            }
        }
        composable(
            "editNote/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            if (noteId != null) {
                EditNoteScreen(noteId = noteId, navController = navController, viewModel = viewModel)
            }
        }
    }
}
