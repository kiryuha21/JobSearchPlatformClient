package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.PageRequestFilter
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.START_PAGE
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ClickableVacancyCard
import com.kiryuha21.jobsearchplatformclient.ui.components.display.LastPaginationListItem
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ShimmeringVacancyListItem
import com.kiryuha21.jobsearchplatformclient.ui.components.searchbar.VacancySearchBar
import com.kiryuha21.jobsearchplatformclient.ui.components.special.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerHomeContract
import kotlinx.coroutines.launch

@Composable
fun WorkerHomeScreen(
    state: WorkerHomeContract.State,
    loadFiltered: (VacancyFilters) -> Unit,
    loadRecommended: (Int) -> Unit,
    openVacancyDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        listState.scrollToItem(0)
        loadRecommended(START_PAGE)
    }

    Column(modifier = modifier) {
        VacancySearchBar(
            initFilters = state.filters,
            onSearch = {
                coroutineScope.launch {
                    listState.scrollToItem(0)
                    loadFiltered(it.copy(pageRequestFilter = PageRequestFilter()))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        LazyColumn(state = listState) {
            when (state.isLoading) {
                true -> {
                    items(10) {
                        ShimmeringVacancyListItem(modifier = Modifier.fillMaxWidth())
                    }
                }

                false -> {
                    if (!state.vacancies.isNullOrEmpty()) {
                        items(state.vacancies, key = { vacancy -> vacancy.id }) {
                            ClickableVacancyCard(
                                vacancy = it,
                                onClick = { openVacancyDetails(it.id) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item(key = state.moreItemsState) {
                            LastPaginationListItem(
                                state = state.moreItemsState,
                                loadMore = {
                                    if (state.areRecommendationsShown) {
                                        loadRecommended(state.pageNumber + 1)
                                    } else {
                                        val newFilter = state.filters.pageRequestFilter.copy(pageNumber = state.pageNumber + 1)
                                        loadFiltered(state.filters.copy(pageRequestFilter = newFilter))
                                    }
                                }
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