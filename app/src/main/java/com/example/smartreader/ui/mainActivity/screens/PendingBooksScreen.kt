package com.example.smartreader.ui.mainActivity.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource
import kotlinx.coroutines.delay

@Composable
fun PendingBooksScreen(navController: NavController, viewModel: MainViewModel){
    val booksResource by viewModel.pendingBooks.observeAsState(initial = Resource.loading(null))
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Fetch data every time the screen is opened
        viewModel.getPendingBooks()
        // Add a delay to let data fetch
        delay(200)
        isLoading = false
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        when(isLoading) {
            true -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
            false -> {
                when (booksResource.status) {
                    Resource.Status.LOADING -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                    Resource.Status.SUCCESS -> {
                        booksResource.data?.let { books ->
                            LazyColumn {
                                items(books) { book ->
                                    BookItem(book = book) { bookId ->
                                        navController.navigate("manageBook/$bookId")
                                    }
                                }
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        Text("Error: " + booksResource.message)
                    }
                }

            }
        }

    }
}
