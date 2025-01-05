package com.cbf.nfceventcheckin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cbf.nfceventcheckin.screens.AdminScreen
import com.cbf.nfceventcheckin.screens.CheckInGuidanceScreen
import com.cbf.nfceventcheckin.screens.CheckInResultScreen
import com.cbf.nfceventcheckin.screens.EventDetailsScreen
import com.cbf.nfceventcheckin.screens.EventListScreen
import com.cbf.nfceventcheckin.screens.LoginScreen

@Composable
fun Navigation(
    isLoggedIn: Boolean,
    isCheckedIn: Boolean,
    navController: NavHostController,
    isLoggedInState: MutableState<Boolean>
) {

    NavHost(
        navController = navController,
        startDestination = when {
            isLoggedIn && isCheckedIn -> "check_in_result_screen"
            isLoggedIn -> "event_list_screen"
            else -> "login_screen"
        }
    ) {
        composable("login_screen") { LoginScreen(navController, isLoggedInState = isLoggedInState) }
        composable("event_list_screen") { EventListScreen(events, navController) }
        composable("check_in_result_screen") { CheckInResultScreen() }
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
