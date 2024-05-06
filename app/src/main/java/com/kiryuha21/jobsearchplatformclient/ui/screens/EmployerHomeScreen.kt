package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.PageRequestFilter
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ClickableResumeCard
import com.kiryuha21.jobsearchplatformclient.ui.components.display.LastPaginationListItem
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ShimmeringVacancyListItem
import com.kiryuha21.jobsearchplatformclient.ui.components.searchbar.ResumeSearchBar
import com.kiryuha21.jobsearchplatformclient.ui.components.special.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract
import kotlinx.coroutines.launch

@Composable
fun EmployerHomeScreen(
    state: EmployerHomeContract.State,
    resetPage: () -> Unit,
    loadFiltered: (ResumeFilters) -> Unit,
    loadRecommended: (Int) -> Unit,
    openResumeDetails: (String) -> Unit,
    switchToOnlineRecommendations: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        listState.scrollToItem(0)
        resetPage()
    }

    Column(modifier = modifier) {
        ResumeSearchBar(
            initFilters = state.filters,
            onSearch = {
                coroutineScope.launch {
                    listState.scrollToItem(0)
                    loadFiltered(it.copy(pageRequestFilter = PageRequestFilter()))
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )

        AnimatedVisibility(visible = state.areOnlineRecommendationsReady) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                        switchToOnlineRecommendations()
                    }
                },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Найдены новые резюме! Показать?")
            }
        }

        LazyColumn(state = listState) {
            when (state.isLoading) {
                true -> {
                    items(10) {
                        ShimmeringVacancyListItem(modifier = Modifier.fillMaxWidth())
                    }
                }

                false -> {
                    if (state.resumes.isNotEmpty()) {
                        items(state.resumes, key = { resume -> resume.id }) {
                            ClickableResumeCard(
                                resume = it,
                                onClick = { openResumeDetails(it.id) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item(key = state.moreItemsState) {
                            LastPaginationListItem(
                                state = state.moreItemsState,
                                loadMore = {
                                    if (state.areRecommendationsShown) {
                                        loadRecommended(state.nextLoadPage)
                                    } else {
                                        val newFilter = state.filters.pageRequestFilter.copy(pageNumber = state.nextLoadPage)
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