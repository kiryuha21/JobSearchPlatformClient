package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableVacancyCard
import com.kiryuha21.jobsearchplatformclient.ui.components.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.components.ShimmeringVacancyListItem
import com.kiryuha21.jobsearchplatformclient.ui.components.VacancySearchBar
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract

@Composable
fun WorkerHomeScreen(
    state: WorkerHomeContract.State,
    loadVacancies: (filters: VacancyFilters) -> Unit,
    openVacancyDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true) {
        loadVacancies(state.filters)
    }

    Column {
        VacancySearchBar(
            initFilters = state.filters,
            onSearch = { loadVacancies(it) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )

        LazyColumn(
            modifier = modifier
        ) {
            when (state.isLoading) {
                true -> {
                    items(10) {
                        ShimmeringVacancyListItem(modifier = Modifier.fillMaxWidth())
                    }
                }

                false -> {
                    if (!state.vacancies.isNullOrEmpty()) {
                        items(state.vacancies) {
                            ClickableVacancyCard(
                                vacancy = it,
                                onClick = {
                                    openVacancyDetails(it.id)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        item {
                            NoItemsCard()
                        }
                    }
                }
            }
        }
    }
}