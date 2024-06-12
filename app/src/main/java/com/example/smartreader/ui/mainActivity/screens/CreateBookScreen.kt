package com.example.smartreader.ui.mainActivity.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartreader.MainApplication.Companion.context
import com.example.smartreader.data.entities.Book
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

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = titleState.value,
            onValueChange = { titleState.value = it },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = authorState.value,
            onValueChange = { authorState.value = it },
            label = { Text("Author") }
        )
        OutlinedTextField(
            value = pagesState.value,
            onValueChange = { pagesState.value = it },
            label = { Text("Number of Pages") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = languageState.value,
            onValueChange = { languageState.value = it },
            label = { Text("Language") }
        )
        OutlinedTextField(
            value = genreState.value,
            onValueChange = { genreState.value = it },
            label = { Text("Genre") }
        )
        OutlinedTextField(
            value = imageState.value,
            onValueChange = { imageState.value = it },
            label = { Text("Image URL") }
        )

        Button(
            onClick = {
                val book = Book(
                    title = titleState.value,
                    author = setOf(authorState.value),
                    noOfPages = pagesState.value.toIntOrNull(),
                    language = languageState.value,
                    genre = genreState.value,
                    image = imageState.value
                )
                viewModel.createBook(book)
                when (bookResource.status) {
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.SUCCESS -> {
                        Toast.makeText(context, "Book created successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate("dashboard")
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(context,"Error: " + bookResource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Add Book")
        }

    }
}