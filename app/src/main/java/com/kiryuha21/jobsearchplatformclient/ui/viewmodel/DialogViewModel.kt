package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.kiryuha21.jobsearchplatformclient.ui.contract.DialogContract
import kotlinx.coroutines.launch

class DialogViewModel: BaseViewModel<DialogContract.DialogIntent, DialogContract.DialogState>() {
    override fun initialState(): DialogContract.DialogState {
        return DialogContract.DialogState(
            isLoading = false,
            passwordState = mutableStateOf("")
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is DialogContract.DialogIntent.ApplyDialog -> checkPassword(intent.password)
        }
    }

    private fun checkPassword(password: String) {
        _viewState.value = _viewState.value.copy(
            isLoading = true
        )

        viewModelScope.launch {
            // TODO
        }

        _viewState.value = _viewState.value.copy(
            isLoading = false
        )
    }
}