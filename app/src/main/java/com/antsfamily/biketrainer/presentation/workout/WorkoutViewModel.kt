package com.antsfamily.biketrainer.presentation.workout

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.presentation.Event
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.biketrainer.util.AppContentResolver
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.domain.Result
import com.antsfamily.domain.antservice.device.*
import com.antsfamily.domain.antservice.orZero
import com.antsfamily.domain.usecase.WorkoutTimerFlow
import com.antsfamily.domain.usecase.workout.GetWorkoutUseCase
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceType
import com.dsi.ant.plugins.antplus.pcc.defines.RequestStatus
import com.dsi.ant.plugins.antplus.pccbase.MultiDeviceSearch.MultiDeviceSearchResult
import com.garmin.fit.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.ZoneOffset


class WorkoutViewModel @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val heartRateDevice: HeartRateDevice,
    private val cadenceDevice: BikeCadenceDevice,
    private val powerDevice: BikePowerDevice,
    private val speedDistanceDevice: BikeSpeedDistanceDevice,
    private val fitnessEquipmentDevice: FitnessEquipmentDevice,
    private val workoutTimerFlow: WorkoutTimerFlow,
    @Assisted private val devices: List<MultiDeviceSearchResult>,
    @Assisted private val programName: String,
    private val contentResolver: AppContentResolver
) : StatefulViewModel<WorkoutViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(devices: List<MultiDeviceSearchResult>, programName: String): WorkoutViewModel
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
        val remainingTime: Long = 0L,
        val progress: Int = 100,
        val currentStep: ProgramData? = null,
        val nextStep: ProgramData? = null,
        val heartRate: Int? = null,
        val cadence: Int? = null,
        val speed: BigDecimal? = null,
        val distance: BigDecimal? = null,
        val power: Int? = null,
        val program: List<ProgramData> = emptyList()
    )

    private val _resetChartHighlightsEvent = MutableLiveData<Event<Unit>>()
    val resetChartHighlightsEvent: LiveData<Event<Unit>>
        get() = _resetChartHighlightsEvent

    private val _createDocumentEvent = MutableLiveData<Event<String>>()
    val createDocumentEvent: LiveData<Event<String>>
        get() = _createDocumentEvent

    private var isWorkoutStarted: Boolean = false
    private var isWorkoutPaused: Boolean = false
    private var isTargetPowerSetSuccessfully: Boolean = false
    private var currentStepNumber: Int = 0

    //    private var programName: String? = null
