package com.cbf.nfceventcheckin

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(isLoggedIn: Boolean, isCheckedIn: Boolean) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn && isCheckedIn) "check_in_result_screen" else if (isLoggedIn) "event_details_screen" else "login_screen"
    ) {
        composable("login_screen") { LoginScreen(navController) }
        composable("event_details_screen") { EventDetailsScreen(navController) }
        composable("check_in_result_screen") { CheckInResultScreen(navController) }
        composable("check_in_guidance_screen") { CheckInGuidanceScreen() }
//        composable("admin_screen") { AdminScreen(checkedInUsers) }
    }
}