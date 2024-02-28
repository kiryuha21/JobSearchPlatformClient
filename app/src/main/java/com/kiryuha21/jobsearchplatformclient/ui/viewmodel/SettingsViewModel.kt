package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.kiryuha21.jobsearchplatformclient.ui.contract.SettingsContract
import kotlinx.coroutines.launch

class SettingsViewModel: BaseViewModel<SettingsContract.SettingsIntent, SettingsContract.SettingsState>() {
    override fun initialState(): SettingsContract.SettingsState {
        return SettingsContract.SettingsState(
            isLoading = false,
            password = ""
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is SettingsContract.SettingsIntent.ApplyDialog -> checkPassword(intent.password)
        }
    }

    private fun checkPassword(password: String) {
        viewModelScope.launch {
            // TODO
        }
    }
}