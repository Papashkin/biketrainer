package com.antsfamily.biketrainer.ui.createworkout

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentAddStairsBinding
import com.antsfamily.biketrainer.presentation.EventObserver
import com.antsfamily.biketrainer.presentation.createworkout.AddStairsViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.util.afterTextChange
import com.antsfamily.biketrainer.util.mapDistinct
import com.antsfamily.domain.antservice.orZero
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddStairsFragment : BaseFragment(R.layout.fragment_add_stairs) {

    @Inject
    lateinit var factory: AddStairsViewModel.Factory

    override val viewModel: AddStairsViewModel by viewModelsFactory { factory.build() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentAddStairsBinding.bind(view)) {
            observeState()
            observeEvents()
            bindInteractions()
        }
    }

    private fun FragmentAddStairsBinding.observeState() {
        viewModel.mapDistinct { it.startPowerError }.observe { startPowerTil.error = it }
        viewModel.mapDistinct { it.endPowerError }.observe { endPowerTil.error = it }
        viewModel.mapDistinct { it.durationError }.observe { durationView.error = it }
        viewModel.mapDistinct { it.stepCountError }.observe { stepCountTil.error = it }
    }

    private fun observeEvents() {
        viewModel.setStairsResult.observe(viewLifecycleOwner, EventObserver {
            setFragmentResult(RQ_KEY_ADD_STAIRS, bundleOf(KEY_ADD_STAIRS to it))
        })
    }

    private fun FragmentAddStairsBinding.bindInteractions() {
        addBtn.setOnClickListener { proceedAddStairs() }
        startPowerEt.afterTextChange { viewModel.onStartPowerTextChange() }
        endPowerEt.afterTextChange { viewModel.onEndPowerTextChange() }
        stepCountEt.afterTextChange { viewModel.onStepCountChange() }
        backBtn.setOnClickListener { viewModel.onBackClick() }
        durationView.apply {
            setOnCodeChangedListener { viewModel.onDurationChange() }
            setOnDoneActionListener { proceedAddStairs() }
        }
    }

    private fun FragmentAddStairsBinding.proceedAddStairs() {
        viewModel.onAddClick(
            startPower = startPowerEt.text.toString().toIntOrNull().orZero(),
            endPower = endPowerEt.text.toString().toIntOrNull().orZero(),
            stepCount = stepCountEt.text.toString().toIntOrNull().orZero(),
            duration = durationView.getDurationValue()
        )
    }

    companion object {
        const val RQ_KEY_ADD_STAIRS = "request_key_add_stairs"
        const val KEY_ADD_STAIRS = "key_add_stairs"
    }
}
