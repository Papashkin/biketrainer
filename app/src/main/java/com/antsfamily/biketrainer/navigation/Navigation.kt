package com.antsfamily.biketrainer.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen
import com.antsfamily.biketrainer.ui.history.HistoryScreen
import com.antsfamily.biketrainer.ui.home.HomeScreen
import com.antsfamily.biketrainer.ui.settings.SettingsScreen
import com.antsfamily.biketrainer.ui.splash.SplashScreen

@Composable
fun Navigation() {
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    bottomBarState.value = when (currentRoute) {
        MainBottomItem.Home.route, MainBottomItem.History.route, MainBottomItem.Settings.route -> true
        else -> false
    }

    Scaffold(
        bottomBar = { HomeBottomNavigation(navController = navController, bottomBarState) },
        content = {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(it)
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen.Content(
                        navigateToMain = {
                            navController.navigate(MainBottomItem.Home.route) { popUpToTop(navController) }
                        },
                        navigateToCreateAccount = {
                            navController.navigate(Screen.CreateProfile.route) { popUpToTop(navController) }
                        },
                    )
                }
                composable(Screen.CreateProfile.route) {
                    CreateProfileScreen.Content {
                        navController.navigate(MainBottomItem.Home.route) { popUpToTop(navController) }
                    }
                }
                composable(MainBottomItem.Home.route) {
                    HomeScreen.Content()
                }
                composable(MainBottomItem.History.route) {
                    HistoryScreen.Content()
                }
                composable(MainBottomItem.Settings.route) {
                    SettingsScreen.Content()
                }
            }
        }
    )
}

@Composable
fun HomeBottomNavigation(navController: NavHostController, visibilityState: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = visibilityState.value,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            MainBottomItem.listOfTabItems().forEach { item ->
                BottomNavigationItem(
                    selected = currentRoute == item.route,
                    label = { Text(text = item.label) },
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route)
                        }
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(imageVector = item.icon, contentDescription = null) }
                )
            }
        }
    }
}

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object CreateProfile : Screen("createProfile")
    object Home : Screen("home")
    object History : Screen("history")
    object Settings : Screen("settings")
}

sealed class MainBottomItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : MainBottomItem(Screen.Home.route, "Home", Icons.Rounded.Home)
    object History : MainBottomItem(Screen.History.route, "History", Icons.Rounded.List)
    object Settings : MainBottomItem(Screen.Settings.route, "Settings", Icons.Rounded.Settings)

    companion object {
        fun listOfTabItems() = listOf(Home, History, Settings)
    }
}
