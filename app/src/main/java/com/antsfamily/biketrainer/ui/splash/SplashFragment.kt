package com.antsfamily.biketrainer.ui.splash

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentSplashBinding
import com.antsfamily.biketrainer.presentation.splash.SplashViewModel
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.util.viewModelsFactory
import com.antsfamily.biketrainer.util.mapDistinct
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    @Inject lateinit var factory: SplashViewModel.Factory

    override val viewModel: SplashViewModel by viewModelsFactory { factory.build() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentSplashBinding.bind(view)) {
            observeState(this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun observeState(binding: FragmentSplashBinding) {
        with(binding) {
            viewModel.state.mapDistinct { it.isLoading }
                .observe(viewLifecycleOwner) { loadingPb.isVisible = it }
        }
    }
}
