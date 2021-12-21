package com.antsfamily.biketrainer.ui.workoutinfo

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentWorkoutInfoBinding
import com.antsfamily.biketrainer.presentation.EventObserver
import com.antsfamily.biketrainer.presentation.programinfo.WorkoutInfoViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.util.*
import com.antsfamily.biketrainer.ui.util.BarCharsStaticFields.BAR_WIDTH_95
import com.antsfamily.biketrainer.util.mapDistinct
import com.antsfamily.biketrainer.util.timeFormat
import com.antsfamily.data.model.program.ProgramData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutInfoFragment : BaseFragment(R.layout.fragment_workout_info) {

    private val args: WorkoutInfoFragmentArgs by navArgs()

    @Inject
    lateinit var barChartGestureListener: BarChartGestureListener

    @Inject
    lateinit var factory: WorkoutInfoViewModel.Factory

    override val viewModel: WorkoutInfoViewModel by viewModelsFactory {
        factory.build(args.programName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentWorkoutInfoBinding.bind(view)) {
            observeState(this)
            observeEvents(this)
            bindInteractions(this)
        }
    }

    private fun observeState(binding: FragmentWorkoutInfoBinding) {
        with(binding) {
            viewModel.mapDistinct { it.isLoading }.observe { loadingView.isVisible = it }
            viewModel.mapDistinct { it.program }.observe { setChart(it) }
            viewModel.mapDistinct { it.programName }.observe { workoutInfoNameTv.text = it }
            viewModel.mapDistinct { it.duration }.observe { workoutDurationTv.text = it }
            viewModel.mapDistinct { it.maxPower }.observe { workoutMaxPowerTv.text = it }
            viewModel.mapDistinct { it.avgPower }.observe { workoutAvgPowerTv.text = it }
        }
    }

    private fun observeEvents(binding: FragmentWorkoutInfoBinding) {
        with(binding) {
            viewModel.showRemoveWorkoutAnimationEvent.observe(viewLifecycleOwner, EventObserver {
                loadingPb.isVisible = false
                deleteWorkoutSuccessAnimation.apply {
                    isVisible = true
                    playAnimation()
                }
            })
        }
    }

    private fun bindInteractions(binding: FragmentWorkoutInfoBinding) {
        with(binding) {
            backBtn.setOnClickListener { viewModel.onBackClick() }
            editBtn.setOnClickListener { viewModel.onEditClick() }
            runWorkoutBtn.setOnClickListener { viewModel.onRunWorkoutClick() }
            deleteBtn.setOnClickListener { viewModel.onDeleteClick() }
            barChartGestureListener.setBarChart(workoutChart)
            deleteWorkoutSuccessAnimation.addAnimatorListener(object :
                Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    viewModel.onRemoveWorkoutAnimationEnd()
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationRepeat(animation: Animator?) {}
            })
        }
    }

    private fun FragmentWorkoutInfoBinding.setChart(programData: List<ProgramData>) {
        if (programData.isEmpty()) {
            return
        }
        val entries = programData.mapIndexed { index, _data ->
            BarEntry(index.toFloat(), _data.power.toFloat())
        }
        val labels = programData.map { it.duration.timeFormat() }
        barChartGestureListener.setLabels(labels)
        with(workoutChart) {
            setDefaultBaseSettings(entries.size)
            data = BarData(
                BarDataSet(entries, "")
                    .setDefaultSettings(this@with.getYAxisValueFormatter(entries.size))
            ).apply { barWidth = BAR_WIDTH_95 }
            xAxis.apply {
                isEnabled = entries.size <= 5
                setDefaultSettings(labels, this@with.getXAxisValueFormatter(labels))
            }
            onChartGestureListener = barChartGestureListener
            invalidate()
        }
    }
}
