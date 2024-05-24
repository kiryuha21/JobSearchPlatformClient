package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.JobApplicationDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.jobApplicationRetrofit
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.vacancyRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.OFFER_SAVING_TEXT
import com.kiryuha21.jobsearchplatformclient.ui.contract.OfferContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.OfferStages
import com.kiryuha21.jobsearchplatformclient.ui.contract.VACANCIES_LOADING_TEXT
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfferViewModel(
    private val resumeId: String,
    private val navigateBackToResume: () -> Unit,
    private val showToast: (String) -> Unit
) : BaseViewModel<OfferContract.Intent, OfferContract.State>() {
    override fun initialState(): OfferContract.State {
        return OfferContract.State(
            isLoading = false,
            loadingText = VACANCIES_LOADING_TEXT,
            vacancies = emptyList(),
            selectedVacancy = Vacancy(),
            message = "",
            stage = OfferStages.ChooseVacancy
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is OfferContract.Intent.CreateOffer -> createOffer()
            is OfferContract.Intent.SetOfferStage -> setState { copy(stage = intent.offerStage) }
            is OfferContract.Intent.ChooseVacancy -> setState { copy(selectedVacancy = intent.vacancy) }
            is OfferContract.Intent.UpdateMessage -> setState { copy(message = intent.newMessage) }
        }
    }

    init {
        loadVacancies()
    }

    private fun createOffer() {
        viewModelScope.launch {
            setState { copy(isLoading = true, loadingText = OFFER_SAVING_TEXT) }

            val isSavedSuccessfully = withContext(Dispatchers.IO) {
                networkCallWrapper(networkCall = {
                    jobApplicationRetrofit.createNewJobApplication(
                        authToken = "Bearer ${AuthToken.getToken()}",
                        jobApplicationDTO = JobApplicationDTO.JobApplicationRequestDTO(
                            referenceResumeId = resumeId,
                            referenceVacancyId = viewState.selectedVacancy.id,
                            message = viewState.message
                        )
                    )
                })
            }

            navigateBackToResume()
            if (isSavedSuccessfully) {
                showToast("Оффер отправлен")
            } else {
                showToast("При отправке оффера произошла ошибка")
            }
        }
    }

    private fun loadVacancies() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val publicVacancies = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = {
                        vacancyRetrofit.getPublicVacanciesByEmployerLogin(CurrentUser.info.username)
                    }
                )
            }

            if (publicVacancies != null) {
                setState { copy(isLoading = false, vacancies = publicVacancies.map { it.toDomainVacancy() }) }
            } else {
                setState { copy(isLoading = false) }
            }
        }
    }

    companion object {
        fun provideFactory(
            resumeId: String,
            navigateBackToResume: () -> Unit,
            showToast: (String) -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return OfferViewModel(
                        resumeId = resumeId,
                        navigateBackToResume = navigateBackToResume,
                        showToast = showToast
                    ) as T
                }
            }
    }
}