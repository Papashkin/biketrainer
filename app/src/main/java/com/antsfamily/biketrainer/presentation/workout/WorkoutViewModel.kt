package com.antsfamily.biketrainer.presentation.workout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.presentation.Event
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.data.model.WorkoutState
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.domain.Result
import com.antsfamily.domain.antservice.device.*
import com.antsfamily.domain.antservice.orZero
import com.antsfamily.domain.usecase.workout.GetWorkoutUseCase
import com.antsfamily.domain.usecase.WorkoutTimerFlow
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType
import com.dsi.ant.plugins.antplus.pcc.defines.RequestStatus
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch.MultiDeviceSearchResult
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class WorkoutViewModel @AssistedInject constructor(
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val heartRateDevice: HeartRateDevice,
    private val cadenceDevice: BikeCadenceDevice,
    private val powerDevice: BikePowerDevice,
    private val speedDistanceDevice: BikeSpeedDistanceDevice,
    private val fitnessEquipmentDevice: FitnessEquipmentDevice,
    private val workoutTimerFlow: WorkoutTimerFlow
) : StatefulViewModel<WorkoutViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(): WorkoutViewModel
    }

    data class State(
        val isLoading: Boolean = true,
        val title: String? = null,
        val allRounds: Int? = null,
        val currentRound: Int = 0,
        val startButtonVisible: Boolean = true,
        val pauseButtonVisible: Boolean = false,
        val continueButtonVisible: Boolean = false,
        val stopButtonVisible: Boolean = false,
        val stepRemainingTime: Long = 0L,
        val progress: Int = 100,
        val currentStep: ProgramData? = null,
        val nextStep: ProgramData? = null,
        val heartRate: Int? = null,
        val cadence: Int? = null,
        val speed: BigDecimal? = null,
        val distance: BigDecimal? = null,
        val power: Int? = null,
        val program: List<ProgramData> = emptyList(),
        val workoutRemainingTime: Long = 0L,
        val workoutPassedTime: Long = 0L,
    )

    private val _resetChartHighlightsEvent = MutableLiveData<Event<Unit>>()
    val resetChartHighlightsEvent: LiveData<Event<Unit>>
        get() = _resetChartHighlightsEvent

    private var workoutState: WorkoutState = WorkoutState.READY
    private var isTargetPowerSetSuccessfully: Boolean = false
    private var currentStepNumber: Int = 0

    fun onStop() {
        closeAccessToSensors()
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onStartClick() = viewModelScope.launch {
        workoutState = WorkoutState.IN_PROGRESS
        changeState {
            it.copy(
                startButtonVisible = false,
                continueButtonVisible = false,
                pauseButtonVisible = true,
                stopButtonVisible = true
            )
        }
    }

    fun onPauseClick() {
        workoutState = WorkoutState.PAUSE
        setPausePowerToDevice()
        changeState {
            it.copy(
                startButtonVisible = false,
                continueButtonVisible = true,
                pauseButtonVisible = false,
                stopButtonVisible = true
            )
        }
    }

    fun onContinueClick() {
        workoutState = WorkoutState.IN_PROGRESS
        setTargetPowerToDevice()
        changeState {
            it.copy(
                startButtonVisible = false,
                continueButtonVisible = false,
                pauseButtonVisible = true,
                stopButtonVisible = true
            )
        }
    }

    fun onStopClick() {
        workoutState = WorkoutState.STOP
        currentStepNumber = 0
        resetWorkoutChartHighlights()
        resetWorkoutFields()
        changeState {
            it.copy(
                startButtonVisible = true,
                continueButtonVisible = false,
                pauseButtonVisible = false,
                stopButtonVisible = false
            )
        }
    }

    fun onCreate(devices: List<MultiDeviceSearchResult>, programName: String) {
        getWorkoutUseCase(programName) {
            handleProgramResult(it, devices)
        }
    }

    private fun handleProgramResult(
        result: Result<Program, Error>, devices: List<MultiDeviceSearchResult>
    ) {
        when (result) {
            is Result.Success -> handleProgramSuccessResult(result.successData, devices)
            is Result.Failure -> showErrorSnackbar("Something went wrong :( \nPlease try again")
        }
    }

    private fun handleProgramSuccessResult(
        program: Program,
        devices: List<MultiDeviceSearchResult>
    ) {
        setDevices(devices)
        changeState { state ->
            state.copy(
                title = program.title,
                allRounds = program.data.size,
                currentRound = currentStepNumber,
                nextStep = program.data[currentStepNumber],
                stepRemainingTime = program.data[currentStepNumber].duration,
                progress = 100,
                startButtonVisible = true,
                program = program.data,
                workoutRemainingTime = program.data.sumOf { it.duration },
                workoutPassedTime = 0L,
            )
        }
    }

    private fun setDevices(devices: List<MultiDeviceSearchResult>) {
        devices.forEach { device ->
            when (device.antDeviceType) {
                DeviceType.HEARTRATE -> subscribeToHeartRate()
                DeviceType.BIKE_CADENCE -> subscribeToBikeCadence()
                DeviceType.BIKE_SPD -> subscribeToBikeSpeed()
                DeviceType.BIKE_SPDCAD -> subscribeToBikeSpeed()
                DeviceType.BIKE_POWER -> subscribeToBikePower()
                DeviceType.FITNESS_EQUIPMENT -> subscribeToFitnessEquipment()
                else -> {
                    // no-op
                }
            }
        }
        changeState { it.copy(isLoading = false) }
        startWorkoutTimerFlow()
    }

    private fun startWorkoutTimerFlow() = viewModelScope.launch {
        workoutTimerFlow(PERIOD).collect {
            showDataFromSensors()
            if (workoutState == WorkoutState.IN_PROGRESS) {
                setTargetPowerToDevice()
                updateView()
            }
        }
    }

    private fun subscribeToBikeCadence() = viewModelScope.launch {
        cadenceDevice.subscribe()
    }

    private fun subscribeToHeartRate() = viewModelScope.launch {
        heartRateDevice.subscribe()
    }

    private fun subscribeToBikeSpeed() = viewModelScope.launch {
        speedDistanceDevice.subscribe()
    }

    private fun subscribeToBikePower() = viewModelScope.launch {
        powerDevice.subscribe()
    }

    private fun subscribeToFitnessEquipment() = viewModelScope.launch {
        fitnessEquipmentDevice.subscribe(::showErrorSnackbar)
    }

    private fun closeAccessToSensors() {
        heartRateDevice.clear()
        cadenceDevice.clear()
        speedDistanceDevice.clear(true)
        powerDevice.clear()
        fitnessEquipmentDevice.clear()
    }

    private fun showDataFromSensors() {
        changeState {
            it.copy(
                heartRate = getHeartRateValue(),
                cadence = getCadenceValue(),
                power = getPowerValue(),
                speed = getSpeedValue(),
                distance = getDistanceValue()
            )
        }
    }

    private fun setTargetPowerToDevice() {
        if (!isTargetPowerSetSuccessfully) {
            state.value?.currentStep?.let { data ->
                fitnessEquipmentDevice.setTargetPower(
                    data.power.toBigDecimal(),
                    {
                        Log.d(this::class.java.simpleName, "Set target power with result: $it")
                        isTargetPowerSetSuccessfully = it == RequestStatus.SUCCESS
                    }, {
                        isTargetPowerSetSuccessfully = it
                    }
                )
            }
        }
    }

    private fun setPausePowerToDevice() {
        fitnessEquipmentDevice.setTargetPower(
            BigDecimal(100),
            {
                Log.d(this::class.java.simpleName, "Set paused power (100 W)")
                isTargetPowerSetSuccessfully = it == RequestStatus.SUCCESS
            }, {
                isTargetPowerSetSuccessfully = it
            }
        )
    }

    private fun updateView() {
        val remainingTime = state.value?.stepRemainingTime.orZero().minus(1)
        when {
            (remainingTime >= ONE) -> updateView(remainingTime)
            else -> {
                currentStepNumber = currentStepNumber.inc()
                if (currentStepNumber < state.value?.program?.size.orZero()) {
                    val updatedRemainingTime = state.value?.program
                        ?.getOrNull(currentStepNumber)?.duration.orZero()
                    isTargetPowerSetSuccessfully = false
                    updateView(updatedRemainingTime)
                } else {
                    resetWorkoutChartHighlights()
                    workoutState = WorkoutState.READY
                    currentStepNumber = 0
                    resetWorkoutFields()
                    showSuccessSnackbar("Your workout is finished! Well done!")
                }
            }
        }
    }


    private fun updateView(stepRemainingTime: Long) {
        changeState {
            it.copy(
                currentRound = currentStepNumber + 1,
                currentStep = it.program.getOrNull(currentStepNumber),
                nextStep = it.program.getOrNull(currentStepNumber + 1),
                progress = stepRemainingTime.times(HUNDRED)
                    .div(it.program.getOrNull(currentStepNumber)?.duration ?: 1).toInt(),
                stepRemainingTime = stepRemainingTime,
                workoutRemainingTime = it.workoutRemainingTime.minus(1),
                workoutPassedTime = it.workoutPassedTime.plus(1),
            )
        }
    }

    private fun resetWorkoutFields() {
        changeState { state ->
            state.copy(
                currentRound = currentStepNumber,
                currentStep = null,
                nextStep = state.program.getOrNull(currentStepNumber),
                stepRemainingTime = state.program.getOrNull(currentStepNumber)?.duration.orZero(),
                progress = 100,
                startButtonVisible = true,
                pauseButtonVisible = false,
                stopButtonVisible = false,
                program = state.program,
                workoutRemainingTime = state.program.sumOf { it.duration },
                workoutPassedTime = 0L,
            )
        }
    }

    private fun getHeartRateValue() = heartRateDevice.heartRate

    private fun getCadenceValue() = (cadenceDevice.cadence ?: fitnessEquipmentDevice.cadence)
        ?.setScale(2, RoundingMode.HALF_DOWN)?.toInt()

    private fun getSpeedValue() = (speedDistanceDevice.speed ?: fitnessEquipmentDevice.speed)
        ?.setScale(1, RoundingMode.HALF_DOWN)

    private fun getDistanceValue() = when (workoutState) {
        WorkoutState.IN_PROGRESS,
        WorkoutState.PAUSE -> (speedDistanceDevice.distance ?: fitnessEquipmentDevice.distance)
            ?.divide(METERS_IN_KILOMETER, 2, RoundingMode.HALF_DOWN)
        WorkoutState.READY,
        WorkoutState.STOP -> BigDecimal.ZERO
    }

    private fun getPowerValue() = (powerDevice.power ?: fitnessEquipmentDevice.power)
        ?.setScale(2, RoundingMode.HALF_DOWN)?.toInt()

    private fun resetWorkoutChartHighlights() {
        _resetChartHighlightsEvent.postValue(Event(Unit))
    }

    companion object {
        private const val ONE = 1L
        private const val PERIOD = 1000L
        private const val HUNDRED = 100
        private val METERS_IN_KILOMETER = BigDecimal(1000)
    }
}
