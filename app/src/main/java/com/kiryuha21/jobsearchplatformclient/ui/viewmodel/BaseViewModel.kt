package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

interface ViewState
interface ViewIntent

abstract class BaseViewModel<UserIntent: ViewIntent, UiState: ViewState> : ViewModel() {
    protected abstract fun initialState(): UiState
    abstract fun processIntent(intent: ViewIntent)

    private val initialState: UiState by lazy { initialState() }
    private val _viewState: MutableState<UiState> = mutableStateOf(initialState)
    val viewState by _viewState

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.reducer()
        _viewState.value = newState
    }
}