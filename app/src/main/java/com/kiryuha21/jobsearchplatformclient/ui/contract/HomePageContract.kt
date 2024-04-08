package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class HomePageContract {
    data class HomePageState(
        val isLoading: Boolean,
        val vacancies: List<Vacancy>?,
        val resumes: List<Resume>?,
        val openedVacancy: Vacancy?,
        val openedResume: Resume?
    ) : ViewState

    sealed class HomePageIntent : ViewIntent {
        data object FindMatchingVacancies: HomePageIntent()
        data object LoadProfileVacancies: HomePageIntent()
        data object FindMatchingResumes: HomePageIntent()
        data object LoadProfileResumes: HomePageIntent()
        data object LogOut: HomePageIntent()
        data object OpenVacancyEdit: HomePageIntent()
        data class CreateNewVacancy(val vacancy: Vacancy): HomePageIntent()
        data object OpenResumeEdit: HomePageIntent()
        data class CreateNewResume(val resume: Resume): HomePageIntent()
        data class OpenVacancyDetails(val vacancyId: String): HomePageIntent()
        data class OpenResumeDetails(val resumeId: String): HomePageIntent()
    }
}