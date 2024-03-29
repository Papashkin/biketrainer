package com.antsfamily.biketrainer.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.FragmentHomeBinding
import com.antsfamily.biketrainer.presentation.home.HomeViewModel
import com.antsfamily.biketrainer.presentation.viewModelsFactory
import com.antsfamily.biketrainer.ui.BaseFragment
import com.antsfamily.biketrainer.ui.home.adapter.CreateWorkoutAdapter
import com.antsfamily.biketrainer.ui.home.adapter.WorkoutsAdapter
import com.antsfamily.biketrainer.ui.util.iconId
import com.antsfamily.biketrainer.util.mapDistinct
import com.antsfamily.data.model.profile.Profile
import com.garmin.fit.Gender
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val args: HomeFragmentArgs by navArgs()

    @Inject
    lateinit var factory: HomeViewModel.Factory

    @Inject
    lateinit var workoutsAdapter: WorkoutsAdapter

    @Inject
    lateinit var createWorkoutAdapter: CreateWorkoutAdapter

    override val viewModel by viewModelsFactory { factory.build(args.profileName) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentHomeBinding.bind(view)) {
            observeState(this)
            bindInteractions(this)
        }
    }

    private fun observeState(binding: FragmentHomeBinding) {
        with(binding) {
            viewModel.mapDistinct { it.dateTime }.observe { homeDateTimeTv.text = it }
            viewModel.mapDistinct { it.appVersion }.observe { ver ->
                ver?.let { appVersionTv.text = getString(R.string.home_app_version, it) }
            }
            viewModel.mapDistinct { it.isProgramsLoading }
                .observe { homeProgramsLoadingFl.isVisible = it }
            viewModel.mapDistinct { it.isProgramsVisible }.observe { homeWorkoutsRv.isVisible = it }
            viewModel.mapDistinct { it.isEmptyProgramsVisible }
                .observe { emptyProgramsCl.isVisible = it }
            viewModel.mapDistinct { it.profile }.observe { setupProfile(it) }
            viewModel.mapDistinct { it.programs }.observe {
                workoutsAdapter.submitList(it)
                Handler(Looper.getMainLooper()).postDelayed({
                    addWorkoutBtn.isVisible = it.isNotEmpty()
                }, 400)
            }
        }
    }

    private fun FragmentHomeBinding.setupProfile(profile: Profile?) {
        profile?.let {
            homeProfileNameTv.text = it.name
            homeProfileIconIv.setImageResource(Gender.valueOf(it.gender).iconId())
        }
    }

    private fun bindInteractions(binding: FragmentHomeBinding) {
        with(binding) {
            createWorkoutBtn.setOnClickListener { viewModel.onCreateProgramClick() }
            addWorkoutBtn.setOnClickListener { viewModel.onCreateProgramClick() }
            settingsIb.setOnClickListener { viewModel.onSettingsClick() }
            createWorkoutAdapter.apply {
                setOnCreateProgramClickListener { viewModel.onCreateProgramClick() }
            }
            homeWorkoutsRv.adapter = workoutsAdapter.apply {
                setOnItemClickListener { viewModel.onProgramClick(it) }
            }
        }
    }
}
