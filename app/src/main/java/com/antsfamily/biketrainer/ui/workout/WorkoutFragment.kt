package com.antsfamily.biketrainer.ui.workout

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.antsfamily.biketrainer.R
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.biketrainer.databinding.FragmentWorkoutBinding
import com.antsfamily.biketrainer.presentation.EventObserver
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.presentation.workout.WorkoutViewModel
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.util.hideAllLabels
import com.antsfamily.biketrainer.ui.util.setHighlightedMode
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.biketrainer.util.mapDistinct
import com.antsfamily.biketrainer.util.toStringOrEmpty
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutFragment : BaseFragment(R.layout.fragment_workout) {

    private val args: WorkoutFragmentArgs by navArgs()

    @Inject
    lateinit var factory: WorkoutViewModel.Factory

    override val viewModel: WorkoutViewModel by viewModelsFactory {
        factory.build(args.devices.toList(), args.program)
    }

    private val chartHighlights = mutableListOf<Highlight>()

    private val documentCreationResultLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument()) { uri: Uri? ->
            uri?.let { viewModel.onFileUriReceived(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentWorkoutBinding.bind(view)) {
            observeState(this)
            observeEvents(this)
            bindInteractions(this)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    private fun bindInteractions(binding: FragmentWorkoutBinding) {
        with(binding) {
            backBtn.setOnClickListener { viewModel.onBackClick() }
            startWorkoutBtn.setOnClickListener { viewModel.onStartClick() }
            pauseWorkoutBtn.setOnClickListener { viewModel.onPauseClick() }
            continueWorkoutBtn.setOnClickListener { viewModel.onContinueClick() }
            stopWorkoutBtn.setOnClickListener { viewModel.onStopClick() }
        }
    }

    private fun observeEvents(binding: FragmentWorkoutBinding) {
        viewModel.resetChartHighlightsEvent.observe(viewLifecycleOwner, EventObserver {
            chartHighlights.clear()
            binding.programChart.highlightValues(arrayOf())
        })
        viewModel.createDocumentEvent.observe(viewLifecycleOwner, EventObserver {
            openFileFolder(it)
        })
    }

    private fun observeState(binding: FragmentWorkoutBinding) {
        with(binding) {
            viewModel.mapDistinct { it.isLoading }.observe { loadingView.isVisible = it }
            viewModel.mapDistinct { it.title }.observe { titleTv.text = it }
            viewModel.mapDistinct { it.allRounds }.observe {
                workoutAllStepsTv.text = it.toStringOrEmpty(EMPTY_DATA)
            }
            viewModel.mapDistinct { it.currentRound }.observe {
                workoutCurrentStepTv.text = it.toString()
                highlightAppropriateBar(it)
            }
            viewModel.mapDistinct { it.currentStep }.observe { data ->
                workoutTargetPowerTv.text =
                    data?.let { getString(R.string.workout_power, it.power.toString()) }
                        ?: EMPTY_DATA
            }
            viewModel.mapDistinct { it.nextStep }.observe { setNextStep(it) }
            viewModel.mapDistinct { it.startButtonVisible }.observe { startWorkoutBtn.isVisible = it }
            viewModel.mapDistinct { it.pauseButtonVisible }.observe { pauseWorkoutBtn.isVisible = it }
            viewModel.mapDistinct { it.stopButtonVisible }.observe { stopWorkoutBtn.isVisible = it }
            viewModel.mapDistinct { it.continueButtonVisible }.observe { continueWorkoutBtn.isVisible = it }
            viewModel.mapDistinct { it.progress }.observe { stepCountdownRb.progress = it }
            viewModel.mapDistinct { it.remainingTime }.observe { setRemainingTime(it) }
            viewModel.mapDistinct { it.heartRate }.observe {
                workoutHeartRateTv.text =
                    getString(R.string.workout_heart_rate, it.toStringOrEmpty(EMPTY_DATA))
            }
            viewModel.mapDistinct { it.cadence }.observe {
                workoutCadenceTv.text =
                    getString(R.string.workout_cadence, it.toStringOrEmpty(EMPTY_DATA))
            }
            viewModel.mapDistinct { it.distance }.observe {
                workoutDistanceTv.text =
                    getString(R.string.workout_distance, it.toStringOrEmpty(EMPTY_DATA))
            }
            viewModel.mapDistinct { it.speed }.observe {
                workoutSpeedTv.text =
                    getString(R.string.workout_speed, it.toStringOrEmpty(EMPTY_DATA))
            }
            viewModel.mapDistinct { it.power }.observe { power ->
                workoutUserPowerTv.text =
                    power?.let { getString(R.string.workout_power, it.toString()) } ?: EMPTY_DATA
            }
            viewModel.mapDistinct { it.program }.observe { setProgramBarChart(it) }
        }
    }

    private fun FragmentWorkoutBinding.highlightAppropriateBar(columnNumber: Int) {
        val index = columnNumber.dec()
        if (index < 0) return
        chartHighlights.add(Highlight(index.toFloat(), 0, 0))
        programChart.highlightValues(chartHighlights.toTypedArray())
    }

    private fun FragmentWorkoutBinding.setRemainingTime(remainingTime: Long) {
        workoutRemainingTimeTv.text = remainingTime.fullTimeFormat()
    }

    private fun FragmentWorkoutBinding.setNextStep(data: ProgramData?) {
        workoutNextStepValueTv.text = data?.let {
            getString(R.string.workout_next_round_value, it.power, it.duration.fullTimeFormat())
        } ?: EMPTY_DATA
    }

    private fun FragmentWorkoutBinding.setProgramBarChart(data: List<ProgramData>?) {
        val entities = data?.mapIndexed { index, _data ->
            BarEntry(index.toFloat(), _data.power.toFloat())
        }
        entities?.let {
            with(this.programChart) {
                setScaleEnabled(false)
                setTouchEnabled(true)
                hideAllLabels()
                setDrawGridBackground(false)
                setDrawBorders(false)
                this.data = BarData(
                    BarDataSet(it, EMPTY_LABEL).apply {
                        barBorderWidth = BAR_BORDER_WIDTH
                        valueFormatter = object : ValueFormatter() {
                            override fun getBarLabel(entry: BarEntry): String = EMPTY_LABEL
                        }
                        setHighlightedMode(false)
                        setColors(intArrayOf(R.color.color_cooler), requireContext())
                        highLightColor = resources.getColor(R.color.color_central, null)
                        stackLabels = emptyArray()
                    }
                ).apply {
                    barWidth = BAR_WIDTH
                }
                invalidate()
            }
        }
    }

    private fun openFileFolder(fileName: String) {
        documentCreationResultLauncher.launch(fileName)
    }

    companion object {
        private const val EMPTY_DATA = "--"
        private const val EMPTY_LABEL = ""
        private const val BAR_BORDER_WIDTH = 0f
        private const val BAR_WIDTH = 0.95f
    }
}
