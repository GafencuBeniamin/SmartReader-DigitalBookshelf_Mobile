package com.example.smartreader.ui.loginActivity.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartreader.ui.loginActivity.screens.LoginScreen
import com.example.smartreader.ui.loginActivity.screens.RegisterScreen
import com.example.smartreader.ui.loginActivity.screens.StartScreen
import com.example.smartreader.ui.loginActivity.viewmodels.LoginViewModel

@Composable
fun LoginNavGraph(navController: NavHostController, viewModel: LoginViewModel) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(navController)
        }
        composable("login") {
            LoginScreen(viewModel)
        }
        composable("register") {
            RegisterScreen(viewModel,navController)
        }
    }
}