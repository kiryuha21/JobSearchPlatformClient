package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.CompanySize
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.data.remote.api.ResumeAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.api.VacancyAPI
import com.kiryuha21.jobsearchplatformclient.ui.contract.HomePageContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.RESUME_DETAILS_BASE
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.VACANCY_DETAILS_BASE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePageViewModel(private val navController: NavController) :
    BaseViewModel<HomePageContract.HomePageIntent, HomePageContract.HomePageState>() {
    private val resumeRetrofit = RetrofitObject.retrofit.create(ResumeAPI::class.java)
    private val vacancyRetrofit = RetrofitObject.retrofit.create(VacancyAPI::class.java)
    private val user by CurrentUser.info

    override fun initialState(): HomePageContract.HomePageState {
        return HomePageContract.HomePageState(
            isLoading = true,
            vacancies = null,
            resumes = null,
            openedVacancy = null,
            openedResume = null
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is HomePageContract.HomePageIntent.FindMatchingVacancies -> findMatchingVacancies()
            is HomePageContract.HomePageIntent.FindMatchingResumes -> findMatchingResumes()
            is HomePageContract.HomePageIntent.LoadProfileVacancies -> loadProfileVacancies()
            is HomePageContract.HomePageIntent.LoadProfileResumes -> loadProfileResumes()
            is HomePageContract.HomePageIntent.LogOut -> logOut()
            is HomePageContract.HomePageIntent.CreateNewResume -> createNewResume()
            is HomePageContract.HomePageIntent.CreateNewVacancy -> createNewVacancy()
            is HomePageContract.HomePageIntent.OpenResumeDetails -> openResumeDetails(intent.resumeId)
            is HomePageContract.HomePageIntent.OpenVacancyDetails -> openVacancyDetails(intent.vacancyId)
        }
    }

    private fun findMatchingVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val vacancies = vacancyRetrofit.getMatchingVacancies().map { it.toDomainVacancy() }
            setState { copy(isLoading = false, vacancies = vacancies) }
        }
    }

    private fun findMatchingResumes() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val resumes = resumeRetrofit.getMatchingResumes().map { it.toDomainResume() }
            setState { copy(isLoading = false, resumes = resumes) }
        }
    }

    private fun loadProfileResumes() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val resumes = resumeRetrofit.getResumesByWorkerLogin(user.username).map { it.toDomainResume() }
            setState { copy(isLoading = false, resumes = resumes) }
        }
    }
    private fun loadProfileVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val vacancies = vacancyRetrofit.getVacanciesByEmployerLogin(user.username).map { it.toDomainVacancy() }
            setState { copy(isLoading = false, vacancies = vacancies) }
        }
    }

    private fun openResumeDetails(resumeId: String) {
        navController.navigate("$RESUME_DETAILS_BASE/$resumeId")
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val resume = resumeRetrofit.getResumeById(resumeId).toDomainResume()
            setState { copy(isLoading = false, openedResume = resume) }
        }
    }

    private fun openVacancyDetails(vacancyId: String) {
        navController.navigate("$VACANCY_DETAILS_BASE/$vacancyId")
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val vacancy = vacancyRetrofit.getVacancyById(vacancyId).toDomainVacancy()
            setState { copy(isLoading = false, openedVacancy = vacancy) }
        }
    }

    private fun createNewResume(): Nothing = TODO()
    private fun createNewVacancy(): Nothing = TODO()

    private fun logOut() {
        navController.navigate(NavigationGraph.Authentication.LOG_IN) {
            popUpTo(NavigationGraph.MainApp.NAV_ROUTE) {
                inclusive = true
            }
        }
    }

    companion object {
        fun provideFactory(navController: NavController) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return HomePageViewModel(navController) as T
                }
            }
    }
}