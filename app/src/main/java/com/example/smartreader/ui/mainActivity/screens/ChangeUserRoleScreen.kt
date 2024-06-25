package com.example.smartreader.ui.mainActivity.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartreader.MainApplication.Companion.context
import com.example.smartreader.R
import com.example.smartreader.data.entities.BookState
import com.example.smartreader.data.entities.UserRole
import com.example.smartreader.ui.mainActivity.utils.ExposedDropdownMenuBox
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun ChangeUserRoleScreen(navController: NavController, viewModel: MainViewModel) {

    val userEditedResource by viewModel.userEdited.observeAsState(initial = Resource.loading(null))
    val roleState = remember { mutableStateOf(UserRole.USER) }
    val userRoleOptions = remember { UserRole.values().map { it.name } }
    val usernameState = remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


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
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                options = userRoleOptions,
                initialSelectedOption = roleState.value.name,
                onOptionSelected = { selectedState ->
                    roleState.value = UserRole.valueOf(selectedState)
                }
            )

            Button(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Change User Role")
            }
            //Confirm Dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    title = {
                        Text(text = "Confirm user role")
                    },
                    text = {
                        Text("Are you sure you want to make user " + usernameState.value + " a " + roleState.value)
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.changeUserRole(role = roleState.value, username = usernameState.value)
                            }
                        ) {
                            when (userEditedResource.status) {
                                Resource.Status.LOADING -> {
                                    // Handle loading state if needed
                                }
                                Resource.Status.SUCCESS -> {
                                    Toast.makeText(context, "User role changed successfully!", Toast.LENGTH_SHORT).show()
                                    viewModel.resetState()
                                    showDialog = false
                                    navController.navigate("account")
                                }
                                Resource.Status.ERROR -> {
                                    showDialog = false
                                    Toast.makeText(context, "Error: User doesn't exist!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialog = false // Dismiss the dialog
                            }
                        ) {
                            Text("Cancel")
                        }
                    },
                    modifier = Modifier
                        .border(
                            BorderStroke(2.dp, Color.LightGray),
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        }

    }
}
