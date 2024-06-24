package com.example.smartreader.ui.mainActivity.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartreader.MainApplication
import com.example.smartreader.data.entities.Note
import com.example.smartreader.ui.mainActivity.utils.DeleteFloatingButton
import com.example.smartreader.ui.mainActivity.utils.EditFloatingButton
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun NoteDetailsScreen(noteId: String, viewModel: MainViewModel, navController: NavController) {
    val noteResource by viewModel.note.observeAsState(initial = Resource.loading(null))
    val noteDeleteResource by viewModel.deletedNote.observeAsState(initial = Resource.loading(null))
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        viewModel.getNoteById(noteId)
    }
    when (noteResource.status) {
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
            noteResource.data?.let { note ->
                NoteDetails(note = note, navController = navController)
            }
            // Floating button edit
            EditFloatingButton(onClick = { navController.navigate("editNote/$noteId") })
            // Floating button delete
            DeleteFloatingButton(onClick = {showDialog=true})
            //Delete Dialog
            val deleteButtonBackgroundColor = if (isSystemInDarkTheme()) {
                Color(0xFF800000) // Dark theme color
            } else {
                Color(0xFFFFC0CB) // Light theme color
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    title = {
                        Text(text = "Confirm Deletion")
                    },
                    text = {
                        Text("Are you sure you want to delete this note?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.deleteNote(noteId)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = deleteButtonBackgroundColor)
                        ) {
                            when (noteDeleteResource.status) {
                                Resource.Status.LOADING -> {
                                    // Handle loading state if needed
                                }
                                Resource.Status.SUCCESS -> {
                                    Toast.makeText(MainApplication.context, "Note deleted successfully!", Toast.LENGTH_SHORT).show()
                                    viewModel.resetState()
                                    showDialog = false
                                    navController.navigate("dashboard")
                                }
                                Resource.Status.ERROR -> {
                                    Toast.makeText(MainApplication.context, "Error: " + noteDeleteResource.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialog = false // Dismiss the dialog
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
            Text("Error: " + noteResource.message)
        }
    }
}

@Composable
fun NoteDetails(note: Note, navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = note.title ?: "No Title",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Page: ${note.page ?: "N/A"}",
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color.LightGray, thickness = 1.dp)
            Text(
                text = "Content:",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = note.content ?: "No Content",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "Comment:",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = note.comment ?: "No Comment",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Back")
            }
        }
    }
}