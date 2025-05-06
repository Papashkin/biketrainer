package com.antsfamily.biketrainer.ui.profiles

//@AndroidEntryPoint
//class ProfilesFragment : BaseFragment(R.layout.fragment_profiles) {
//
//    @Inject
//    lateinit var factory: ProfilesViewModel.Factory
//
//    @Inject
//    lateinit var profilesAdapter: ProfilesAdapter
//
//    override val viewModel by viewModelsFactory { factory.build() }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View = FragmentProfilesBinding.inflate(inflater, container, false).root
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        with(FragmentProfilesBinding.bind(view)) {
//            observeState(this)
//            observeEvents()
//            bindInteractions(this)
//        }
//    }
//
//    private fun observeState(binding: FragmentProfilesBinding) {
//        with(binding) {
//            viewModel.mapDistinct { it.isLoading }.observe { loadingView.isVisible = it }
//            viewModel.mapDistinct { it.isProfilesVisible }.observe { profilesRv.isVisible = it }
//            viewModel.mapDistinct { it.isEmptyProfileVisible }
//                .observe { emptyListProfiles.isVisible = it }
//            viewModel.mapDistinct { it.profiles }.observe { profilesAdapter.submitList(it) }
//        }
//    }
//
//    private fun bindInteractions(binding: FragmentProfilesBinding) {
//        with(binding) {
//            backBtn.setOnClickListener { viewModel.onBackButtonClick() }
//            addProfileBtn.setOnClickListener { viewModel.addNewProfileClick() }
//            profilesRv.adapter = profilesAdapter.apply {
//                // TODO add listener
//            }
//        }
//    }
//
//    private fun observeEvents() {
//        // TODO add events (click on profile; create profile; ...)
//    }
//}
