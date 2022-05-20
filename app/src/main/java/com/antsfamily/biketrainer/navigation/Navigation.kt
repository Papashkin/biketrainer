package com.antsfamily.biketrainer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen
import com.antsfamily.biketrainer.ui.splash.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen.Content(
                navigateToMain = { profileName ->
                    navController.navigate("main/$profileName") {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                navigateToCreateAccount = {
                    navController.navigate(Screen.CreateProfile.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Screen.CreateProfile.route) {
            CreateProfileScreen.Content(
                navigateToMain = { profileName ->
                    navController.navigate("main/$profileName") {
                        popUpTo(Screen.CreateProfile.route) { inclusive = true }
                    }
                },
            )
        }
        composable(
            Screen.Main.route,
            arguments = listOf(navArgument("profileName") { type = NavType.StringType })
        ) {
            //TODO finish it
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main/{profileName}")
    object CreateProfile : Screen("createProfile")
}
