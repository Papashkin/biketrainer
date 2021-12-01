package com.antsfamily.biketrainer.navigation

import androidx.navigation.NavDirections
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileFragmentDirections
import com.antsfamily.biketrainer.ui.createprogram.CreateProgramFragmentDirections
import com.antsfamily.biketrainer.ui.scanning.ScanFragmentDirections
import com.antsfamily.biketrainer.ui.splash.SplashFragmentDirections
import com.antsfamily.biketrainer.ui.home.HomeFragmentDirections
import com.antsfamily.biketrainer.ui.programinfo.ProgramInfoFragmentDirections

fun Route.mapToDirection(): NavDirections = when (this) {
    is SplashToHome -> SplashFragmentDirections.actionStartFragmentToStartFragment(
        profileName
    )
    is SplashToCreateProfile -> SplashFragmentDirections.actionStartFragmentToCreateProfileFragment()
    is CreateProfileToHome -> CreateProfileFragmentDirections.actionCreateProfileFragmentToStartFragment(
        profileName
    )
    is HomeToProfile -> HomeFragmentDirections.actionStartFragmentToProfilesFragment()
    is HomeToCreateProgram -> HomeFragmentDirections.actionHomeFragmentToCreateProgramFragment()
    is HomeToProgramInfo -> HomeFragmentDirections.actionHomeFragmentToProgramInfoFragment(
        programName
    )
    is ProgramInfoToScan -> ProgramInfoFragmentDirections.actionProgramInfoFragmentToScanFragment(
        programName
    )
    is ScanToWorkout -> ScanFragmentDirections.actionScanFragmentToWorkoutFragment(
        devices.toTypedArray(), program
    )
    is CreateProgramToAddSegment -> CreateProgramFragmentDirections.actionCreateProgramFragmentToAddSegmentFragment()
    is CreateProgramToAddInterval -> CreateProgramFragmentDirections.actionCreateProgramFragmentToAddIntervalFragment()
    is CreateProgramToAddStairs -> CreateProgramFragmentDirections.actionCreateProgramFragmentToAddStairsFragment()
}
