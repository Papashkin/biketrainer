package com.antsfamily.biketrainer.navigation

import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch

sealed class Route
class SplashToHome(val profileName: String) : Route()
object SplashToCreateProfile : Route()
class CreateProfileToHome(val profileName: String) : Route()
object HomeToProfile : Route()
object HomeToCreateProgram : Route()
class HomeToProgramInfo(val programName: String) : Route()
class ProgramInfoToScan(val programName: String) : Route()

class ScanToWorkout(
    val devices: List<MultiDeviceSearch.MultiDeviceSearchResult>,
    val program: String
) : Route()

object CreateProgramToAddSegment : Route()
object CreateProgramToAddInterval : Route()
object CreateProgramToAddStairs : Route()
