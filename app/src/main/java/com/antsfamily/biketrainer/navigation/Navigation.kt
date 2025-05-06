package com.antsfamily.biketrainer.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen
import com.antsfamily.biketrainer.ui.createworkout.CreateWorkoutScreen
import com.antsfamily.biketrainer.ui.history.HistoryScreen
import com.antsfamily.biketrainer.ui.home.HomeScreen
import com.antsfamily.biketrainer.ui.settings.SettingsScreen
import com.antsfamily.biketrainer.ui.splash.SplashScreen
import com.antsfamily.biketrainer.ui.workoutinfo.WorkoutInfoScreen

@Composable
fun Navigation() {
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    bottomBarState.value = when (currentRoute) {
        MainBottomItem.Home.route, MainBottomItem.History.route, MainBottomItem.Settings.route -> true
        else -> false
    }

    Scaffold(
        bottomBar = { HomeBottomNavigation(navController = navController, bottomBarState) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            print(it.toString())
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen.Content(
                        onNavigateToHome = {
                            navController.navigate(Screen.Home.route) { popUpToTop(navController) }
                        },
                        onNavigateToCreateProfile = {
                            navController.navigate(Screen.CreateProfile.route) { popUpToTop(navController) }
                        }
                    )
                }
                composable(Screen.CreateProfile.route) {
                    CreateProfileScreen.Content { route ->
                        navController.navigate(route) { popUpToTop(navController) }
                    }
                }
                composable(MainBottomItem.Home.route) {
                    HomeScreen.Content { route ->
                        navController.navigate(route)
                    }
                }
                composable(MainBottomItem.History.route) {
                    HistoryScreen.Content()
                }
                composable(MainBottomItem.Settings.route) {
                    SettingsScreen.Content()
                }
                composable(Screen.CreateWorkout.route) {
                    CreateWorkoutScreen.Content {
                        navController.popBackStack()
                    }
                }
                composable(
                    Screen.WorkoutInfo.route,
                    arguments = listOf(navArgument("workoutName") { type = NavType.StringType })
                ) {
                    WorkoutInfoScreen(
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        workoutName = it.arguments?.getString("workoutName").orEmpty()
                    )
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
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            MainBottomItem.listOfTabItems().forEach { item ->
                NavigationBarItem(
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
    data object Splash : Screen("splash")
    data object CreateProfile : Screen("create_profile")
    data object CreateWorkout : Screen("create_workout")
    data object WorkoutInfo : Screen("workout_info/{workoutName}")
    data object Home : Screen("home")
    data object History : Screen("history")
    data object Settings : Screen("settings")
}

sealed class MainBottomItem(val route: String, val label: String, val icon: ImageVector) {
    data object Home : MainBottomItem(Screen.Home.route, "Home", Icons.Rounded.Home)
    data object History : MainBottomItem(Screen.History.route, "History", Icons.Rounded.DateRange)
    data object Settings : MainBottomItem(Screen.Settings.route, "Settings", Icons.Rounded.Settings)

    companion object {
        fun listOfTabItems() = listOf(Home, History, Settings)
    }
}
