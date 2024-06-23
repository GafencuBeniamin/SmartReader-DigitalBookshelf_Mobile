package com.example.smartreader.ui.mainActivity.bottomBar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.smartreader.R

sealed class BottomNavItem(val route: String, @StringRes val title: Int, val icon: ImageVector) {
    object Dashboard : BottomNavItem("dashboard", R.string.dashboard, Icons.Filled.Home)
    object Account : BottomNavItem("account", R.string.account, Icons.Filled.AccountCircle)
    object Search : BottomNavItem("search",R.string.search, Icons.Filled.Search)
}