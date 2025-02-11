package com.example.smartreader.ui.mainActivity.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartreader.MainApplication
import com.example.smartreader.R
import com.example.smartreader.data.entities.BookStatus
import com.example.smartreader.ui.mainActivity.utils.BookDetails
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun SearchedBookScreen(bookId: String, viewModel: MainViewModel, navController: NavController) {
    val bookResource by viewModel.book.observeAsState(initial = Resource.loading(null))
    val bookAddResource by viewModel.addedBook.observeAsState(initial =  Resource.loading(null))
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bookId) {
        viewModel.getBookById(bookId)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        when (bookResource.status){
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
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    val bookStatus = bookResource.data?.isPublic ?: BookStatus.PRIVATE
                    BookDetails(bookResource = bookResource, bookStatus = bookStatus)
                }
                // Floating button request for book
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = {
                            showDialog = true
                        },
                        modifier = Modifier
                            .padding(30.dp)
                            .width(200.dp)
                            .height(56.dp)
                            .align(Alignment.BottomCenter),
                    ) {
                        Text("Add book to your library")
                    }
                }

                //Accept Dialog
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        title = {
                            Text(text = "Add book")
                        },
                        text = {
                            Text("You are adding a public book to your personal library. Are you sure?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.addPublicBookToLibrary(bookId)
                                }
                            ) {
                                when (bookAddResource.status) {
                                    Resource.Status.LOADING -> {
                                        // Handle loading state if needed
                                    }
                                    Resource.Status.SUCCESS -> {
                                        Toast.makeText(MainApplication.context, "Book added to library!", Toast.LENGTH_SHORT).show()
                                        viewModel.resetState()
                                        showDialog = false
                                        navController.navigate("dashboard")
                                    }
                                    Resource.Status.ERROR -> {
                                        showDialog = false
                                        Toast.makeText(MainApplication.context, "Error: " + bookAddResource.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showDialog = false // Dismiss the dialog
                                }
                            ) {
                                Text("No")
                            }
                        },
                        modifier = Modifier
                            .border(
                                BorderStroke(2.dp, Color.LightGray),
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            }
            Resource.Status.ERROR -> {
                Text("Error: " + bookResource.message)
            }
        }
    }
}