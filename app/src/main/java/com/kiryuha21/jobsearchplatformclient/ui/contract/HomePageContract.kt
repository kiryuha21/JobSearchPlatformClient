package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class HomePageContract {
    data class HomePageState(
        val isLoading: Boolean,
        val vacancies: List<Vacancy>?,
        val resumes: List<Resume>?
    ) : ViewState

    sealed class HomePageIntent : ViewIntent {
        data object LoadVacancies: HomePageIntent()
        data object LogOut: HomePageIntent()
    }
}