package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.kiryuha21.jobsearchplatformclient.data.domain.ResumeFilter
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.data.remote.api.ResumeAPI
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmployerHomeViewModel(
    private val openResumeCallback: (String) -> Unit
) : BaseViewModel<EmployerHomeContract.Intent, EmployerHomeContract.State>() {
    private val resumeRetrofit by lazy { RetrofitObject.retrofit.create(ResumeAPI::class.java) }

    override fun initialState(): EmployerHomeContract.State {
        return EmployerHomeContract.State(
            isLoading = true,
            resumes = null,
            openedResume = null,
            filters = ResumeFilter()
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is EmployerHomeContract.Intent.LoadResumes -> loadResumes(intent.filters)
            is EmployerHomeContract.Intent.OpenResumeDetails -> openResumeDetails(intent.resumeId)
        }
    }

    private fun loadResumes(filter: ResumeFilter) {
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
}