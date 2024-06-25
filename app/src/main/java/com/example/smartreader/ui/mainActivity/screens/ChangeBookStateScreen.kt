package com.example.smartreader.ui.mainActivity.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartreader.MainApplication.Companion.context
import com.example.smartreader.R
import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.BookState
import com.example.smartreader.data.entities.BookStatus
import com.example.smartreader.ui.mainActivity.utils.ExposedDropdownMenuBox
import com.example.smartreader.ui.mainActivity.utils.ImagePicker
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun ChangeBookStateScreen(bookId: String, navController: NavController, viewModel: MainViewModel) {
    val bookResource by viewModel.book.observeAsState(initial = Resource.loading(null))
    val bookEditResource by viewModel.editedBook.observeAsState(initial = Resource.loading(null))
    val idState = remember { mutableStateOf("") }
    val titleState = remember { mutableStateOf("") }
    val authorState = remember { mutableStateOf("") }
    val pagesState = remember { mutableStateOf("") }
    val languageState = remember { mutableStateOf("") }
    val genreState = remember { mutableStateOf("") }
    val imageState = remember { mutableStateOf("") }
    val editureState = remember { mutableStateOf("") }
    val stateState = remember { mutableStateOf(BookState.TO_BE_READ) } // Default state
    val bookStateOptions = remember { BookState.values().map { it.name } }

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
                if (idState.value.isEmpty()) {
                    idState.value = bookResource.data?.id?.toString() ?: ""
                    titleState.value = bookResource.data?.title ?: ""
                    authorState.value = bookResource.data?.author?.joinToString(", ") ?: ""
                    editureState.value = bookResource.data?.editure ?: ""
                    pagesState.value = bookResource.data?.noOfPages?.toString() ?: ""
                    languageState.value = bookResource.data?.language ?: ""
                    genreState.value = bookResource.data?.genre ?: ""
                    stateState.value = bookResource.data?.bookStates?.entries?.firstOrNull()?.value ?: BookState.TO_BE_READ
                    imageState.value = bookResource.data?.image ?: ""
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Book Title
                    Text(
                        text = titleState.value ?: "Unknown Title",
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    //Image box
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(200.dp) // Adjust width as needed
                            .height(267.dp) // Calculate height based on the aspect ratio (3:4)
                            .aspectRatio(3f / 4f)
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        if (imageState.value.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(imageState.value),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(), // Maintain aspect ratio
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            val placeHolder = if (!isSystemInDarkTheme()) {
                                R.drawable.no_image
                            } else {
                                R.drawable.no_image_white
                            }
                            Image(
                                painter = painterResource(id = placeHolder),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(), // Maintain aspect ratio
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ExposedDropdownMenuBox(
                        options = bookStateOptions,
                        initialSelectedOption = stateState.value.name,
                        onOptionSelected = { selectedState ->
                            stateState.value = BookState.valueOf(selectedState)
                        }
                    )


                    Button(
                        onClick = {
                            viewModel.editPublicBook(bookId, stateState.value)
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit public books",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Edit Book State")
                        when (bookEditResource.status) {
                            Resource.Status.LOADING -> {
                                // Handle loading state if needed
                            }

                            Resource.Status.SUCCESS -> {
                                Toast.makeText(context, "Book state changed successfully!", Toast.LENGTH_SHORT).show()
                                viewModel.resetState()
                                navController.navigate("dashboard")
                            }

                            Resource.Status.ERROR -> {
                                Toast.makeText(
                                    context,
                                    "Error: " + bookResource.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
            Resource.Status.ERROR -> {
                Text("Error: " + bookResource.message)
            }
        }
    }
}