package com.antsfamily.biketrainer.ui.createworkout

import android.os.Bundle
import android.view.View
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentCreateWorkoutBinding
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.createworkout.AddIntervalsFragment.Companion.KEY_ADD_INTERVAL
import com.antsfamily.biketrainer.ui.createworkout.AddIntervalsFragment.Companion.RQ_KEY_ADD_INTERVAL
import com.antsfamily.biketrainer.ui.createworkout.AddSegmentFragment.Companion.KEY_ADD_SEGMENT
import com.antsfamily.biketrainer.ui.createworkout.AddSegmentFragment.Companion.RQ_KEY_ADD_SEGMENT
import com.antsfamily.biketrainer.ui.createworkout.AddStairsFragment.Companion.KEY_ADD_STAIRS
import com.antsfamily.biketrainer.ui.createworkout.AddStairsFragment.Companion.RQ_KEY_ADD_STAIRS
import com.antsfamily.biketrainer.ui.util.afterTextChange
import com.antsfamily.biketrainer.util.mapDistinct
import com.antsfamily.data.model.workouts.WorkoutIntervalParams
import com.antsfamily.data.model.workouts.WorkoutSegmentParams
import com.antsfamily.data.model.workouts.WorkoutStairsParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateWorkoutFragment : BaseFragment(R.layout.fragment_create_workout) {

    @Inject
    lateinit var factory: CreateWorkoutViewModel.Factory

    override val viewModel: CreateWorkoutViewModel by viewModelsFactory { factory.build() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentCreateWorkoutBinding.bind(view)) {
            observeState()
            observeEvents()
            bindInteractions()
        }
        setupFragmentResultListener()
    }

    private fun FragmentCreateWorkoutBinding.observeState() {
        viewModel.mapDistinct { it.loadingState }.observe { loadingView.state = it }
        viewModel.mapDistinct { it.programNameError }.observe { programNameTil.error = it }
        viewModel.mapDistinct { it.workoutData }.observe { workoutChart.item = it }
        viewModel.mapDistinct { it.isEmptyBarChartVisible }
            .observe { workoutChart.isEmptyDataVisible = it }
        viewModel.mapDistinct { it.isBarChartVisible }
            .observe { workoutChart.isChartVisible = it }
        viewModel.mapDistinct { it.workoutError }.observe { workoutChart.error = it }
    }

    private fun FragmentCreateWorkoutBinding.observeEvents() {
        viewModel.clearInputFieldsEvent.observeEvent { programNameEt.text = null }
    }

    private fun FragmentCreateWorkoutBinding.bindInteractions() {
        backBtn.setOnClickListener { viewModel.onBackClick() }
        programNameEt.afterTextChange { viewModel.onProgramNameChange() }
        addIntervalsBtn.setOnClickListener { viewModel.onIntervalsClick() }
        addSegmentBtn.setOnClickListener { viewModel.onSegmentClick() }
        addStairsBtn.setOnClickListener { viewModel.onStairsClick() }
        createBtn.setOnClickListener { viewModel.onCreateClick(programNameEt.text.toString()) }
        loadingView.setOnAnimationFinishedListener {
            viewModel.onWorkoutSuccessAnimationEnd()
        }
    }

    private fun setupFragmentResultListener() {
        with(parentFragmentManager) {
            setFragmentResultListener(RQ_KEY_ADD_SEGMENT, viewLifecycleOwner) { _, bundle ->
                viewModel.onSegmentAdd(bundle[KEY_ADD_SEGMENT] as? WorkoutSegmentParams)
            }
            setFragmentResultListener(RQ_KEY_ADD_INTERVAL, viewLifecycleOwner) { _, bundle ->
                viewModel.onIntervalAdd(bundle[KEY_ADD_INTERVAL] as? WorkoutIntervalParams)
            }
            setFragmentResultListener(RQ_KEY_ADD_STAIRS, viewLifecycleOwner) { _, bundle ->
                viewModel.onStairsAdd(bundle[KEY_ADD_STAIRS] as? WorkoutStairsParams)
            }
        }
    }
}
