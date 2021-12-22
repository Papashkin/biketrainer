package com.antsfamily.biketrainer.ui.createprofile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentCreateProfileBinding
import com.antsfamily.biketrainer.presentation.createprofile.CreateProfileViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.util.afterTextChange
import com.antsfamily.biketrainer.ui.util.hideKeyboard
import com.antsfamily.biketrainer.util.mapDistinct
import com.antsfamily.domain.antservice.orZero
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileFragment : BaseFragment(R.layout.fragment_create_profile) {

    @Inject
    lateinit var factory: CreateProfileViewModel.Factory

    override val viewModel: CreateProfileViewModel by viewModelsFactory { factory.build() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentCreateProfileBinding.bind(view)) {
            observeState(this)
            bindInteractions(this)
        }
    }

    private fun observeState(binding: FragmentCreateProfileBinding) {
        with(binding) {
            viewModel.mapDistinct { it.usernameError }.observe { usernameTil.error = it }
            viewModel.mapDistinct { it.ageError }.observe { ageTil.error = it }
            viewModel.mapDistinct { it.weightError }.observe { weightTil.error = it }
            viewModel.mapDistinct { it.heightError }.observe { heightTil.error = it }
            viewModel.mapDistinct { it.genderError }.observe { setupGenderError(it) }
            viewModel.mapDistinct { it.loadingState }.observe { loadingView.state = it }
        }
    }

    private fun bindInteractions(binding: FragmentCreateProfileBinding) {
        with(binding) {
            createBtn.setOnClickListener { createProfile() }
            usernameEt.afterTextChange { viewModel.onUsernameChange() }
            ageEt.afterTextChange { viewModel.onAgeChange() }
            weightEt.afterTextChange { viewModel.onWeightChange() }
            heightEt.afterTextChange { viewModel.onHeightChange() }
            genderGroup.apply {
                femaleRb.setOnClickListener { viewModel.onFemaleGenderSelected() }
                maleRb.setOnClickListener { viewModel.onMaleGenderSelected() }
            }
            loadingView.setOnAnimationFinishedListener { viewModel.onAnimationEnd() }
        }
    }

    private fun FragmentCreateProfileBinding.createProfile() {
        getFocus()?.hideKeyboard()
        viewModel.onCreateClick(
            usernameEt.text.toString(),
            ageEt.text.toString().toIntOrNull().orZero(),
            weightEt.text.toString().toBigDecimalOrNull(),
            heightEt.text.toString().toBigDecimalOrNull()
        )
    }

    private fun FragmentCreateProfileBinding.getFocus(): View? =
        root.findFocus() ?: root.focusedChild

    private fun FragmentCreateProfileBinding.setupGenderError(error: String?) {
        with(genderErrorTv) {
            isVisible = !error.isNullOrBlank()
            text = error
        }
    }
}
