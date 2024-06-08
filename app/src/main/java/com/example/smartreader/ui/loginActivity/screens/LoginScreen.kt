package com.example.smartreader.ui.loginActivity.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartreader.data.entities.LogInCredentials
import com.example.smartreader.ui.loginActivity.viewmodels.LoginViewModel
import com.example.smartreader.util.Resource
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loggedInUserResource by viewModel.loggedInUser.observeAsState(initial = Resource.loading(null))

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Log In", style = MaterialTheme.typography.h4, modifier = Modifier.padding(bottom = 16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val credentials = LogInCredentials(username, password)
                    viewModel.logInUser(credentials)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
            Spacer(modifier = Modifier.height(16.dp))

            when (loggedInUserResource.status) {
                Resource.Status.LOADING -> {
                    CircularProgressIndicator()
                }
                Resource.Status.SUCCESS -> {
                    Text("Logged in: Welcome "+ loggedInUserResource.data?.username)
                }
                Resource.Status.ERROR -> {
                    Text("Error: " + loggedInUserResource.message)
                }
            }
        }
    }
}
