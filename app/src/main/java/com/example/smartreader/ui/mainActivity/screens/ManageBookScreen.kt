package com.example.smartreader.ui.mainActivity.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
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
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun ManageBookScreen(bookId: String, viewModel: MainViewModel, navController: NavController) {
    val bookResource by viewModel.book.observeAsState(initial = Resource.loading(null))
    val bookWithChangedStatusResource by viewModel.bookWithChangedStatus.observeAsState(initial =  Resource.loading(null))
    var showDialogAccept by remember { mutableStateOf(false) }
    var showDialogDecline by remember { mutableStateOf(false) }

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
                    Text(
                        text = bookResource.data?.title.toString(),
                        style = MaterialTheme.typography.h4
                    )
                    Text(
                        text = bookResource.data?.author.toString(),
                        style = MaterialTheme.typography.h6
                    )
                    // Add other book details here
                    val painter = if (bookResource.data?.image.isNullOrEmpty()) {
                        painterResource(id = R.drawable.no_image)
                    } else {
                        rememberAsyncImagePainter(model = bookResource.data?.image)
                    }
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                // Floating button Decline
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = {
                            showDialogDecline = true
                        },
                        modifier = Modifier
                            .padding(30.dp)
                            .size(56.dp)
                            .align(Alignment.BottomEnd),
                        backgroundColor = Color(0xFFFFC0CB)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Decline Book",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                // Floating button Accept
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = {
                            showDialogAccept = true
                        },
                        modifier = Modifier
                            .padding(30.dp)
                            .size(56.dp)
                            .align(Alignment.BottomStart),
                        backgroundColor = Color(0xFF90EE90)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Accept Book",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                // Floating button edit
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("editBook/$bookId")
                        },
                        modifier = Modifier
                            .padding(30.dp)
                            .size(56.dp)
                            .align(Alignment.BottomCenter),
                        backgroundColor = Color(0xFFADD8E6)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Book",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                //Accept Dialog
                if (showDialogAccept) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialogAccept = false
                        },
                        title = {
                            Text(text = "Accept Book")
                        },
                        text = {
                            Text("You are making this public and every user can access its details or add private notes to it! Are you sure?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.changeBookStatus(bookId, BookStatus.PUBLIC)
                                }
                            ) {
                                when (bookWithChangedStatusResource.status) {
                                    Resource.Status.LOADING -> {
                                        // Handle loading state if needed
                                    }
                                    Resource.Status.SUCCESS -> {
                                        Toast.makeText(MainApplication.context, "Book is now public!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("pending")
                                        viewModel.resetBookWithChangedStatus()
                                        showDialogAccept = false
                                    }
                                    Resource.Status.ERROR -> {
                                        showDialogAccept = false
                                        Toast.makeText(MainApplication.context, "Error: " + bookWithChangedStatusResource.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showDialogAccept = false // Dismiss the dialog
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
                //Decline Dialog
                if (showDialogDecline) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialogDecline = false
                        },
                        title = {
                            Text(text = "Decline Book")
                        },
                        text = {
                            Text("You are declining the book and making it back private. Are you sure?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.changeBookStatus(bookId, BookStatus.PRIVATE)
                                }
                            ) {
                                when (bookWithChangedStatusResource.status) {
                                    Resource.Status.LOADING -> {
                                        // Handle loading state if needed
                                    }
                                    Resource.Status.SUCCESS -> {
                                        Toast.makeText(MainApplication.context, "Book is now private!", Toast.LENGTH_SHORT).show()
                                        viewModel.resetBookWithChangedStatus()
                                        navController.navigate("pending")
                                        showDialogDecline = false
                                    }
                                    Resource.Status.ERROR -> {
                                        showDialogDecline = false
                                        Toast.makeText(MainApplication.context, "Error: " + bookWithChangedStatusResource.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showDialogDecline = false // Dismiss the dialog
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