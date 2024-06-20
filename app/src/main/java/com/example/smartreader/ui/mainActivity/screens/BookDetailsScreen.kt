package com.example.smartreader.ui.mainActivity.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartreader.R
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource
import androidx.compose.ui.graphics.Color
import com.example.smartreader.MainApplication
import com.example.smartreader.data.entities.BookStatus
import com.example.smartreader.data.entities.Note

@Composable
fun BookDetailsScreen(bookId: String, viewModel: MainViewModel, navController: NavController) {
    val bookResource by viewModel.book.observeAsState(initial = Resource.loading(null))
    val bookDeleteResource by viewModel.deletedBook.observeAsState(initial = Resource.loading(null))
    val bookWithChangedStatusResource by viewModel.bookWithChangedStatus.observeAsState(initial =  Resource.loading(null))
    val notesResource by viewModel.notesFromBook.observeAsState(initial = Resource.loading(null))
    var bookStatus : BookStatus by remember { mutableStateOf(BookStatus.PRIVATE) }
    val idState = remember { mutableStateOf("") }
    var showDialogDelete by remember { mutableStateOf(false) }
    var showDialogRequest by remember { mutableStateOf(false) }
    var showDialogCancel by remember { mutableStateOf(false) }

    LaunchedEffect(bookId) {
        viewModel.getBookById(bookId)
        viewModel.getMyNotesFromBook(bookId)
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
                if (idState.value.isEmpty()) {
                    idState.value = bookResource.data?.id?.toString() ?: ""
                    bookStatus = bookResource.data?.isPublic ?: BookStatus.PRIVATE
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = bookResource.data?.title.toString(), style = MaterialTheme.typography.h4)
                    Text(text = bookResource.data?.author.toString(), style = MaterialTheme.typography.h6)
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

                    //List of Notes
                    when (notesResource.status){
                        Resource.Status.SUCCESS ->{
                            notesResource.data?.let { notes ->
                                // Sort the notes by page
                                val sortedNotes = notes.sortedBy { it.page }

                                LazyRow( modifier = Modifier.padding(vertical = 16.dp)){
                                    items(sortedNotes) { note ->
                                        NoteItem(note = note) { noteId ->
                                            navController.navigate("noteDetails/$noteId")
                                        }
                                    }
                                }

                            }
                        }
                        Resource.Status.LOADING->{

                        }
                        Resource.Status.ERROR->{

                        }
                    }
                    //Button add note
                    Button(
                        onClick = {
                            navController.navigate("createNote/$bookId")
                        }
                    ) {
                        Text("Add note")
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
                            .align(Alignment.BottomStart),
                        backgroundColor = Color(0xFFADD8E6)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Book",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                // Floating button request for book
                if (bookStatus!=BookStatus.PUBLIC){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        FloatingActionButton(
                            onClick = {
                                if (bookStatus==BookStatus.PRIVATE){
                                    showDialogRequest =true
                                }
                                if (bookStatus==BookStatus.PENDING){
                                    showDialogCancel =true
                                }
                            },
                            modifier = Modifier
                                .padding(30.dp)
                                .width(200.dp)
                                .height(56.dp)
                                .align(Alignment.BottomCenter),
                        ) {
                            when (bookStatus){
                                BookStatus.PRIVATE ->{
                                    Text("Request to be public")
                                }
                                BookStatus.PENDING->{
                                    Text("Cancel public request")
                                }
                                BookStatus.PUBLIC->{

                                }
                            }
                        }
                    }
                }
                // Floating button delete
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = {
                            showDialogDelete=true
                        },
                        modifier = Modifier
                            .padding(30.dp)
                            .size(56.dp)
                            .align(Alignment.BottomEnd),
                        backgroundColor = Color(0xFFFFC0CB)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Book",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                //Cancel Dialog
                if (showDialogCancel) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialogCancel = false
                        },
                        title = {
                            Text(text = "Cancel Request")
                        },
                        text = {
                            Text("You are canceling a request for a book to be public. Book will stay private until another request is made. Are you sure?")
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
                                        Toast.makeText(MainApplication.context, "Request canceled successfully!", Toast.LENGTH_SHORT).show()
                                        viewModel.resetBookWithChangedStatus()
                                        bookStatus=BookStatus.PRIVATE
                                        showDialogCancel = false
                                    }
                                    Resource.Status.ERROR -> {
                                        showDialogCancel = false
                                        Toast.makeText(MainApplication.context, "Error: " + bookWithChangedStatusResource.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showDialogCancel = false // Dismiss the dialog
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
                //Request Dialog
                if (showDialogRequest) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialogRequest = false
                        },
                        title = {
                            Text(text = "Confirm Request")
                        },
                        text = {
                            Text("You are requesting a book to be public. Once accepted you can't modify its details anymore. Are you sure?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.changeBookStatus(bookId, BookStatus.PENDING)
                                }
                            ) {
                                when (bookWithChangedStatusResource.status) {
                                    Resource.Status.LOADING -> {
                                        // Handle loading state if needed
                                    }
                                    Resource.Status.SUCCESS -> {
                                        Toast.makeText(MainApplication.context, "Request made successfully!", Toast.LENGTH_SHORT).show()
                                        viewModel.resetBookWithChangedStatus()
                                        bookStatus=BookStatus.PENDING
                                        showDialogRequest = false
                                    }
                                    Resource.Status.ERROR -> {
                                        showDialogRequest = false
                                        Toast.makeText(MainApplication.context, "Error: " + bookWithChangedStatusResource.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showDialogRequest = false // Dismiss the dialog
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
                //Delete Dialog
                if (showDialogDelete) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialogDelete = false
                        },
                        title = {
                            Text(text = "Confirm Deletion")
                        },
                        text = {
                            Text("Are you sure you want to delete this book?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.deleteBook(bookId)
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFC0CB))
                            ) {
                                when (bookDeleteResource.status) {
                                    Resource.Status.LOADING -> {
                                        // Handle loading state if needed
                                    }
                                    Resource.Status.SUCCESS -> {
                                        Toast.makeText(MainApplication.context, "Book deleted successfully!", Toast.LENGTH_SHORT).show()
                                        viewModel.resetState()
                                        showDialogDelete = false
                                        navController.navigate("dashboard")
                                    }
                                    Resource.Status.ERROR -> {
                                        showDialogDelete = false
                                        Toast.makeText(MainApplication.context, "Error: " + bookDeleteResource.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showDialogDelete = false // Dismiss the dialog
                                }
                            ) {
                                Text("Cancel")
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
@Composable
fun NoteItem(note: Note, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp, 100.dp)
            .clickable { onClick(note.id.toString()) },
        backgroundColor = Color(0xFFFFFFE0),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Page: ${note.page}", style = MaterialTheme.typography.subtitle1)
            Text(text = note.title.toString(), style = MaterialTheme.typography.h6)
        }
    }
}
