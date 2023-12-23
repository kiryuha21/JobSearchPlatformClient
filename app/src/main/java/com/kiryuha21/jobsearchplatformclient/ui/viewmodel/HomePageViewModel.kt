package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.model.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.contract.HomePageContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePageViewModel(navController: NavController) :
    BaseViewModel<HomePageContract.HomePageIntent, HomePageContract.HomePageState>() {
    override fun initialState(): HomePageContract.HomePageState {
        return HomePageContract.HomePageState.Loading
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is HomePageContract.HomePageIntent.LoadVacancies -> loadVacancies()
        }
    }

    private fun loadVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(5000)
            val vacancies = listOf(
                Vacancy(
                    title = "Cave Digger",
                    description = "In this good company you will have everything you want and even money",
                    company = "Gold rocks",
                    minSalary = 15000,
                    maxSalary = 20000
                ),
                Vacancy(
                    title = "Cave Digger",
                    description = "In this good company you will have everything you want and even money",
                    company = "Gold rocks",
                    minSalary = 15000,
                    maxSalary = 20000
                ),
                Vacancy(
                    title = "Cave Digger",
                    description = "In this good company you will have everything you want and even money",
                    company = "Gold rocks",
                    minSalary = 15000,
                    maxSalary = 20000
                ),
            )
            _viewState.value = HomePageContract.HomePageState.Success(vacancies)
            println("here")
        }
    }
}