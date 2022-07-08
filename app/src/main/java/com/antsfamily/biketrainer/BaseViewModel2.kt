package com.antsfamily.biketrainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.navigation.MainBottomItem
import com.antsfamily.biketrainer.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel2: ViewModel() {

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()

    private val _navigateBackEvent = MutableSharedFlow<Unit>()
    val navigateBackEvent: SharedFlow<Unit> = _navigateBackEvent.asSharedFlow()

    protected fun navigateTo(screen: Screen) = viewModelScope.launch {
        _navigationFlow.emit(screen.route)
    }

    protected fun navigateTo(screenItem: MainBottomItem) = viewModelScope.launch {
        _navigationFlow.emit(screenItem.route)
    }

    protected fun navigateBack() = viewModelScope.launch {
        _navigateBackEvent.emit(Unit)
    }
}
