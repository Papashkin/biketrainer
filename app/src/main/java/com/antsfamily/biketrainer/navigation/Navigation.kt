package com.antsfamily.biketrainer.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen
import com.antsfamily.biketrainer.ui.createworkout.CreateWorkoutScreen
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
        MainBottomDestination.Home.destination,
        MainBottomDestination.Settings.destination -> true
        else -> false
    }

    Scaffold(
        bottomBar = { HomeBottomNavigation(navController = navController, bottomBarState) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            print(it.toString())
            NavHost(
                navController = navController,
                startDestination = Splash
            ) {
                composable<Splash> { _ ->
                    SplashScreen.Content(
                        onNavigateToHome = {
                            navController.navigate(Home) { popUpToTop(navController) }
                        },
                        onNavigateToCreateProfile = {
                            navController.navigate(CreateProfile) { popUpToTop(navController) }
                        }
                    )
                }
                composable<CreateProfile> { _ ->
                    CreateProfileScreen.Content {
                        navController.navigate(MainBottomDestination.Home) { popUpToTop(navController) }
                    }
                }
                composable<Home> {
                    HomeScreen.Content(
                        navigateToEditWorkout = {
                            //TODO implement navigation to editWorkout
                        },
                        navigateToWorkoutInfo = { id ->
                            navController.navigate(WorkoutInfo(id))
                        },
                        navigateToCreateWorkout = { navController.navigate(CreateWorkout) }
                    )
                }
                composable<Settings> {
                    SettingsScreen.Content()
                }
                composable<CreateWorkout> {
                    CreateWorkoutScreen.Content(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable<WorkoutInfo> { entry ->
                    val data = entry.toRoute<WorkoutInfo>()
                    WorkoutInfoScreen(
                        snackbarHostState = snackbarHostState,
                        workoutId = data.id,
                        navigateBack = { navController.popBackStack() }
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
            MainBottomDestination.listOfTabItems().forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.destination,
                    label = { Text(text = item.label) },
                    onClick = {
                        if (currentRoute != item.destination) {
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
