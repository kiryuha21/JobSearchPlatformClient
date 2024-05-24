package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.JobApplicationDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.ui.contract.RESPONSE_SAVING_TEXT
import com.kiryuha21.jobsearchplatformclient.ui.contract.RESPONSE_SENT_ERROR
import com.kiryuha21.jobsearchplatformclient.ui.contract.RESPONSE_SENT_SUCCESS
import com.kiryuha21.jobsearchplatformclient.ui.contract.RESUMES_LOADING_TEXT
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResponseContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResponseStages
import com.kiryuha21.jobsearchplatformclient.ui.contract.VACANCIES_LOADING_TEXT
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResponseViewModel(
    private val vacancyId: String,
    private val navigateBackToVacancy: () -> Unit,
    private val showToast: (String) -> Unit
) : BaseViewModel<ResponseContract.Intent, ResponseContract.State>() {
    override fun initialState(): ResponseContract.State {
        return ResponseContract.State(
            isLoading = false,
            loadingText = VACANCIES_LOADING_TEXT,
            resumes = emptyList(),
            selectedResume = Resume(),
            message = "",
            stage = ResponseStages.ChooseResume
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is ResponseContract.Intent.CreateResponse -> createResponse()
            is ResponseContract.Intent.SetResponseStage -> setState { copy(stage = intent.responseStage) }
            is ResponseContract.Intent.ChooseResume -> setState { copy(selectedResume = intent.resume) }
            is ResponseContract.Intent.UpdateMessage -> setState { copy(message = intent.newMessage) }
        }
    }

    init {
        loadResumes()
    }

    private fun createResponse() {
        viewModelScope.launch {
            setState { copy(isLoading = true, loadingText = RESPONSE_SAVING_TEXT) }

            val isSavedSuccessfully = withContext(Dispatchers.IO) {
                networkCallWrapper(networkCall = {
                    RetrofitObject.jobApplicationRetrofit.createNewJobApplication(
                        authToken = "Bearer ${AuthToken.getToken()}",
                        jobApplicationDTO = JobApplicationDTO.JobApplicationRequestDTO(
                            referenceResumeId = viewState.selectedResume.id,
                            referenceVacancyId = vacancyId,
                            message = viewState.message
                        )
                    )
                })
            }

            navigateBackToVacancy()
            if (isSavedSuccessfully) {
                showToast(RESPONSE_SENT_SUCCESS)
            } else {
                showToast(RESPONSE_SENT_ERROR)
            }
        }
    }

    private fun loadResumes() {
        viewModelScope.launch {
            setState { copy(isLoading = true, loadingText = RESUMES_LOADING_TEXT) }

            val publicResumes = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = {
                        RetrofitObject.resumeRetrofit.getPublicResumesByWorkerLogin(CurrentUser.info.username)
                    }
                )
            }

            if (publicResumes != null) {
                setState { copy(isLoading = false, resumes = publicResumes.map { it.toDomainResume() }) }
            } else {
                setState { copy(isLoading = false) }
            }
        }
    }

    companion object {
        fun provideFactory(
            vacancyId: String,
            navigateBackToVacancy: () -> Unit,
            showToast: (String) -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return ResponseViewModel(
                        vacancyId = vacancyId,
                        navigateBackToVacancy = navigateBackToVacancy,
                        showToast = showToast
                    ) as T
                }
            }
    }
}