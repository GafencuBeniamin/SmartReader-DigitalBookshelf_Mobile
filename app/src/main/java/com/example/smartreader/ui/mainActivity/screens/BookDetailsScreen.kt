package com.example.smartreader.ui.mainActivity.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.smartreader.MainApplication
import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.BookStatus
import com.example.smartreader.data.entities.Note
import com.example.smartreader.ui.mainActivity.utils.BookDetails
import com.example.smartreader.ui.mainActivity.utils.DeleteFloatingButton
import com.example.smartreader.ui.mainActivity.utils.EditFloatingButton

@Composable
fun BookDetailsScreen(bookId: String, viewModel: MainViewModel, navController: NavController) {
    val bookResource by viewModel.book.observeAsState(initial = Resource.loading(null))
    val bookDeleteResource by viewModel.deletedBook.observeAsState(initial = Resource.loading(null))
    val bookWithChangedStatusResource by viewModel.bookWithChangedStatus.observeAsState(initial =  Resource.loading(null))
    val notesResource by viewModel.notesFromBook.observeAsState(initial = Resource.loading(null))
    var bookStatus : BookStatus by remember { mutableStateOf(BookStatus.PRIVATE) }
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
        var isBookStatusUpdated by remember { mutableStateOf(false) }
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
                if (!isBookStatusUpdated){
                    isBookStatusUpdated=true
                    bookStatus = bookResource.data?.isPublic ?: BookStatus.PRIVATE
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    BookDetails(bookResource = bookResource, bookStatus = bookStatus)
                    //List of Notes
                    when (notesResource.status) {
                        Resource.Status.SUCCESS -> {
                            notesResource.data?.let { notes ->
                                // Sort the notes by page
                                val sortedNotes = notes.sortedBy { it.page }

                                LazyRow(modifier = Modifier.padding(vertical = 16.dp)) {
                                    items(sortedNotes) { note ->
                                        NoteItem(note = note) { noteId ->
                                            navController.navigate("noteDetails/$noteId")
                                        }
                                    }
                                }

                            }
                        }

                        Resource.Status.LOADING -> {

                        }

                        Resource.Status.ERROR -> {

                        }
                    }
                    //Button add note
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            navController.navigate("createNote/$bookId")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddBox,
                            contentDescription = "Edit public books",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Add note")
                    }
                }
                // Floating button edit
                EditFloatingButton (onClick = {
                    if (bookStatus!=BookStatus.PUBLIC) {
                        navController.navigate("editBook/$bookId")
                    } else {
                        navController.navigate("changeBookState/$bookId")
                    }
                })
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
                DeleteFloatingButton(onClick = {showDialogDelete=true})
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
                val deleteButtonBackgroundColor = if (isSystemInDarkTheme()) {
                    Color(0xFF800000) // Dark theme color
                } else {
                    Color(0xFFFFC0CB) // Light theme color
                }
                if (showDialogDelete) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialogDelete = false
                        },
                        title = {
                            Text(text = "Confirm Deletion")
                        },
                        text = {
                            if (bookStatus!=BookStatus.PUBLIC){
                                Text("Are you sure you want to delete this book? All data will be permanently lost")
                            } else {
                                Text("Are you sure you want to delete this book from library?")
                            }

                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (bookStatus!=BookStatus.PUBLIC) {
                                        viewModel.deleteBook(bookId)
                                    } else {
                                        viewModel.removeBookFromLibrary(bookId)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = deleteButtonBackgroundColor)
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
    val noteColor = if (isSystemInDarkTheme()) {
        Color(0xFF654321) // Dark theme color
    } else {
        Color(0xFFFFFFE0) // Light theme color
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp, 100.dp)
            .clickable { onClick(note.id.toString()) },
        backgroundColor = noteColor,
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Page: ${note.page}",
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = note.title.toString(),
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
