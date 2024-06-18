package com.example.smartreader.ui.mainActivity.screens

import android.content.Context
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartreader.MainApplication
import com.example.smartreader.MainApplication.Companion.context
import com.example.smartreader.R
import com.example.smartreader.data.entities.BookState
import com.example.smartreader.data.entities.Note
import com.example.smartreader.ui.mainActivity.utils.CameraPreviewScanner
import com.example.smartreader.ui.mainActivity.utils.ExposedDropdownMenuBox
import com.example.smartreader.ui.mainActivity.utils.GalleryImagePicker
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun CreateNoteScreen(navController: NavController, viewModel: MainViewModel, bookId: String) {

    val noteResource by viewModel.createdNote.observeAsState(initial = Resource.loading(null))
    val titleState = remember { mutableStateOf("") }
    val pageState = remember { mutableStateOf("") }
    val contentState = remember { mutableStateOf("") }
    val commentState = remember { mutableStateOf("") }
    val showCamera = remember { mutableStateOf(false) }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                    value = pageState.value,
                    onValueChange = { pageState.value = it },
                    label = { Text("Page") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contentState.value,
                    onValueChange = { contentState.value = it },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = commentState.value,
                    onValueChange = { commentState.value = it },
                    label = { Text("Comment") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = { showCamera.value = true }) {
                    Text("Scan Text with Camera")
                }
                GalleryImagePicker(onImagePicked = { uri ->
                    selectedImageUri.value = uri
                })

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val note = Note(
                            title = titleState.value,
                            comment = commentState.value,
                            content = contentState.value,
                            page = pageState.value.toIntOrNull(),
                            book = bookId.toIntOrNull()
                        )
                        viewModel.createNote(note)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    when (noteResource.status) {
                        Resource.Status.LOADING -> {
                            // Handle loading state if needed
                        }
                        Resource.Status.SUCCESS -> {
                            Toast.makeText(context, "Note created successfully!", Toast.LENGTH_SHORT).show()
                            viewModel.resetState()
                            navController.navigate("dashboard")
                        }
                        Resource.Status.ERROR -> {
                            Toast.makeText(context, "Error: " + noteResource.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    Text("Add Note")
                }
            }

            if (showCamera.value) {
                CameraPreviewScanner(onTextRecognized = { text ->
                    contentState.value = text
                    showCamera.value = false
                },
                    onClose = {
                        showCamera.value = false
                    })
            }

            selectedImageUri.value?.let { uri ->
                LaunchedEffect(uri) {
                    extractTextFromImage(uri, context = context) { text ->
                        contentState.value = text
                        selectedImageUri.value = null // Reset the image URI after processing
                    }
                }
            }
        }

    }
}
private fun extractTextFromImage(uri: Uri, context: Context, onTextExtracted: (String) -> Unit) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val inputImage = InputImage.fromFilePath(context, uri)

    recognizer.process(inputImage)
        .addOnSuccessListener { visionText ->
            onTextExtracted(visionText.text)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
        }
}