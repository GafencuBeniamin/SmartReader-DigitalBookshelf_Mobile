package com.example.smartreader.ui.mainActivity.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartreader.MainApplication.Companion.context
import com.example.smartreader.R
import com.example.smartreader.data.entities.UserRole
import com.example.smartreader.di.SessionManager
import com.example.smartreader.ui.loginActivity.LoginActivity
import com.example.smartreader.ui.mainActivity.MainActivity
import com.example.smartreader.ui.mainActivity.viewmodels.MainViewModel
import com.example.smartreader.util.Resource

@Composable
fun AccountScreen(navController: NavController, viewModel: MainViewModel) {

    val userResource by viewModel.userDetails.observeAsState(initial = Resource.loading(null))
    val sessionManager = SessionManager(LocalContext.current)

    LaunchedEffect(Unit) {
        viewModel.getMyDetails()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        when (userResource.status) {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    userResource.data?.picture.let {
                        Image(
                            painter = painterResource(id = R.drawable.no_image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .padding(bottom = 16.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    userResource.data?.username?.let {
                        Text(text = "Username: $it", modifier = Modifier.padding(bottom = 8.dp))
                    }
                    userResource.data?.email?.let {
                        Text(text = "Email: $it", modifier = Modifier.padding(bottom = 8.dp))
                    }
                    userResource.data?.role?.let {
                        Text(text = "Role: ${it.name}", modifier = Modifier.padding(bottom = 8.dp))
                    }
                    userResource.data?.token?.let {
                        Text(text = "Token: $it", modifier = Modifier.padding(bottom = 8.dp))
                    }
                    // Logout Button
                    Button(onClick = {
                        sessionManager.deleteAuthToken()
                        // Start LoginActivity
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }) {
                        Text("Logout")
                    }
                    if (userResource.data?.role==UserRole.ADMIN || userResource.data?.role==UserRole.MODERATOR) {
                        // Moderator Button
                        Button(onClick = {
                            navController.navigate("pending")
                        }) {
                            Text("Manage book requests")
                        }
                        // Moderator Button
                        Button(onClick = {
                            navController.navigate("moderatorSearch")
                        }) {
                            Text("Edit public books")
                        }
                    }
                    if(userResource.data?.role==UserRole.ADMIN)
                    // Moderator Button
                        Button(onClick = {
                            navController.navigate("changeUserRole")
                        }) {
                            Text("Change user role")
                        }
                }
            }
            Resource.Status.ERROR -> {
                Text("Error: " + userResource.message)
            }
        }

    }
}
