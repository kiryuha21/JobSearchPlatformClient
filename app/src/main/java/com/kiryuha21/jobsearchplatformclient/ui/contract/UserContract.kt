package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.model.User
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class UserContract {
    sealed class UserState: ViewState {
        data object Unauthorized : UserState()
        data class Authorized(val user: User) : UserState()
    }

    sealed class UserIntent: ViewIntent {
        data class TryLogIn(val username: String, val password: String) : UserIntent()
        data class TrySignUp(val login: String, val email: String, val password: String) : UserIntent()
        data class TryResetPassword(val email: String) : UserIntent()
    }
}