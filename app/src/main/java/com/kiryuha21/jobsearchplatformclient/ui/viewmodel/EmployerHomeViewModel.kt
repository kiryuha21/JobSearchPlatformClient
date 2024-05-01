package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject.resumeRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmployerHomeViewModel(
    private val openResumeCallback: (String) -> Unit
) : BaseViewModel<EmployerHomeContract.Intent, EmployerHomeContract.State>() {
    override fun initialState(): EmployerHomeContract.State {
        return EmployerHomeContract.State(
            isLoading = true,
            resumes = null,
            openedResume = null,
            filters = ResumeFilters()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is EmployerHomeContract.Intent.LoadResumes -> loadResumes(intent.filters)
            is EmployerHomeContract.Intent.OpenResumeDetails -> openResumeDetails(intent.resumeId)
        }
    }

    private fun loadResumes(filter: ResumeFilters) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val resumesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.getMatchingResumes().map { it.toDomainResume() } }
                )
            }
            setState { copy(isLoading = false, resumes = resumesResult) }
        }
    }

    private fun openResumeDetails(resumeId: String) {
        openResumeCallback(resumeId)
    }

    companion object {
        fun provideFactory(openResumeCallback: (String) -> Unit) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return EmployerHomeViewModel(openResumeCallback) as T
                }
            }
    }
}