package com.cbf.nfceventcheckin

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(isLoggedIn: Boolean, isCheckedIn: Boolean) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = when {
            isLoggedIn && isCheckedIn -> "check_in_result_screen"
            isLoggedIn -> "event_list_screen"
            else -> "login_screen"
        }
    ) {
        composable("login_screen") { LoginScreen(navController) }
        composable("event_list_screen") { EventListScreen(events, navController) }
        composable("check_in_result_screen") { CheckInResultScreen(navController) }
        composable("check_in_guidance_screen") { CheckInGuidanceScreen() }
        composable("event_details_screen/{eventTitle}",
            arguments = listOf(navArgument("eventTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventTitle = backStackEntry.arguments?.getString("eventTitle") ?: return@composable
            val event = events.find { it.title == eventTitle }
            event?.let {
                EventDetailsScreen(navController, event = it)
            }
        }
        composable("admin_screen/{tagSerialNumber}",
            arguments = listOf(navArgument("tagSerialNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val tagSerialNumber =
                backStackEntry.arguments?.getString("tagSerialNumber") ?: return@composable
            val tag = events.find { it.tagSerialNumber == tagSerialNumber }
            tag?.let {
                AdminScreen(event = it)
            }
        }
    }
}