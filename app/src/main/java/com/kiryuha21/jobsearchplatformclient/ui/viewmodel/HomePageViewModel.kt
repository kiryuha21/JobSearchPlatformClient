package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.create

class HomePageViewModel(private val navController: NavController) :
    BaseViewModel<HomePageContract.HomePageIntent, HomePageContract.HomePageState>() {
    private val resumeRetrofit = RetrofitObject.retrofit.create(ResumeAPI::class.java)
    private val vacancyRetrofit = RetrofitObject.retrofit.create(VacancyAPI::class.java)

    override fun initialState(): HomePageContract.HomePageState {
        return HomePageContract.HomePageState(
            isLoading = true,
            vacancies = null,
            resumes = null
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is HomePageContract.HomePageIntent.FindMatchingVacancies -> findMatchingVacancies()
            is HomePageContract.HomePageIntent.FindMatchingResumes -> findMatchingResumes()
            is HomePageContract.HomePageIntent.LoadProfileVacancies -> loadProfileVacancies()
            is HomePageContract.HomePageIntent.LoadProfileResumes -> loadProfileResumes()
            is HomePageContract.HomePageIntent.LogOut -> logOut()
        }
    }

    private fun findMatchingVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(5000)
            val vacancies = listOf(
                Vacancy(
                    title = "Cave Digger",
                    description = "In this good company you will have everything you want and even money",
                    company = Company("Gold rocks", CompanySize.Big),
                    minSalary = 15000,
                    maxSalary = 20000
                ),
                Vacancy(
                    title = "Cave Digger",
                    description = "In this good company you will have everything you want and even money",
                    company = Company("Gold rocks", CompanySize.Big),
                    minSalary = 15000,
                    maxSalary = 20000
                ),
                Vacancy(
                    title = "Cave Digger",
                    description = "In this good company you will have everything you want and even money",
                    company = Company("Gold rocks", CompanySize.Big),
                    minSalary = 15000,
                    maxSalary = 20000
                ),
            )
            setState { copy(vacancies = vacancies, isLoading = false) }
        }
    }

    private fun findMatchingResumes(): Nothing = TODO()
    private fun loadProfileResumes() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val resumes = resumeRetrofit.getResumesByWorkerLogin(CurrentUser.userInfo.value.username).map { it.toDomainResume() }
            setState { copy(isLoading = false, resumes = resumes) }
        }
    }
    private fun loadProfileVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val vacancies = vacancyRetrofit.getVacanciesByEmployerLogin(CurrentUser.userInfo.value.username).map { it.toDomainVacancy() }
            setState { copy(isLoading = false, vacancies = vacancies) }
        }
    }


    private fun logOut() {
        navController.navigate(NavigationGraph.Authentication.LogIn) {
            popUpTo(NavigationGraph.MainApp.route) {
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