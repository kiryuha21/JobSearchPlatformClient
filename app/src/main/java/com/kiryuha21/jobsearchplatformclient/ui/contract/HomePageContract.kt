package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.model.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class HomePageContract {
    sealed class HomePageState : ViewState {
        data object Loading: HomePageState()
        data class Success(val vacancies: List<Vacancy>): HomePageState()
    }

    sealed class HomePageIntent : ViewIntent {
        data object LoadVacancies: HomePageIntent()
        data object LogOut: HomePageIntent()
    }
}