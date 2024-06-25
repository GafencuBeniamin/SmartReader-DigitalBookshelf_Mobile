package com.example.smartreader.ui.mainActivity.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditFloatingButton(onClick: () -> Unit) {
    val deleteButtonBackgroundColor = if (isSystemInDarkTheme()) {
        Color(0xFF004C7F) // Dark theme color
    } else {
        Color(0xFFADD8E6) // Light theme color
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .padding(30.dp)
                .size(56.dp)
                .align(Alignment.BottomStart),
            backgroundColor = deleteButtonBackgroundColor
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Delete Book",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}