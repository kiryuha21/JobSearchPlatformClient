package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

enum class OfferStages {
    ChooseVacancy,
    WriteMessage
}

const val VACANCIES_LOADING_TEXT = "Загружаем вакансии..."
const val OFFER_SAVING_TEXT = "Сохраняем оффер"

sealed class OfferContract {
    data class State(
        val isLoading: Boolean,
        val loadingText: String,
        val vacancies: List<Vacancy>,
        val selectedVacancy: Vacancy,
        val message: String,
        val stage: OfferStages
    ) : ViewState

    sealed interface Intent : ViewIntent {
        data class ChooseVacancy(val vacancy: Vacancy) : Intent
        data class SetOfferStage(val offerStage: OfferStages) : Intent
        data class UpdateMessage(val newMessage: String) : Intent
        data object CreateOffer : Intent
    }
}