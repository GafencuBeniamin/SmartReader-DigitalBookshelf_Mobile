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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
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
import com.example.smartreader.data.entities.User
import com.example.smartreader.ui.mainActivity.utils.ExposedDropdownMenuBox
import com.example.smartreader.ui.mainActivity.utils.ImagePicker
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun ChangeAccountDetails(navController: NavController, viewModel: MainViewModel) {
    val userResource by viewModel.userDetails.observeAsState(initial = Resource.loading(null))
    val userEditResource by viewModel.userEdited.observeAsState(initial = Resource.loading(null))
    val idState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val usernameState = remember { mutableStateOf("") }
    val pictureState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getMyDetails()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        when (userResource.status){
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
                    idState.value = userResource.data?.id?.toString() ?: ""
                    emailState.value = userResource.data?.email ?: ""
                    usernameState.value = userResource.data?.username ?: ""
                    pictureState.value = userResource.data?.picture ?: ""
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = usernameState.value,
                        onValueChange = { usernameState.value = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                        if (pictureState.value.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(pictureState.value),
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
                        pictureState.value = uri.toString()
                    })

                    Button(
                        onClick = {
                            val user = User(
                                email=emailState.value,
                                picture=pictureState.value,
                                username = usernameState.value
                            )
                            viewModel.updateMyDetails(user)
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Edit my details")
                        when (userEditResource.status) {
                            Resource.Status.LOADING -> {
                                // Handle loading state if needed
                            }

                            Resource.Status.SUCCESS -> {
                                Toast.makeText(context, "Changes made successfully!", Toast.LENGTH_SHORT).show()
                                viewModel.resetState()
                                navController.navigate("account")
                            }

                            Resource.Status.ERROR -> {
                                Toast.makeText(
                                    context,
                                    "Error: " + userEditResource.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
            Resource.Status.ERROR -> {
                Text("Error: " + userResource.message)
            }
        }
    }
}