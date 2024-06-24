package com.example.smartreader.ui.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartreader.ui.mainActivity.bottomBar.BottomNavigationBar
import com.example.smartreader.ui.mainActivity.navigation.MainNavGraph
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.ui.theme.SmartReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartReaderTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(viewModel)
            }
        }
    }
}


@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val screenTitles = mapOf(
        "dashboard" to "Dashboard",
        "account" to "Account",
        "search" to "Search",
        "searchedBook/{bookId}" to "Book Details",
        "changeAccountDetails" to "Change Account Details",
        "bookDetails/{bookId}" to "Book Details",
        "createBook" to "Create Book",
        "editBook/{bookId}" to "Edit Book",
        "changeBookState/{bookId}" to "Edit Book State",
        "noteDetails/{noteId}" to "Note Details",
        "createNote/{bookId}" to "Create Note",
        "editNote/{noteId}" to "Edit Note",
        "pending" to "Pending Books",
        "manageBook/{bookId}" to "Manage Book",
        "moderatorSearch" to "Search",
        "moderatorBookDetails/{bookId}" to "Moderator Book Details",
        "changeUserRole" to "Change User Role"
    )
    Scaffold(
        topBar = {
            val title = currentRoute?.let { route ->
                screenTitles[route]
            }
            title?.let {
                TopAppBar(
                    title = { Text(text = it, fontSize = 20.sp) },
                    // Add more customization if needed
                )
            }
        },
        bottomBar = {
            if (currentRoute == "dashboard" || currentRoute=="account" || currentRoute=="search") {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MainNavGraph(navController = navController, viewModel)
        }
    }
}
