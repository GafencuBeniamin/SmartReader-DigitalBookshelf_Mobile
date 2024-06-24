package com.example.smartreader.ui.mainActivity.screens

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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

    LaunchedEffect(Unit) {
        viewModel.getMyBooks()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
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
                    val groupedBooks = books.groupBy { it.bookStates?.values?.firstOrNull() ?: "UNKNOWN" }
                    val customOrder = listOf("READING", "TO_BE_READ", "DROPPED", "FINISHED", "UNKNOWN")
                    // Define a comparator that sorts states according to the 'customOrder' list
                    val stateComparator = Comparator<String> { state1, state2 ->
                        val index1 = customOrder.indexOf(state1)
                        val index2 = customOrder.indexOf(state2)
                        when {
                            index1 == -1 && index2 == -1 -> 0 // both are UNKNOWN, treat as equal
                            index1 == -1 -> 1 // state1 is UNKNOWN, push it to the end
                            index2 == -1 -> -1 // state2 is UNKNOWN, push it to the end
                            else -> index1.compareTo(index2)
                        }
                    }
                    val sortedGroupedBooks = groupedBooks.mapKeys { it.key.toString() }.toSortedMap(stateComparator)
                    LazyColumn {
                        sortedGroupedBooks.forEach { (state, books) ->
                            item {
                                Text(
                                    text = state.toString(),
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(books.sortedBy { it.title }) { book ->
                                BookItem(book = book) { bookId ->
                                    navController.navigate("bookDetails/$bookId")
                                }
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

@Composable
fun BookItem(book: Book, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick(book.id.toString()) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val placeHolder = if (!isSystemInDarkTheme()) {
            R.drawable.no_image
        } else {
            R.drawable.no_image_white
        }
        val painter = if (book.image.isNullOrEmpty()) {
            painterResource(id = placeHolder)
        } else {
            rememberAsyncImagePainter(model = book.image, contentScale = ContentScale.Fit)
        }
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 16.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = book.title.toString(), style = MaterialTheme.typography.h6)
            Text(text = book.author.toString(), style = MaterialTheme.typography.body2)
        }
    }
}