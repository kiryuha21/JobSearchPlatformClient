package com.kiryuha21.jobsearchplatformclient.ui.contract

import android.graphics.Bitmap
import android.net.Uri
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

sealed class MainAppContract {
    data class MainAppState(
        val isLoading: Boolean,
        val vacancies: List<Vacancy>?,
        val resumes: List<Resume>?,
        val openedVacancy: Vacancy?,
        val openedResume: Resume?
    ) : ViewState

    sealed class MainAppIntent : ViewIntent {
        data object FindMatchingVacancies: MainAppIntent()
        data object LoadProfileVacancies: MainAppIntent()
        data object FindMatchingResumes: MainAppIntent()
        data object LoadProfileResumes: MainAppIntent()
        data object LogOut: MainAppIntent()
        data class OpenVacancyEdit(val vacancy: Vacancy): MainAppIntent()
        data class CreateNewVacancy(val vacancy: Vacancy, val bitmap: Bitmap?): MainAppIntent()
        data class EditVacancy(val vacancy: Vacancy, val bitmap: Bitmap?): MainAppIntent()
        data class DeleteVacancy(val vacancy: Vacancy): MainAppIntent()
        data class OpenResumeEdit(val resume: Resume): MainAppIntent()
        data class CreateNewResume(val resume: Resume, val bitmap: Bitmap?): MainAppIntent()
        data class EditResume(val resume: Resume, val bitmap: Bitmap?): MainAppIntent()
        data class DeleteResume(val resume: Resume): MainAppIntent()
        data class OpenVacancyDetails(val vacancyId: String): MainAppIntent()
        data class OpenResumeDetails(val resumeId: String): MainAppIntent()
        data class SetUserImage(val bitmap: Bitmap): MainAppIntent()
    }
}