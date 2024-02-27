package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.kiryuha21.jobsearchplatformclient.ui.contract.DialogContract
import kotlinx.coroutines.launch

class DialogViewModel: BaseViewModel<DialogContract.DialogIntent, DialogContract.DialogState>() {
    override fun initialState(): DialogContract.DialogState {
        return DialogContract.DialogState(
            isLoading = false,
            password = ""
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is DialogContract.DialogIntent.ApplyDialog -> checkPassword(intent.password)
        }
    }

    private fun checkPassword(password: String) {
        viewModelScope.launch {
            // TODO
        }
    }
}