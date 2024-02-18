package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import com.kiryuha21.jobsearchplatformclient.ui.contract.UserContract

class UserViewModel : BaseViewModel<UserContract.UserIntent, UserContract.UserState>() {
    override fun initialState(): UserContract.UserState {
        return UserContract.UserState.Unauthorized
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is UserContract.UserIntent.TryLogIn -> {

            }
        }
    }

}