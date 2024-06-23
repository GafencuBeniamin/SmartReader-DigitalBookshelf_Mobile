package com.example.smartreader.ui.mainActivity.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun SearchScreen(navController: NavController, viewModel: MainViewModel) {
    var searchText by remember { mutableStateOf("") }
    val booksResource by viewModel.publicBooks.observeAsState(initial = Resource.loading(null))

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                viewModel.searchPublicBooks(it)
            },
            label = { Text("Search books") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (searchText.trim().isNotEmpty()){
            true -> {
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
                                        navController.navigate("searchedBook/$bookId")
                                    }
                                }
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        Text("Error: " + booksResource.message)
                    }
                }
            }
            false -> {

            }
        }

    }
}