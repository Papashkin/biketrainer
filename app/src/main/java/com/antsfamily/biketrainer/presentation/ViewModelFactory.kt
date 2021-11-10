package com.antsfamily.biketrainer.presentation

//@Singleton
//@Suppress("UNCHECKED_CAST")
//class ViewModelFactory<T: ViewModel>(
//    owner: SavedStateRegistryOwner,
//    private val create: (handle: SavedStateHandle) -> T
//): AbstractSavedStateViewModelFactory(owner, null) {
//
//    override fun <T : ViewModel?> create(
//        key: String,
//        modelClass: Class<T>,
//        handle: SavedStateHandle
//    ): T {
//        return create.invoke(handle) as T
//    }
//
//    fun create(owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null) =
//        object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
//            override fun <T : ViewModel?> create(
//                key: String,
//                modelClass: Class<T>,
//                handle: SavedStateHandle
//            ): T {
//                val viewModel = createInjectViewModel(modelClass)
//                        ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")
//
//                try {
//                    return viewModel as T
//                } catch (e: Exception) {
//                    throw RuntimeException(e)
//                }
//            }
//        }
//
//    private fun <T : ViewModel?> createInjectViewModel(modelClass: Class<T>): ViewModel? {
//        val creator = creators[modelClass]
//            ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
//            ?: return null
//
//        return creator.get()
//    }
//}
//
//@MainThread
//fun SavedStateRegistryOwner.withFactory(factory: ViewModelFactory, defaultArgs: Bundle? = null) =
//    factory.create(this, defaultArgs)

interface ViewModelFactory
