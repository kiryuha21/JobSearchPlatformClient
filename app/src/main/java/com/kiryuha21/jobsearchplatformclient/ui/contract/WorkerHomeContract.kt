package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.MoreItemsState
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class WorkerHomeContract {
    data class State(
        val isLoading: Boolean,
        val areRecommendationsShown: Boolean,
        val areOnlineRecommendationsReady: Boolean,
        val moreItemsState: MoreItemsState,
        val nextLoadPage: Int,
        val vacancies: List<Vacancy>,
        val filters: VacancyFilters
    ): ViewState

    sealed interface Intent : ViewIntent {
        data class LoadVacancies(val filters: VacancyFilters): Intent
        data class LoadRecommendations(val pageNumber: Int) : Intent
        data class OpenVacancyDetails(val vacancyId: String): Intent
        data object SwitchToOnlineRecommendations: Intent
        data object RefreshRecommendations: Intent
    }
}