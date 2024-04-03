package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableResumeCard
import com.kiryuha21.jobsearchplatformclient.ui.components.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.components.ShimmeringVacancyListItem
import com.kiryuha21.jobsearchplatformclient.ui.contract.HomePageContract

@Composable
fun EmployerHomeScreen(
    state: HomePageContract.HomePageState,
    loadResumes: () -> Unit,
    openResumeDetails: (String) -> Unit
) {
    LaunchedEffect(key1 = true) {
        loadResumes()
    }

    LazyColumn {
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