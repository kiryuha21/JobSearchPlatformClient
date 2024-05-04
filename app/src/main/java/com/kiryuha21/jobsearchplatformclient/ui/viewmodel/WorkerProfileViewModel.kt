package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.resumeRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerProfileContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerProfileViewModel(
    private val openResumeDetailsCallback: (String) -> Unit,
    private val createResumeCallback: () -> Unit
) : BaseViewModel<WorkerProfileContract.Intent, WorkerProfileContract.State>() {
    override fun initialState(): WorkerProfileContract.State {
        return WorkerProfileContract.State(
            isLoading = false,
            resumes = null
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is WorkerProfileContract.Intent.LoadResumes -> loadResumes()
            is WorkerProfileContract.Intent.OpenResumeDetails -> openResumeDetailsCallback(intent.resumeId)
            is WorkerProfileContract.Intent.CreateResume -> createResumeCallback()
        }
    }

    private fun loadResumes() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val resumesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.getResumesByWorkerLogin(CurrentUser.info.username).map { it.toDomainResume() } }
                )
            }
            setState { copy(isLoading = false, resumes = resumesResult) }
        }
    }

    companion object {
        fun provideFactory(
            openResumeDetailsCallback: (String) -> Unit,
            createResumeCallback: () -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return WorkerProfileViewModel(
                        openResumeDetailsCallback = openResumeDetailsCallback,
                        createResumeCallback = createResumeCallback
                    ) as T
                }
            }
    }
}