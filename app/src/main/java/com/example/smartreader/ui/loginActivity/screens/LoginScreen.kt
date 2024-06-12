package com.example.smartreader.ui.loginActivity.screens

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.example.smartreader.di.SessionManager
import com.example.smartreader.ui.mainActivity.MainActivity

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loggedInUserResource by viewModel.loggedInUser.observeAsState(initial = Resource.loading(null))
    val sessionManager = SessionManager(LocalContext.current)

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
                    sessionManager.saveAuthToken(loggedInUserResource.data?.token.toString())
                    // Start MainActivity
                    val intent = Intent(LocalContext.current, MainActivity::class.java)
                    LocalContext.current.startActivity(intent)
                }
                Resource.Status.ERROR -> {
                    Text("Error: " + loggedInUserResource.message)
                }
            }
        }
    }
}
