package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.AppDataStore
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.UserDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.userRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.ImportantAction
import com.kiryuha21.jobsearchplatformclient.ui.contract.SettingsContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val tokenDatasourceProvider: AppDataStore,
    private val logOut: () -> Unit
): BaseViewModel<SettingsContract.Intent, SettingsContract.State>() {
    override fun initialState(): SettingsContract.State {
        return SettingsContract.State(
            isScreenLoading = false,
            isPasswordDialogShown = false,
            isDialogLoading = false,
            isAreYouSureDialogShown = false,
            areFieldsEnabled = false,
            isPasswordError = false,
            importantAction = ImportantAction.ChangeAccountData,
            areNotificationsOn = false,
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
            is SettingsContract.Intent.ShowAreYouSureDialog -> setState { copy(isAreYouSureDialogShown = true, importantAction = intent.action) }
            is SettingsContract.Intent.HideAreYouSureDialog -> setState { copy(isAreYouSureDialogShown = false) }
            is SettingsContract.Intent.ToggleNotifications -> toggleNotifications(intent.newValue)
        }
    }

    init {
        viewModelScope.launch {
            setState { copy(isScreenLoading = true) }
            val notificationsOn = tokenDatasourceProvider.getNotifications().stateIn(this).value
            setState { copy(isScreenLoading = false, areNotificationsOn = notificationsOn) }
        }
    }

    private fun toggleNotifications(newValue: Boolean) {
        viewModelScope.launch {
            tokenDatasourceProvider.toggleNotifications(newValue)
        }
        setState { copy(areNotificationsOn = newValue) }
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
                setState { copy(isDialogLoading = false, isPasswordDialogShown = false, areFieldsEnabled = true, password = password) }
            } else {
                setState { copy(isDialogLoading = false, isPasswordError = true) }
            }
        }
    }

    private fun commitChanges() {
        if (viewState.importantAction == ImportantAction.DeleteAccount) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    networkCallWrapper(networkCall = {
                        userRetrofit.deleteUserByUsername(
                            username = CurrentUser.info.username,
                            authToken = "Bearer ${AuthToken.getToken()}"
                        )
                    })
                }

                logOut()
            }
        } else {
            viewModelScope.launch {
                setState { copy(isScreenLoading = true, isAreYouSureDialogShown = false) }

                val updatedUser = withContext(Dispatchers.IO) {
                    networkCallWithReturnWrapper(networkCall = {
                        userRetrofit.editUserByUsername(
                            username = CurrentUser.info.username,
                            updatedFields = UserDTO.SignUpUserDTO(
                                email = viewState.email,
                                username = viewState.username,
                                password = viewState.password,
                                role = CurrentUser.info.role
                            ),
                            authToken = "Bearer ${AuthToken.getToken()}"
                        )
                    })
                }

                if (updatedUser != null) {
                    CurrentUser.updateUserFromDTO(updatedUser)
                    AuthToken.createToken(viewState.username, viewState.password)
                }
                setState { copy(isScreenLoading = false) }
            }
        }
    }

    companion object {
        fun provideFactory(
            tokenDatasourceProvider: AppDataStore,
            logOut: () -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return SettingsViewModel(
                        tokenDatasourceProvider = tokenDatasourceProvider,
                        logOut = logOut
                    ) as T
                }
            }
    }
}