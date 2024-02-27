package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.CompanySize
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.contract.HomePageContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePageViewModel(private val navController: NavController) :
    BaseViewModel<HomePageContract.HomePageIntent, HomePageContract.HomePageState>() {
    override fun initialState(): HomePageContract.HomePageState {
        return HomePageContract.HomePageState(
            isLoading = true,
            vacancies = null,
            resumes = null
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is HomePageContract.HomePageIntent.LoadVacancies -> loadVacancies()
            is HomePageContract.HomePageIntent.LogOut -> logOut()
        }
    }

    private fun loadVacancies() {
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