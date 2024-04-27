package com.kiryuha21.jobsearchplatformclient.ui.contract

import android.graphics.Bitmap
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

class VacancyDetailsContract {
    data class State(
        val isLoading: Boolean,
        val openedVacancy: Vacancy?
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class EditVacancy(val vacancy: Vacancy, val bitmap: Bitmap?) : Intent()
        data class CreateVacancy(val vacancy: Vacancy, val bitmap: Bitmap?) : Intent()
        data class DeleteVacancy(val vacancyId: String) : Intent()
        data class LoadVacancy(val vacancyId: String) : Intent()
        data class OpenEdit(val vacancy: Vacancy) : Intent()
    }
}