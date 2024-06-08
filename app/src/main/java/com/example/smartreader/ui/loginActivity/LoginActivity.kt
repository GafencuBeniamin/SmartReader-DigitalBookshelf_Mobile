package com.example.smartreader.ui.loginActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.smartreader.ui.loginActivity.navigation.LoginNavGraph
import com.example.smartreader.ui.loginActivity.viewmodels.LoginViewModel
import com.example.smartreader.ui.theme.SmartReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartReaderTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: LoginViewModel) {
    val navController = rememberNavController()
    LoginNavGraph(navController = navController, viewModel)
}