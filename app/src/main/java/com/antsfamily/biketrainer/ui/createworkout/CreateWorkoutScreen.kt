package com.antsfamily.biketrainer.ui.createworkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.core.view.TopBar
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutUiState
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutViewModel
import com.antsfamily.biketrainer.ui.common.FullScreenLoading
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.createworkout.model.Duration
import com.antsfamily.biketrainer.ui.createworkout.model.IndexedWorkoutStep
import com.antsfamily.biketrainer.ui.createworkout.model.WorkoutStep
import com.antsfamily.biketrainer.ui.createworkout.model.WorkoutType
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutCoolDownStepDialog
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutIntervalsStepDialog
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutNameDialog
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutOneStepDialog
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutStepCard
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutTypeSwitcher
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutWarmUpStepDialog
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.util.fullTimeFormat

interface CreateWorkoutScreen {
    companion object {
        @Composable
        fun Content(onNavigateBack: () -> Unit) {
            CreateWorkoutScreen(onNavigateBack)
        }
    }
}

@Composable
fun CreateWorkoutScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateWorkoutViewModel = hiltViewModel(),
) {
    val (oneStepWorkout, setOneStepWorkout) = remember {
        mutableStateOf<IndexedWorkoutStep?>(null)
    }
    val (isOneStepWorkoutVisible, setIsOneStepDialogVisible) = remember {
        mutableStateOf(false)
    }

    val (warmUpWorkout, setWarmUpWorkout) = remember {
        mutableStateOf<IndexedWorkoutStep?>(null)
    }
    val (isWarmUpStepWorkoutVisible, setIsWarmUpStepDialogVisible) = remember {
        mutableStateOf(false)
    }

    val (intervalsWorkout, setIntervalsWorkout) = remember {
        mutableStateOf<IndexedWorkoutStep?>(null)
    }
    val (isIntervalsWorkoutVisible, setIsIntervalsWorkoutVisible) = remember {
        mutableStateOf(false)
    }

    val (coolDownWorkout, setCoolDownWorkout) = remember {
        mutableStateOf<IndexedWorkoutStep?>(null)
    }
    val (isCoolDownWorkoutVisible, setIsCoolDownWorkoutVisible) = remember {
        mutableStateOf(false)
    }

    val (workoutName, setWorkoutName) = remember { mutableStateOf<String?>(null) }

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateBackEvent.collect {
            onNavigateBack()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.setWarmUpDialogVisibilityEvent.collect {
            setIsWarmUpStepDialogVisible(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.editWarmUpDialogEvent.collect {
            setWarmUpWorkout(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.showWorkoutNameDialogEvent.collect {
            setWorkoutName(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.setOneStepDialogVisibilityEvent.collect {
            setIsOneStepDialogVisible(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.editOneStepDialogEvent.collect {
            setOneStepWorkout(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.setCoolDownDialogVisibilityEvent.collect {
            setIsCoolDownWorkoutVisible(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.editCoolDownDialogEvent.collect {
            setCoolDownWorkout(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.setIntervalDialogVisibilityEvent.collect {
            setIsIntervalsWorkoutVisible(it)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.editIntervalDialogEvent.collect {
            setIntervalsWorkout(it)
        }
    }

    when (val stateValue = state.value) {
        is CreateWorkoutUiState.Loading -> FullScreenLoading()
        is CreateWorkoutUiState.Content -> CreateWorkoutContent(
            content = stateValue,
            onNavigationBackClick = { onNavigateBack() },
            onWorkoutChipClicked = { viewModel.onWorkoutChipClick(it) },
            onTitleClick = { viewModel.onTitleClick(it) },
            onWorkoutEditClick = { viewModel.onWorkoutEditClick(it) },
            onWorkoutDeleteClick = { viewModel.onWorkoutStepDelete(it) }
        )
    }

    if (isOneStepWorkoutVisible) {
        WorkoutOneStepDialog(
            step = null,
            onDismiss = { setIsOneStepDialogVisible(false) },
            onConfirmClick = { viewModel.onOneStepAdd(it) }
        )
    }
    if (isWarmUpStepWorkoutVisible) {
        WorkoutWarmUpStepDialog(
            step = null,
            onDismiss = { setIsWarmUpStepDialogVisible(false) },
            onConfirmClick = { viewModel.onWarmUpStepAdd(it) }
        )
    }
    if (isIntervalsWorkoutVisible) {
        WorkoutIntervalsStepDialog(
            step = null,
            onDismiss = { setIsIntervalsWorkoutVisible(false) },
            onCloseDialogWithData = { viewModel.onIntervalsAdd(it) }
        )
    }
    if (isCoolDownWorkoutVisible) {
        WorkoutCoolDownStepDialog(
            step = null,
            onDismiss = { setIsCoolDownWorkoutVisible(false) },
            onConfirmClick = { viewModel.onCoolDownStepAdd(it) }
        )
    }

    oneStepWorkout?.let {
        WorkoutOneStepDialog(
            step = it.step as? WorkoutStep.OneStep,
            onDismiss = { setOneStepWorkout(null) },
            onConfirmClick = { step -> viewModel.onOneStepChange(it.index, step) }
        )
    }
    warmUpWorkout?.let {
        WorkoutWarmUpStepDialog(
            step = it.step as? WorkoutStep.WarmUp,
            onDismiss = { setWarmUpWorkout(null) },
            onConfirmClick = { step -> viewModel.onWarmUpStepChange(it.index, step) }
        )
    }
    intervalsWorkout?.let {
        WorkoutIntervalsStepDialog(
            step = it.step as? WorkoutStep.Intervals,
            onDismiss = { setIntervalsWorkout(null) },
            onCloseDialogWithData = { step -> viewModel.onIntervalsChange(it.index, step) }
        )
    }
    coolDownWorkout?.let {
        WorkoutCoolDownStepDialog(
            step = it.step as? WorkoutStep.CoolDown,
            onDismiss = { setCoolDownWorkout(null) },
            onConfirmClick = { step -> viewModel.onCoolDownStepChange(it.index, step) }
        )
    }

    workoutName?.let { name ->
        WorkoutNameDialog(
            currentName = name,
            onDismiss = { setWorkoutName(null) },
            onNameChanged = { viewModel.onTitleChange(it) }
        )
    }
}

@Composable
fun CreateWorkoutContent(
    content: CreateWorkoutUiState.Content,
    onNavigationBackClick: () -> Unit,
    onTitleClick: (String) -> Unit,
    onWorkoutChipClicked: (WorkoutType) -> Unit,
    onWorkoutEditClick: (IndexedWorkoutStep) -> Unit,
    onWorkoutDeleteClick: (IndexedWorkoutStep) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
            .navigationBarsPadding()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            TopBar(
                onNavigationBack = { onNavigationBackClick() }
            )
            Column(modifier = Modifier.padding(horizontal = Padding.medium)) {
                Text(
                    text = content.name ?: stringResource(R.string.compose_create_workout_name),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Padding.small)
                        .clickable { onTitleClick(content.name.orEmpty()) }
                )
                Text(
                    text = "Click on title to change workout name",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.create_workout_description),
                    modifier = Modifier.padding(top = Padding.xx_large),
                    style = MaterialTheme.typography.labelMedium,
                )
                WorkoutTypeSwitcher(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Padding.small)
                ) {
                    onWorkoutChipClicked(it)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Padding.medium),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.create_workout_your_workout),
                    modifier = Modifier
                        .padding(vertical = Padding.regular)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = stringResource(
                        R.string.create_workout_total_duration,
                        content.totalDuration.toLong().fullTimeFormat()
                    ),
                    modifier = Modifier.padding(vertical = Padding.regular),
                    style = MaterialTheme.typography.bodyMedium,
                )

            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(content.steps) { step ->
                    WorkoutStepCard(
                        step = step.step,
                        onDeleteClick = { onWorkoutDeleteClick(step) },
                        onEditClick = { onWorkoutEditClick(step) }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(80.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            LoadingButton(
                modifier = Modifier.padding(Padding.medium),
                onClick = { },
                loading = content.isSafeWorkoutLoadingVisible,
                enabled = content.isSafeWorkoutButtonEnable,
            ) {
                Text(text = stringResource(id = R.string.compose_create_workout_save))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateWorkoutContentPreview() {
    CreateWorkoutContent(
        content = CreateWorkoutUiState.Content(
            name = "Test",
            steps = listOf(
                IndexedWorkoutStep(
                    1, WorkoutStep.OneStep(200, Duration(12, 35))
                ),
                IndexedWorkoutStep(
                    2, WorkoutStep.Intervals(1200, Duration(1, 0), 300, Duration(1, 40), 6)
                ),
                IndexedWorkoutStep(
                    3, WorkoutStep.WarmUp(100, 240, Duration(3, 55))
                ),
                IndexedWorkoutStep(
                    4, WorkoutStep.CoolDown(400, 130, Duration(1, 30))
                )
            ),
            totalDuration = 1703,
            isSafeWorkoutButtonEnable = false,
            isSafeWorkoutLoadingVisible = false,
        ),
        {},
        {},
        {},
        {},
        {}
    )
}