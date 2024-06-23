package com.example.smartreader.ui.mainActivity.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.smartreader.ui.mainActivity.utils.ExposedDropdownMenuBox
import com.example.smartreader.ui.mainActivity.utils.ImagePicker
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun CreateBookScreen(navController: NavController, viewModel: MainViewModel) {
    val bookResource by viewModel.createdBook.observeAsState(initial = Resource.loading(null))
    val titleState = remember { mutableStateOf("") }
    val authorState = remember { mutableStateOf("") }
    val pagesState = remember { mutableStateOf("") }
    val languageState = remember { mutableStateOf("") }
    val genreState = remember { mutableStateOf("") }
    val imageState = remember { mutableStateOf("") }
    val editureState = remember { mutableStateOf("") }
    val stateState = remember { mutableStateOf(BookState.TO_BE_READ) } // Default state
    val bookStateOptions = remember { BookState.values().map { it.name } }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = titleState.value,
                onValueChange = { titleState.value = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = authorState.value,
                onValueChange = { authorState.value = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = editureState.value,
                onValueChange = { editureState.value = it },
                label = { Text("Editure") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pagesState.value,
                onValueChange = { pagesState.value = it },
                label = { Text("Number of Pages") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = languageState.value,
                onValueChange = { languageState.value = it },
                label = { Text("Language") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = genreState.value,
                onValueChange = { genreState.value = it },
                label = { Text("Genre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                options = bookStateOptions,
                initialSelectedOption = stateState.value.name,
                onOptionSelected = { selectedState ->
                    stateState.value = BookState.valueOf(selectedState)
                }
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
                    Image(
                        painter = painterResource(id = R.drawable.no_image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(), // Maintain aspect ratio
                        contentScale = ContentScale.Fit
                    )
                }
            }

            ImagePicker(viewModel = viewModel, onImagePicked = { uri ->
                imageState.value = uri.toString()
            })

            Button(
                onClick = {
                    val book = Book(
                        title = titleState.value,
                        author = setOf(authorState.value),
                        noOfPages = pagesState.value.toIntOrNull(),
                        language = languageState.value,
                        genre = genreState.value,
                        image = imageState.value,
                        bookStates = mapOf("0" to stateState.value),
                        editure = editureState.value
                    )
                    viewModel.createBook(book)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                when (bookResource.status) {
                    Resource.Status.LOADING -> {
                        // Handle loading state if needed
                    }
                    Resource.Status.SUCCESS -> {
                        Toast.makeText(context, "Book created successfully!", Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
                        navController.navigate("dashboard")
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(context, "Error: " + bookResource.message, Toast.LENGTH_SHORT).show()
                    }
                }
                Text("Add Book")
            }
        }
    }
}