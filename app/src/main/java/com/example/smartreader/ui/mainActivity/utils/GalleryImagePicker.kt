package com.example.smartreader.ui.mainActivity.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GalleryImagePicker(onImagePicked: (Uri) -> Unit) {

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImagePicked(it) }
    }
    Button(shape = RoundedCornerShape(16.dp),
        onClick = { galleryLauncher.launch("image/*") }) {
        Icon(
            imageVector = Icons.Default.PhotoLibrary,
            contentDescription = "Pick photo from gallery",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text("Scan text from gallery image")
    }
}