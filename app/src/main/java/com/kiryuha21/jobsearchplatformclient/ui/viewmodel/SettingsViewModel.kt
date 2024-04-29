package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject.userRetrofit
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.ui.contract.SettingsContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SettingsViewModel: BaseViewModel<SettingsContract.Intent, SettingsContract.State>() {
    override fun initialState(): SettingsContract.State {
        return SettingsContract.State(
            isScreenLoading = false,
            isPasswordDialogShown = false,
            isDialogLoading = false,
            isAreYouSureDialogShown = false,
            areFieldsEnabled = false,
            isPasswordError = false,
            username = CurrentUser.info.username,
            email = CurrentUser.info.email,
            password = "",
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is SettingsContract.Intent.EditPassword -> setState { copy(password = intent.newPassword) }
            is SettingsContract.Intent.EditUsername -> setState { copy(username = intent.newUsername) }
            is SettingsContract.Intent.EditEmail -> setState { copy(email = intent.newEmail) }
            is SettingsContract.Intent.CheckPassword -> checkPassword(intent.password)
            is SettingsContract.Intent.CommitChanges -> commitChanges()
            is SettingsContract.Intent.ShowPasswordDialog -> setState { copy(isPasswordDialogShown = true) }
            is SettingsContract.Intent.HidePasswordDialog -> setState { copy(isPasswordDialogShown = false) }
            is SettingsContract.Intent.ShowAreYouSureDialog -> setState { copy(isAreYouSureDialogShown = true) }
            is SettingsContract.Intent.HideAreYouSureDialog -> setState { copy(isAreYouSureDialogShown = false) }
        }
    }

    private fun checkPassword(password: String) {
        viewModelScope.launch {
            setState { copy(isDialogLoading = true) }

            val validPassword = networkCallWrapper(
                networkCall = {
                    AuthToken.createToken(
                        username = CurrentUser.info.username,
                        password = password
                    )
                },
            )

            if (validPassword) {
                setState { copy(isDialogLoading = false, isPasswordDialogShown = false, areFieldsEnabled = true) }
            } else {
                setState { copy(isDialogLoading = false, isPasswordError = true) }
            }
        }
    }

    private fun commitChanges() {
        val token = viewModelScope.async {
            networkCallWithReturnWrapper(networkCall = {
                "Bearer ${AuthToken.getToken()}"
            })
        }

        viewModelScope.launch {
            setState { copy(isScreenLoading = true) }

            networkCallWrapper(networkCall = {
                val updatedUser = userRetrofit.editUserByUsername(
                    username = CurrentUser.info.username,
                    updatedFields = UserDTO.SignUpUserDTO(
                        email = viewState.email,
                        username = viewState.username,
                        password = viewState.password,
                        role = CurrentUser.info.role
                    ),
                    authToken = token.await() ?: throw Exception("something wrong with token")
                )
                CurrentUser.updateUserFromDTO(updatedUser)
            })

            setState { copy(isScreenLoading = false, isAreYouSureDialogShown = false) }
        }
    }
}