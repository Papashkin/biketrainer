package com.antsfamily.biketrainer.navigation

import androidx.navigation.NavDirections
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileFragmentDirections
import com.antsfamily.biketrainer.ui.createworkout.CreateWorkoutFragmentDirections
import com.antsfamily.biketrainer.ui.devicesearch.ScanFragmentDirections
import com.antsfamily.biketrainer.ui.home.HomeFragmentDirections
import com.antsfamily.biketrainer.ui.programinfo.ProgramInfoFragmentDirections
import com.antsfamily.biketrainer.ui.splash.SplashFragmentDirections

fun Route.mapToDirection(): NavDirections = when (this) {
    is SplashToHome -> SplashFragmentDirections.actionStartFragmentToStartFragment(
        profileName
    )
    is SplashToCreateProfile -> SplashFragmentDirections.actionStartFragmentToCreateProfileFragment()
    is CreateProfileToHome -> CreateProfileFragmentDirections.actionCreateProfileFragmentToStartFragment(
        profileName
    )
    is HomeToProfile -> HomeFragmentDirections.actionStartFragmentToProfilesFragment()
    is HomeToCreateProgram -> HomeFragmentDirections.actionHomeFragmentToCreateWorkoutFragment()
    is HomeToProgramInfo -> HomeFragmentDirections.actionHomeFragmentToProgramInfoFragment(
        programName
    )
    is ProgramInfoToScan -> ProgramInfoFragmentDirections.actionProgramInfoFragmentToScanFragment(
        programName
    )
    is ScanToWorkout -> ScanFragmentDirections.actionScanFragmentToWorkoutFragment(
        devices.toTypedArray(), program
    )
    is CreateProgramToAddSegment -> CreateWorkoutFragmentDirections.actionCreateProgramFragmentToAddSegmentFragment()
    is CreateProgramToAddInterval -> CreateWorkoutFragmentDirections.actionCreateProgramFragmentToAddIntervalFragment()
    is CreateProgramToAddStairs -> CreateWorkoutFragmentDirections.actionCreateProgramFragmentToAddStairsFragment()
}
