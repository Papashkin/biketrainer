package com.antsfamily.biketrainer.ui.createprogram

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentAddSegmentBinding
import com.antsfamily.biketrainer.presentation.EventObserver
import com.antsfamily.biketrainer.presentation.createprogram.AddSegmentViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.util.afterTextChange
import com.antsfamily.biketrainer.util.mapDistinct
import com.antsfamily.biketrainer.util.orZero
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddSegmentFragment : BaseFragment(R.layout.fragment_add_segment) {

    @Inject
    lateinit var factory: AddSegmentViewModel.Factory

    override val viewModel: AddSegmentViewModel by viewModelsFactory {
        factory.build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentAddSegmentBinding.bind(view)) {
            observeState()
            observeEvents()
            bindInteractions()
        }
    }

    private fun FragmentAddSegmentBinding.observeState() {
        viewModel.mapDistinct { it.powerError }.observe { powerTil.error = it }
        viewModel.mapDistinct { it.durationError }.observe { durationView.error = it }
    }

    private fun observeEvents() {
        viewModel.setSegmentResult.observe(viewLifecycleOwner, EventObserver {
            setFragmentResult(RQ_KEY_ADD_SEGMENT, bundleOf(KEY_ADD_SEGMENT to it))
        })
    }

    private fun FragmentAddSegmentBinding.bindInteractions() {
        addBtn.setOnClickListener {
            viewModel.onAddClick(
                powerEt.text.toString().toIntOrNull().orZero(),
                durationView.getValue()
            )
        }
        durationView.setOnDurationChangeListener { viewModel.onDurationChange() }
        powerEt.afterTextChange { viewModel.onPowerTextChange() }
        backBtn.setOnClickListener { viewModel.onBackClick() }
    }

    companion object {
        const val RQ_KEY_ADD_SEGMENT = "request_key_add_segment"
        const val KEY_ADD_SEGMENT = "key_add_segment"
    }
}
