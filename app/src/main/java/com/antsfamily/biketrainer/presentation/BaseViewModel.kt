package com.antsfamily.biketrainer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.navigation.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _singleEvent = Channel<SingleEvent>()
    val singleEvent = _singleEvent.receiveAsFlow()

    fun navigateTo(route: Route) = viewModelScope.launch {
        _singleEvent.send(SingleEvent.NavigationEvent(route))
    }

    fun navigateBack() = viewModelScope.launch {
        _singleEvent.send(SingleEvent.NavigationBackEvent)
    }

    fun showErrorSnackbar(message: String?) = viewModelScope.launch {
        _singleEvent.send(SingleEvent.ErrorMessageEvent(message.orEmpty()))
    }

    fun showErrorSnackbar(messageId: Int) = viewModelScope.launch {
        _singleEvent.send(SingleEvent.ErrorMessageIdEvent(messageId))
    }

    fun showSuccessSnackbar(message: String) = viewModelScope.launch {
        _singleEvent.send(SingleEvent.SuccessMessageEvent(message))
    }
}
