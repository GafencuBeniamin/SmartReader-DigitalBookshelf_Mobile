package com.example.smartreader.ui.mainActivity.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartreader.R
import com.example.smartreader.data.entities.Book
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(navController: NavController, viewModel: MainViewModel) {
    val booksResource by viewModel.userLibrary.observeAsState(initial = Resource.loading(null))
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Fetch data every time the screen is opened
        viewModel.getMyBooks()
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
                                        navController.navigate("book_details/$bookId")
                                    }
                                }
                            }
                        }
                        // Floating button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate("createBook")
                                },
                                modifier = Modifier
                                    .padding(30.dp)
                                    .size(56.dp)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Text("+", fontSize = 24.sp)
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

@Composable
fun BookItem(book: Book, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick(book.id.toString()) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = if (book.image == null) {
            painterResource(id = R.drawable.no_image)
        } else {
            // Assuming book.image is a painter resource ID
            rememberAsyncImagePainter(model = book.image)
        }
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 16.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = book.title.toString(), style = MaterialTheme.typography.h6)
            Text(text = book.author.toString(), style = MaterialTheme.typography.body2)
        }
    }
    Spacer(modifier = Modifier.height(200.dp))
}