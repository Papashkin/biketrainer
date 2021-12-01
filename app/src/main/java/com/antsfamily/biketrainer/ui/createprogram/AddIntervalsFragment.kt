package com.antsfamily.biketrainer.ui.createprogram

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentAddIntervalsBinding
import com.antsfamily.biketrainer.presentation.EventObserver
import com.antsfamily.biketrainer.presentation.createprogram.AddIntervalsViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.util.afterTextChange
import com.antsfamily.biketrainer.util.mapDistinct
import com.antsfamily.biketrainer.util.orZero
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddIntervalsFragment : BaseFragment(R.layout.fragment_add_intervals) {

    @Inject
    lateinit var factory: AddIntervalsViewModel.Factory

    override val viewModel: AddIntervalsViewModel by viewModelsFactory {
        factory.build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentAddIntervalsBinding.bind(view)) {
            observeState()
            observeEvents()
            bindInteractions()
        }
    }

    private fun FragmentAddIntervalsBinding.observeState() {
        viewModel.mapDistinct { it.restPowerError }
            .observe(viewLifecycleOwner) { restPowerTil.error = it }
        viewModel.mapDistinct { it.peakPowerError }
            .observe(viewLifecycleOwner) { peakPowerTil.error = it }
        viewModel.mapDistinct { it.restDurationError }
            .observe(viewLifecycleOwner) { restPowerDurationView.error = it }
        viewModel.mapDistinct { it.peakDurationError }
            .observe(viewLifecycleOwner) { peakPowerDurationView.error = it }
        viewModel.mapDistinct { it.countError }
            .observe(viewLifecycleOwner) { timesTil.error = it }
    }

    private fun observeEvents() {
        viewModel.setIntervalResult.observe(viewLifecycleOwner, EventObserver {
            setFragmentResult(RQ_KEY_ADD_INTERVAL, bundleOf(KEY_ADD_INTERVAL to it))
        })
    }

    private fun FragmentAddIntervalsBinding.bindInteractions() {
        addBtn.setOnClickListener {
            viewModel.onAddClick(
                peakPower = peakPowerEt.text.toString().toIntOrNull().orZero(),
                restPower = restPowerEt.text.toString().toIntOrNull().orZero(),
                peakDuration = peakPowerDurationView.getValue(),
                restDuration = restPowerDurationView.getValue(),
                count = timesEt.text.toString().toIntOrNull().orZero()
            )
        }
        peakPowerDurationView.setOnDurationChangeListener { viewModel.onPeakDurationChange() }
        restPowerDurationView.setOnDurationChangeListener { viewModel.onRestDurationChange() }
        peakPowerEt.afterTextChange { viewModel.onPeakPowerTextChange() }
        restPowerEt.afterTextChange { viewModel.onRestPowerTextChange() }
        timesEt.afterTextChange { viewModel.onCountChange() }
        backBtn.setOnClickListener { viewModel.onBackClick() }
    }

    companion object {
        const val RQ_KEY_ADD_INTERVAL = "request_key_add_interval"
        const val KEY_ADD_INTERVAL = "key_add_interval"
    }
}

