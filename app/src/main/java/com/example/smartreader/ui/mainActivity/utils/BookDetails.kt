package com.example.smartreader.ui.mainActivity.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.smartreader.R
import com.example.smartreader.data.entities.Book
import com.example.smartreader.data.entities.BookStatus
import com.example.smartreader.util.Resource

@Composable
fun BookDetails(bookResource : Resource<Book>, bookStatus: BookStatus){
    val book = bookResource.data

    // Book Title
    Text(
        text = book?.title ?: "Unknown Title",
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    // Book Image and Basic Info
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        // Book Image
        val placeHolder = if (!isSystemInDarkTheme()) {
            R.drawable.no_image
        } else {
            R.drawable.no_image_white
        }

        val painter = if (book?.image.isNullOrEmpty()) {
            painterResource(id = placeHolder)
        } else {
            rememberAsyncImagePainter(model = book?.image)
        }

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(end = 16.dp),
            contentScale = ContentScale.Fit
        )

        // Basic Info Column
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Authors
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Author:",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                if (book?.author?.joinToString(", ").isNullOrEmpty())
                    Text(
                        text = "Unknown Author",
                        style = MaterialTheme.typography.h6
                    )
                else
                    Text(
                        text = book?.author?.joinToString(", ") ?: "Unknown Author",
                        style = MaterialTheme.typography.h6
                    )
            }
            // Status
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Status:",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = bookStatus.toString() ?: "Unknown",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }

    Divider()

    // Other Book Details
    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        if (book?.noOfPages != null) {
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Pages:",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = book.noOfPages.toString(),
                    style = MaterialTheme.typography.body1
                )
            }
        }

        if (!book?.language.isNullOrEmpty()) {
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Language:",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = book?.language.toString(),
                    style = MaterialTheme.typography.body1
                )
            }
        }

        if (!book?.genre.isNullOrEmpty()) {
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Genre:",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = book?.genre.toString(),
                    style = MaterialTheme.typography.body1
                )
            }
        }

        if (!book?.editure.isNullOrEmpty()) {
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Publisher:",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = book?.editure.toString(),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}