//    private var activityFile: File? = null
    private val workoutMessage: MutableList<Mesg> = mutableListOf()

    init {
        getWorkout()
    }

    fun onStop() {
        closeAccessToSensors()
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onStartClick() = viewModelScope.launch {
        onWorkoutStart()
        changeState {
            it.copy(
                isLoading = false,
                startButtonVisible = false,
                continueButtonVisible = false,
                pauseButtonVisible = true,
                stopButtonVisible = true
            )
        }
    }

    fun onPauseClick() {
        isWorkoutPaused = true
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
        isWorkoutPaused = false
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
        changeState {
            it.copy(
                isLoading = false,
                startButtonVisible = true,
                continueButtonVisible = false,
                pauseButtonVisible = false,
                stopButtonVisible = false
            )
        }
        onWorkoutFinish()
    }

    fun onFileUriReceived(uri: Uri) {
        try {
//            uri.path?.let { path ->
//                val file = uri.toFile()
//                val encoder = FileEncoder(file, Fit.ProtocolVersion.getHighestVersion())
//                encoder.write(workoutMessage)
//                encoder.close()

            var fileName: String?
            val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
            val isFile = DocumentsContract.isDocumentUri(context, uri)
            val documentFile: DocumentFile = DocumentFile.fromFile(java.io.File(uri.path.orEmpty()))
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor?.close()

            contentResolver.query(uri, projection, null, null, null).use {
                val columnIndex =
                    it?.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA).orZero()
                it?.moveToFirst()
                fileName = it?.getString(columnIndex).orEmpty()
            }
            if (fileName != null) {
                val encoder = FileEncoder(
                    java.io.File(fileName.orEmpty()),
                    Fit.ProtocolVersion.getHighestVersion()
                )
                encoder.write(workoutMessage)
                encoder.close()
            }
//            }
        } catch (e: Exception) {
            Log.e(this::class.java.canonicalName, "Error occurred: $e")
            showErrorSnackbar("Something went wrong :(")
//        } finally {
//            hideLoading()
        }
    }

    private fun getWorkout() {
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
                remainingTime = program.data[currentStepNumber].duration,
                progress = 100,
                startButtonVisible = true,
                program = program.data
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
        hideLoading()
        startWorkoutTimerFlow()
    }

    private fun startWorkoutTimerFlow() = viewModelScope.launch {
        workoutTimerFlow(PERIOD).collect {
            showDataFromSensors()
            if (isWorkoutStarted && !isWorkoutPaused) {
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

    private fun updateView() {
        val remainingTime = state.value?.remainingTime.orZero().minus(1)
        when {
            (remainingTime > ZERO) -> updateView(remainingTime)
            (remainingTime == ZERO) -> {
                currentStepNumber = currentStepNumber.inc()
                if (currentStepNumber < state.value?.program?.size.orZero()) {
                    val updatedRemainingTime = state.value?.program
                        ?.getOrNull(currentStepNumber)?.duration.orZero()
                    isTargetPowerSetSuccessfully = false
                    updateView(updatedRemainingTime)
                    createLapMessage(getTimestamp())
                } else {
                    showSuccessSnackbar("Your workout is finished! Well done!")
                    onWorkoutFinish()
                }
            }
        }
    }

    private fun updateView(remainingTime: Long) {
        changeState { state ->
            state.copy(
                currentRound = currentStepNumber + 1,
                currentStep = state.program.getOrNull(currentStepNumber),
                nextStep = state.program.getOrNull(currentStepNumber + 1),
                progress = remainingTime.times(HUNDRED)
                    .div(state.program.getOrNull(currentStepNumber)?.duration ?: 1).toInt(),
                remainingTime = remainingTime
            )
        }
        createRecordMessage()
    }

    private fun resetWorkoutFields() {
        changeState {
            it.copy(
                currentRound = currentStepNumber,
                currentStep = null,
                nextStep = it.program.getOrNull(currentStepNumber),
                remainingTime = it.program.getOrNull(currentStepNumber)?.duration.orZero(),
                progress = 100,
                startButtonVisible = true,
                pauseButtonVisible = false,
                stopButtonVisible = false,
                program = it.program
            )
        }
    }

    private fun getHeartRateValue() = heartRateDevice.heartRate.orZero()

    private fun getCadenceValue() = (cadenceDevice.cadence ?: fitnessEquipmentDevice.cadence)
        ?.setScale(2, RoundingMode.HALF_DOWN).orZero().toInt()

    private fun getSpeedValue() =
        (speedDistanceDevice.speed ?: fitnessEquipmentDevice.speed)
            ?.setScale(1, RoundingMode.HALF_DOWN).orZero()

    private fun getDistanceValue() =
        (speedDistanceDevice.distance ?: fitnessEquipmentDevice.distance)
            ?.setScale(1, RoundingMode.HALF_DOWN).orZero().divide(METERS_IN_KILOMETER)

    private fun getPowerValue() = (powerDevice.power ?: fitnessEquipmentDevice.power)
        ?.setScale(2, RoundingMode.HALF_DOWN).orZero().toInt()

    private fun showLoadingAndCreateFitFile() {
        showLoading()
        createFitFile()
    }

    private fun onWorkoutStart() {
        isWorkoutStarted = true
        workoutMessage.apply {
            add(
                FileIdMesg().apply {
                    type = File.ACTIVITY
                    manufacturer = Manufacturer.DEVELOPMENT
                    timeCreated = DateTime(getTimestamp())
                }
            )
            add(
                DeviceInfoMesg().apply {
                    manufacturer = Manufacturer.DEVELOPMENT
                    this.timestamp = DateTime(this@WorkoutViewModel.getTimestamp())
                }
            )
        }
        createLapMessage(getTimestamp())
    }

    private fun onWorkoutFinish() {
        isWorkoutStarted = false
        currentStepNumber = 0
        resetWorkoutChartHighlights()
        resetWorkoutFields()
        createSessionMessage()
        createActivityMessage()
        showLoadingAndCreateFitFile()
    }

    private fun createFitFile() {
//        val folder =
//            Environment.getExternalStorageDirectory().path.toString() + "/biketrainer_app_activities"
//        Log.d(this::class.java.canonicalName, "Path : $folder")
//        val file = java.io.File(folder)
//        if (!file.exists()) {
//            if (!file.mkdir()) {
//                Log.d(this::class.java.canonicalName, "***Problem creating Image folder $folder")
//            }
//        }
        _createDocumentEvent.postValue(Event("${programName}_${getTimestamp()}.fit"))
    }

    private fun createRecordMessage() {
        workoutMessage.add(
            RecordMesg().apply {
                this.timestamp = DateTime(this@WorkoutViewModel.getTimestamp())
                heartRate = getHeartRateValue().toShort()
                power = getPowerValue()
                distance = getDistanceValue().toFloat()
                cadence = getCadenceValue().toShort()
                speed = getSpeedValue().toFloat()
            }
        )
    }

    private fun createLapMessage(time: Long) {
        workoutMessage.add(
            LapMesg().apply {
                this.timestamp = DateTime(this@WorkoutViewModel.getTimestamp())
                startTime = DateTime(time)
                totalElapsedTime =
                    state.value?.program?.getOrNull(currentStepNumber)?.duration.orZero().toFloat()
                totalTimerTime =
                    state.value?.program?.getOrNull(currentStepNumber)?.duration.orZero().toFloat()
            }
        )
    }

    private fun createSessionMessage() {
        workoutMessage.add(
            SessionMesg().apply {
                this.timestamp = DateTime(this@WorkoutViewModel.getTimestamp())
                startTime = DateTime(
                    this@WorkoutViewModel.getTimestamp()
                        .minus(state.value?.program?.sumOf { it.duration }?.times(1000).orZero())
                )
                totalElapsedTime = state.value?.program?.sumOf { it.duration }.orZero().toFloat()
                totalTimerTime = state.value?.program?.sumOf { it.duration }.orZero().toFloat()
                totalDistance = getDistanceValue().toFloat()
            }
        )
    }

    private fun createActivityMessage() {
        workoutMessage.add(
            ActivityMesg().apply {
                this.timestamp = DateTime(this@WorkoutViewModel.getTimestamp())
                totalTimerTime = state.value?.program?.sumOf { it.duration }.orZero().toFloat()
                numSessions = 1
                eventType = EventType.STOP
                event = com.garmin.fit.Event.ACTIVITY
            }
        )
    }

    private fun getTimestamp() = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

    private fun showLoading() {
        changeState { it.copy(isLoading = true) }
    }

    private fun hideLoading() {
        changeState { it.copy(isLoading = false) }
    }

    private fun resetWorkoutChartHighlights() {
        _resetChartHighlightsEvent.postValue(Event(Unit))
    }

    companion object {
        private const val ZERO = 0L
        private const val PERIOD = 1000L
        private const val HUNDRED = 100
        private val METERS_IN_KILOMETER = BigDecimal(1000)
    }
}
