package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadedVacancyItem
import com.kiryuha21.jobsearchplatformclient.ui.components.ShimmeringVacancyListItem
import com.kiryuha21.jobsearchplatformclient.ui.contract.HomePageContract
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

@Composable
fun HomeScreen(viewModel: HomePageViewModel) {
    val state by viewModel.viewState

    LaunchedEffect(key1 = true) {
        viewModel.processIntent(HomePageContract.HomePageIntent.LoadVacancies)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        when(state) {
            is HomePageContract.HomePageState.Loading -> {
                items(20) {
                    ShimmeringVacancyListItem(modifier = Modifier.fillMaxWidth())
                }
            }

            is HomePageContract.HomePageState.Success -> {
                items((state as HomePageContract.HomePageState.Success).vacancies) {
                    LoadedVacancyItem(vacancy = it, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}