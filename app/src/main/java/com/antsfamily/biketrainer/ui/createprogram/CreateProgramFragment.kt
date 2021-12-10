package com.antsfamily.biketrainer.ui.createprogram

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.data.models.workouts.WorkoutIntervalParams
import com.antsfamily.biketrainer.data.models.workouts.WorkoutSegmentParams
import com.antsfamily.biketrainer.data.models.workouts.WorkoutStairsParams
import com.antsfamily.biketrainer.databinding.FragmentCreateProgramBinding
import com.antsfamily.biketrainer.presentation.EventObserver
import com.antsfamily.biketrainer.presentation.createprogram.CreateProgramViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.createprogram.AddIntervalsFragment.Companion.KEY_ADD_INTERVAL
import com.antsfamily.biketrainer.ui.createprogram.AddIntervalsFragment.Companion.RQ_KEY_ADD_INTERVAL
import com.antsfamily.biketrainer.ui.createprogram.AddSegmentFragment.Companion.KEY_ADD_SEGMENT
import com.antsfamily.biketrainer.ui.createprogram.AddSegmentFragment.Companion.RQ_KEY_ADD_SEGMENT
import com.antsfamily.biketrainer.ui.createprogram.AddStairsFragment.Companion.KEY_ADD_STAIRS
import com.antsfamily.biketrainer.ui.createprogram.AddStairsFragment.Companion.RQ_KEY_ADD_STAIRS
import com.antsfamily.biketrainer.ui.util.afterTextChange
import com.antsfamily.biketrainer.util.mapDistinct
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateProgramFragment : BaseFragment(R.layout.fragment_create_program) {

    @Inject
    lateinit var factory: CreateProgramViewModel.Factory

    override val viewModel: CreateProgramViewModel by viewModelsFactory { factory.build() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentCreateProgramBinding.bind(view)) {
            observeState()
            observeEvents()
            bindInteractions()
        }
        setupFragmentResultListener()
    }

    private fun FragmentCreateProgramBinding.observeState() {
        viewModel.mapDistinct { it.isLoading }.observe { loadingView.isVisible = it }
        viewModel.mapDistinct { it.programNameError }.observe { programNameTil.error = it }
        viewModel.mapDistinct { it.barItem }.observe { workoutChart.item = it }
        viewModel.mapDistinct { it.isEmptyBarChartVisible }
            .observe { workoutChart.isEmptyDataVisible = it }
        viewModel.mapDistinct { it.isBarChartVisible }
            .observe { workoutChart.isBarChartVisible = it }
        viewModel.mapDistinct { it.workoutError }.observe { workoutChart.error = it }
    }

    private fun FragmentCreateProgramBinding.observeEvents() {
        viewModel.clearInputFieldsEvent.observe(viewLifecycleOwner, EventObserver {
            programNameEt.text = null
        })
    }

    private fun FragmentCreateProgramBinding.bindInteractions() {
        backBtn.setOnClickListener { viewModel.onBackClick() }
        programNameEt.afterTextChange { viewModel.onProgramNameChange() }
        addIntervalsBtn.setOnClickListener { viewModel.onIntervalsClick() }
        addSegmentBtn.setOnClickListener { viewModel.onSegmentClick() }
        addStairsBtn.setOnClickListener { viewModel.onStairsClick() }
        createBtn.setOnClickListener { viewModel.onCreateClick(programNameEt.text.toString()) }
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
