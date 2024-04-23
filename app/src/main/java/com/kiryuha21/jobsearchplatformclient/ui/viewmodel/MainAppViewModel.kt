package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.TokenDataStore
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.mappers.toVacancyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.data.remote.api.ResumeAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.api.UserAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.api.VacancyAPI
import com.kiryuha21.jobsearchplatformclient.ui.contract.MainAppContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.PROFILE
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.RESUME_DETAILS_BASE
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.RESUME_EDIT
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.VACANCY_DETAILS_BASE
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.VACANCY_EDIT
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import com.kiryuha21.jobsearchplatformclient.util.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class MainAppViewModel(
    private val navController: NavController,
    private val tokenDatasourceProvider : TokenDataStore
) : BaseViewModel<MainAppContract.MainAppIntent, MainAppContract.MainAppState>() {
    private val resumeRetrofit by lazy { RetrofitObject.retrofit.create(ResumeAPI::class.java) }
    private val vacancyRetrofit by lazy { RetrofitObject.retrofit.create(VacancyAPI::class.java) }
    private val userRetrofit by lazy { RetrofitObject.retrofit.create(UserAPI::class.java) }

    override fun initialState(): MainAppContract.MainAppState {
        return MainAppContract.MainAppState(
            isLoading = false,
            vacancies = null,
            resumes = null,
            openedVacancy = null,
            openedResume = null
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is MainAppContract.MainAppIntent.OpenVacancyEdit -> {
                setState { copy(openedVacancy = intent.vacancy) }
                navController.navigate(VACANCY_EDIT)
            }
            is MainAppContract.MainAppIntent.OpenResumeEdit -> {
                setState { copy(openedResume = intent.resume) }
                navController.navigate(RESUME_EDIT)
            }
            is MainAppContract.MainAppIntent.FindMatchingVacancies -> findMatchingVacancies()
            is MainAppContract.MainAppIntent.FindMatchingResumes -> findMatchingResumes()
            is MainAppContract.MainAppIntent.LoadProfileVacancies -> loadProfileVacancies()
            is MainAppContract.MainAppIntent.LoadProfileResumes -> loadProfileResumes()
            is MainAppContract.MainAppIntent.OpenResumeDetails -> openResumeDetails(intent.resumeId)
            is MainAppContract.MainAppIntent.CreateNewResume -> createNewResume(intent.resume, intent.bitmap)
            is MainAppContract.MainAppIntent.EditResume -> editResume(intent.resume, intent.bitmap)
            is MainAppContract.MainAppIntent.DeleteResume -> deleteResume(intent.resume.id)
            is MainAppContract.MainAppIntent.OpenVacancyDetails -> openVacancyDetails(intent.vacancyId)
            is MainAppContract.MainAppIntent.CreateNewVacancy -> createNewVacancy(intent.vacancy, intent.bitmap)
            is MainAppContract.MainAppIntent.EditVacancy -> editVacancy(intent.vacancy, intent.bitmap)
            is MainAppContract.MainAppIntent.DeleteVacancy -> deleteVacancy(intent.vacancy.id)
            is MainAppContract.MainAppIntent.SetUserImage -> setUserImage(intent.bitmap)
            is MainAppContract.MainAppIntent.LogOut -> logOut()
        }
    }

    private fun setUserImage(bitmap: Bitmap) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(networkCall = {
                "Bearer ${AuthToken.getToken()}"
            })
        }

        viewModelScope.launch(Dispatchers.IO) {
            val body = MultipartBody.Part.createFormData(
                name = "picture",
                filename = CurrentUser.info.username,
                body = bitmap.toRequestBody(100)
            )

            networkCallWrapper(
                networkCall = { userRetrofit.setPicture(token.await().toString(), CurrentUser.info.username, body) }
            )
        }
    }

    private fun findMatchingVacancies() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val vacanciesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getMatchingVacancies().map { it.toDomainVacancy() } }
                )
            }
            setState { copy(isLoading = false, vacancies = vacanciesResult) }
        }
    }

    private fun findMatchingResumes() {
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

    private fun loadProfileResumes() {
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

    private fun loadProfileVacancies() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val vacanciesResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getVacanciesByEmployerLogin(CurrentUser.info.username).map { it.toDomainVacancy() } }
                )
            }
            setState { copy(isLoading = false, vacancies = vacanciesResult) }
        }
    }

    private fun openResumeDetails(resumeId: String) {
        navController.navigate("$RESUME_DETAILS_BASE/$resumeId")
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val resumeResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.getResumeById(resumeId).toDomainResume() }
                )
            }
            setState { copy(isLoading = false, openedResume = resumeResult) }
        }
    }

    private fun createNewResume(resume: Resume, bitmap: Bitmap?) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val resumeResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = {
                        resumeRetrofit.createNewResume(token.await().toString(), resume.toResumeDTO())
                    }
                )
            }

            if (bitmap != null && resumeResult != null) {
                val body = MultipartBody.Part.createFormData(
                    name = "picture",
                    filename = resume.id,
                    body = bitmap.toRequestBody(100)
                )
                withContext(Dispatchers.IO) {
                    networkCallWrapper(
                        networkCall = {
                            resumeRetrofit.setPicture(token.await().toString(), resumeResult.id, body)
                        }
                    )
                }
            }

            setState { copy(isLoading = false) }
            navController.popBackStack()
            navController.navigate(PROFILE)
        }
    }

    private fun editResume(resume: Resume, bitmap: Bitmap?) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        val job = if (bitmap != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val body = MultipartBody.Part.createFormData(
                    name = "picture",
                    filename = resume.id,
                    body = bitmap.toRequestBody(100)
                )
                networkCallWrapper(
                    networkCall = { resumeRetrofit.setPicture(token.await().toString(), resume.id, body) }
                )
            }
        } else null

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val editedResume = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.editResume(token.await().toString(), resume.id, resume.toResumeDTO()) }
                )
            }
            job?.join()
            setState { copy(openedResume = editedResume?.toDomainResume(), isLoading = false) }
            navController.navigate(PROFILE)
        }
    }

    private fun deleteResume(resumeId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                networkCallWrapper(
                    networkCall = { resumeRetrofit.deleteResume("Bearer ${AuthToken.getToken()}", resumeId) }
                )
            }

            navController.popBackStack()
            navController.navigate(PROFILE)
        }
    }

    private fun openVacancyDetails(vacancyId: String) {
        navController.navigate("$VACANCY_DETAILS_BASE/$vacancyId")
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val vacancy = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getVacancyById(vacancyId).toDomainVacancy() }
                )
            }
            setState { copy(isLoading = false, openedVacancy = vacancy) }
        }
    }

    private fun createNewVacancy(vacancy: Vacancy, bitmap: Bitmap?) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val newVacancy = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = {
                        vacancyRetrofit.createNewVacancy(
                            token.await().toString(),
                            vacancy.toVacancyDTO()
                        )
                    }
                )
            }

            if (bitmap != null && newVacancy != null) {
                val body = MultipartBody.Part.createFormData(
                    name = "picture",
                    filename = vacancy.id,
                    body = bitmap.toRequestBody(100)
                )
                networkCallWrapper(
                    networkCall = { vacancyRetrofit.setPicture(token.await().toString(), newVacancy.id, body) }
                )
            }

            setState { copy(isLoading = false) }
            navController.popBackStack()
            navController.navigate(PROFILE)
        }
    }

    private fun editVacancy(vacancy: Vacancy, bitmap: Bitmap?) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        val job = if (bitmap != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val body = MultipartBody.Part.createFormData(
                    name = "picture",
                    filename = vacancy.id,
                    body = bitmap.toRequestBody(100)
                )
                networkCallWrapper(
                    networkCall = { vacancyRetrofit.setPicture(token.await().toString(), vacancy.id, body) }
                )
            }
        } else null

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val editedVacancy = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.editVacancy(token.await().toString(), vacancy.id, vacancy.toVacancyDTO()) }
                )
            }
            job?.join()

            setState { copy(openedVacancy = editedVacancy?.toDomainVacancy(), isLoading = false) }
            navController.navigate(PROFILE)
        }
    }

    private fun deleteVacancy(vacancyId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                networkCallWrapper(
                    networkCall = { vacancyRetrofit.deleteVacancy("Bearer ${AuthToken.getToken()}", vacancyId) }
                )
            }
            navController.popBackStack()
            navController.navigate(PROFILE)
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tokenDatasourceProvider.deleteRefreshToken()
            }
            navController.navigate(NavigationGraph.Authentication.LOG_IN) {
                popUpTo(NavigationGraph.MainApp.NAV_ROUTE) {
                    inclusive = true
                }
            }
        }
    }

    companion object {
        fun provideFactory(navController: NavController, tokenDatasourceProvider : TokenDataStore) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return MainAppViewModel(navController, tokenDatasourceProvider) as T
                }
            }
    }
}