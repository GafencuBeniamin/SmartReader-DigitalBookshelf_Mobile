package com.example.smartreader.ui.mainActivity.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.smartreader.R
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun BookDetailsScreen(bookId: String, viewModel: MainViewModel) {
    val bookResource by viewModel.book.observeAsState(initial = Resource.loading(null))

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        viewModel.getBookById(bookId)
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
                }
            }
            Resource.Status.ERROR -> {
                Text("Error: " + bookResource.message)
            }
        }

    }
}
