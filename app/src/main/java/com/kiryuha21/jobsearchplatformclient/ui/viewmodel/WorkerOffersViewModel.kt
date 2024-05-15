package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainJobApplication
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.jobApplicationRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerOffersContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerOffersViewModel(
    private val navigateToVacancyDetails: (String) -> Unit
) : BaseViewModel<WorkerOffersContract.Intent, WorkerOffersContract.State>() {
    override fun initialState(): WorkerOffersContract.State {
        return WorkerOffersContract.State(
            isLoading = true,
            jobApplications = emptyList()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is WorkerOffersContract.Intent.LoadOffers -> loadOffers()
            is WorkerOffersContract.Intent.MarkSeen -> markSeen(intent.jobApplicationId)
            is WorkerOffersContract.Intent.ShowVacancyDetails -> navigateToVacancyDetails(intent.vacancyId)
        }
    }

    init {
        loadOffers()
    }

    private fun loadOffers() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val offers = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(networkCall = {
                    jobApplicationRetrofit.getJobApplications("Bearer ${AuthToken.getToken()}")
                })
            }

            if (offers != null) {
                setState { copy(isLoading = false, jobApplications = offers.map { it.toDomainJobApplication() }) }
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
        fun provideFactory(navigateToVacancyDetails: (String) -> Unit) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return WorkerOffersViewModel(navigateToVacancyDetails) as T
                }
            }
    }
}