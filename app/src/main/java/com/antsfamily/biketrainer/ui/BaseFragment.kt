package com.antsfamily.biketrainer.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.antsfamily.biketrainer.navigation.Route
import com.antsfamily.biketrainer.navigation.mapToDirection
import com.antsfamily.biketrainer.presentation.BaseViewModel
import com.antsfamily.biketrainer.presentation.Event
import com.antsfamily.biketrainer.presentation.EventObserver
import com.antsfamily.biketrainer.presentation.SingleEvent
import com.antsfamily.biketrainer.ui.util.addDismissListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    abstract val viewModel: BaseViewModel

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvents()
    }

    protected fun showSnackBar(
        @StringRes messageRes: Int,
        @StringRes actionLabelRes: Int? = null,
        actionClickListener: ((View) -> Unit)? = null,
        dismissListener: (() -> Unit)? = null,
        indefinite: Boolean = false,
    ) {
        showSnackBar(
            getString(messageRes),
            actionLabelRes,
            actionClickListener,
            dismissListener,
            indefinite
        )
    }

    private fun showSnackBar(
        message: String,
        @StringRes actionLabelRes: Int? = null,
        actionClickListener: ((View) -> Unit)? = null,
        dismissListener: (() -> Unit)? = null,
        inDefinite: Boolean = false,
    ) {
        val duration = if (inDefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
        snackbar = Snackbar.make(requireView(), message, duration)
            .apply {
                actionLabelRes?.let { setAction(it, actionClickListener) }
                dismissListener?.let { addDismissListener(it) }
            }
            .also { it.show() }
    }

    protected fun <X> LiveData<X>.observe(observer: Observer<X>) =
        observe(viewLifecycleOwner, observer)

    protected fun <T> LiveData<Event<T>>.observeEvent(onEventUnhandledContent: (T) -> Unit) =
        observe(EventObserver(onEventUnhandledContent))

    fun hideSnackBar() {
        snackbar?.dismiss()
    }

    private fun observeEvents() = lifecycleScope.launchWhenStarted {
        viewModel.singleEvent.collect {
            when (it) {
                is SingleEvent.NavigationEvent -> navigateTo(it.route)
                SingleEvent.NavigationBackEvent -> navigateBack()
                is SingleEvent.ErrorMessageEvent -> showSnackBar(it.message)
                is SingleEvent.SuccessMessageEvent -> showSnackBar(it.message)
                is SingleEvent.ErrorMessageIdEvent -> showSnackBar(getString(it.id))
            }
        }
    }

    private fun navigateTo(route: Route) {
        getNavController()?.navigate(route.mapToDirection())
    }

    private fun navigateBack() {
        getNavController()?.navigateUp()
    }

    private fun getNavController(): NavController? {
        val controller = findNavController()
        when (val destination = controller.currentDestination) {
            is FragmentNavigator.Destination -> {
                if (destination.className == this::class.java.name) {
                    return controller
                }
            }
            // in case of navigating from one BottomSheetDialogFragment to another
            is DialogFragmentNavigator.Destination -> {
                if (destination.className == this::class.java.name) {
                    return controller
                }
            }
        }
        return null
    }
}
