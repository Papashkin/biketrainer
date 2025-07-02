package com.antsfamily.biketrainer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed class Destination

@Serializable
data object Splash : Destination()

@Serializable
data object CreateProfile : Destination()

@Serializable
data object CreateWorkout : Destination()

@Serializable
data class WorkoutInfo(val id: Int) : Destination()

@Serializable
data object Home : Destination()

//@Serializable
//data object History : Destination()

@Serializable
data object Settings : Destination()

@Serializable
data object WorkoutNameDialog : Destination()


sealed class MainBottomDestination(
    val label: String,
    val route: Destination,
    val icon: ImageVector
) {

    val destination: String?
        get() = this.route::class.qualifiedName

    data object Home : MainBottomDestination(
        label = "Home",
        route = com.antsfamily.biketrainer.navigation.Home,
        icon = Icons.Rounded.Home
    )

    data object Settings : MainBottomDestination(
        label = "Settings",
        route = com.antsfamily.biketrainer.navigation.Settings,
        icon = Icons.Rounded.Settings
    )
    //TODO get History back when it's available.
//    data object History : MainBottomDestination(Screen.History.route, "History", Icons.Rounded.DateRange)

    companion object {
        fun listOfTabItems() = listOf(Home, Settings) // listOf(Home, History, Settings)
    }
}