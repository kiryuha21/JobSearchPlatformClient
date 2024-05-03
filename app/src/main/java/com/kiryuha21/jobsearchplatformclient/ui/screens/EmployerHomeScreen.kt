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
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ClickableResumeCard
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ShimmeringVacancyListItem
import com.kiryuha21.jobsearchplatformclient.ui.components.searchbar.ResumeSearchBar
import com.kiryuha21.jobsearchplatformclient.ui.components.special.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerHomeContract

@Composable
fun EmployerHomeScreen(
    state: EmployerHomeContract.State,
    loadResumes: (ResumeFilters) -> Unit,
    openResumeDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true) {
        loadResumes(state.filters)
    }

    Column {
        ResumeSearchBar(
            initFilters = state.filters,
            onSearch = { loadResumes(it) },
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
                    if (!state.resumes.isNullOrEmpty()) {
                        items(state.resumes) {
                            ClickableResumeCard(
                                resume = it,
                                onClick = {
                                    openResumeDetails(it.id)
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