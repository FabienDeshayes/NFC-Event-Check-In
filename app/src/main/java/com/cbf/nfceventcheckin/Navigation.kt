package com.cbf.nfceventcheckin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(isLoggedIn: Boolean, isCheckedIn: Boolean) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)
    val checkedInEmails = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        checkedInEmails.clear()
        checkedInEmails.addAll(dbHelper.getAllCheckedInEmails())
    }
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn && isCheckedIn) "check_in_result_screen" else if (isLoggedIn) "event_details_screen" else "login_screen"
    ) {
        composable("login_screen") { LoginScreen(navController) }
        composable("event_details_screen") { EventDetailsScreen(navController) }
        composable("check_in_result_screen") { CheckInResultScreen(navController) }
        composable("check_in_guidance_screen") { CheckInGuidanceScreen() }
        composable("admin_screen") { AdminScreen(checkedInEmails) }
    }
}