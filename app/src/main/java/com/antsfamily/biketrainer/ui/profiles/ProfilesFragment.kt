package com.antsfamily.biketrainer.ui.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentProfilesBinding
import com.antsfamily.biketrainer.presentation.profiles.ProfilesViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.profiles.adapter.ProfilesAdapter
import com.antsfamily.biketrainer.util.mapDistinct
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfilesFragment : BaseFragment(R.layout.fragment_profiles) {

    @Inject
    lateinit var factory: ProfilesViewModel.Factory

    @Inject
    lateinit var profilesAdapter: ProfilesAdapter

    override val viewModel by viewModelsFactory { factory.build() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentProfilesBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentProfilesBinding.bind(view)) {
            observeState(this)
            observeEvents()
            bindInteractions(this)
        }
    }

    private fun observeState(binding: FragmentProfilesBinding) {
        with(binding) {
            viewModel.mapDistinct { it.isLoading }.observe { loadingView.isVisible = it }
            viewModel.mapDistinct { it.isProfilesVisible }.observe { profilesRv.isVisible = it }
            viewModel.mapDistinct { it.isEmptyProfileVisible }
                .observe { emptyListProfiles.isVisible = it }
            viewModel.mapDistinct { it.profiles }.observe { profilesAdapter.submitList(it) }
        }
    }

    private fun bindInteractions(binding: FragmentProfilesBinding) {
        with(binding) {
            backBtn.setOnClickListener { viewModel.onBackButtonClick() }
            addProfileBtn.setOnClickListener { viewModel.addNewProfileClick() }
            profilesRv.adapter = profilesAdapter.apply {
                // TODO add listener
            }
        }
    }

    private fun observeEvents() {
        // TODO add events (click on profile; create profile; ...)
    }
}
