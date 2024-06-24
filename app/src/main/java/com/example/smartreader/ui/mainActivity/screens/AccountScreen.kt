package com.example.smartreader.ui.mainActivity.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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
                    val placeHolder = if (!isSystemInDarkTheme()) {
                        R.drawable.no_image
                    } else {
                        R.drawable.no_image_white
                    }
                    val painter = if (userResource.data?.picture.isNullOrEmpty()) {
                        painterResource(id = placeHolder)
                    } else {
                        rememberAsyncImagePainter(model = userResource.data?.picture)
                    }
                    // User avatar
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // User information
                    userResource.data?.username?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Username: $it",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    userResource.data?.email?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Email: $it",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    userResource.data?.role?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Role: ${it.name}",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Logout Button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                        sessionManager.deleteAuthToken()
                        // Start LoginActivity
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Logout")
                    }
                    if (userResource.data?.role==UserRole.ADMIN || userResource.data?.role==UserRole.MODERATOR) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)  // Add space between the buttons
                        ){// Moderator Button
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                onClick = {
                                    navController.navigate("pending")
                                }) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = "Manage book requests",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text("Manage book requests")
                            }
                            // Moderator Button
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                onClick = {
                                    navController.navigate("moderatorSearch")
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit public books",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text("Edit public books")
                            }
                        }
                    }
                    if (userResource.data?.role == UserRole.ADMIN)
                    // Moderator Button
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            onClick = {
                            navController.navigate("changeUserRole")
                        }) {
                            Icon(
                                imageVector = Icons.Default.SupervisorAccount,
                                contentDescription = "Change user role",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Change user role")
                        }
                }
                // Floating button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("changeAccountDetails")
                        },
                        modifier = Modifier
                            .padding(30.dp)
                            .size(56.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Change Account Details",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            Resource.Status.ERROR -> {
                Text("Error: " + userResource.message)
            }
        }

    }
}
