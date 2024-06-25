package com.example.smartreader.ui.mainActivity.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.InputStream

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePicker(viewModel: MainViewModel, onImagePicked: (Uri) -> Unit) {
    val context = LocalContext.current
    val imageUri by viewModel.currentPhotoPath.observeAsState()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            onImagePicked(Uri.parse(imageUri))
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileUri = saveImageFromGallery(context, uri)
            fileUri?.let { onImagePicked(it) }
        }
    }

    Row(
        modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        if (rememberPermissionState(
                permission = Manifest.permission.CAMERA
            ).status.isGranted) {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                try {
                    val imageFile = createImageFile(context)
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        imageFile
                    )
                    viewModel.currentPhotoPath.value = imageFile.absolutePath
                    cameraLauncher.launch(uri)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            },
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Edit public books",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Take Photo")
            }
        } else {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {},
                enabled = false
            ){
                Text("Please grant Camera permissions in app settings to use camera feature.")
            }

        }
        Button(modifier = Modifier.padding(8.dp), onClick = { galleryLauncher.launch("image/*") }) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = "Edit public books",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Pick from Gallery")
        }
    }
}

@Throws(IOException::class)
private fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
}

private fun saveImageFromGallery(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "JPEG_${timeStamp}.jpg")
        inputStream?.use { input ->
            imageFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}