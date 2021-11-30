package com.antsfamily.biketrainer

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.antsfamily.biketrainer.navigation.Route
import com.antsfamily.biketrainer.navigation.mapToDirection
import com.antsfamily.biketrainer.presentation.BaseViewModel
import com.antsfamily.biketrainer.presentation.SingleEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigationEvents()
    }

    private fun observeNavigationEvents() = lifecycleScope.launchWhenStarted {
        viewModel.singleEvent.collect { event -> handleEvent(event) }
    }

    private fun handleEvent(event: SingleEvent) {
        when (event) {
            is SingleEvent.NavigationEvent -> navigateTo(event.route)
            else -> {
                /* no-op */
            }
        }
    }
    private fun navigateTo(route: Route) {
        NavHostFragment.findNavController(this).navigate(route.mapToDirection())
    }
}
