package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainJobApplication
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.jobApplicationRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.JobApplicationContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class JobApplicationViewModel(
    private val navigateToVacancyDetails: (String) -> Unit,
    private val navigateToResumeDetails: (String) -> Unit
) : BaseViewModel<JobApplicationContract.Intent, JobApplicationContract.State>() {
    override fun initialState(): JobApplicationContract.State {
        return JobApplicationContract.State(
            isLoading = true,
            sentApplications = emptyList(),
            receivedApplications = emptyList()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is JobApplicationContract.Intent.LoadApplications -> loadApplications()
            is JobApplicationContract.Intent.MarkSeen -> markSeen(intent.jobApplicationId)
            is JobApplicationContract.Intent.ShowVacancyDetails -> navigateToVacancyDetails(intent.vacancyId)
            is JobApplicationContract.Intent.ShowResumeDetails -> navigateToResumeDetails(intent.resumeId)
        }
    }

    init {
        loadApplications()
    }

    private fun loadApplications() {
        val received = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = {
                    jobApplicationRetrofit.getReceivedApplications("Bearer ${AuthToken.getToken()}")
                }
            )
        }

        val sent = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = {
                    jobApplicationRetrofit.getSentApplications("Bearer ${AuthToken.getToken()}")
                }
            )
        }

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val awaitedReceived = received.await()
            val awaitedSent = sent.await()

            if (awaitedSent != null && awaitedReceived != null) {
                setState { copy(
                    isLoading = false,
                    receivedApplications = awaitedReceived.map { it.toDomainJobApplication() },
                    sentApplications = awaitedSent.map { it.toDomainJobApplication() }
                ) }
            } else {
                setState { copy(isLoading = false) }
            }
        }
    }

    private fun markSeen(jobApplicationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkCallWrapper(
                networkCall = {
                    jobApplicationRetrofit.markSeen("Bearer ${AuthToken.getToken()}", jobApplicationId)
                }
            )
        }
    }

    companion object {
        fun provideFactory(
            navigateToVacancyDetails: (String) -> Unit,
            navigateToResumeDetails: (String) -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return JobApplicationViewModel(
                        navigateToVacancyDetails = navigateToVacancyDetails,
                        navigateToResumeDetails = navigateToResumeDetails
                    ) as T
                }
            }
    }
}