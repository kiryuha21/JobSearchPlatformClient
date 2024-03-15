package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableVacancyCard
import com.kiryuha21.jobsearchplatformclient.ui.components.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.components.ResumeCard
import com.kiryuha21.jobsearchplatformclient.ui.components.ShimmeringVacancyListItem
import com.kiryuha21.jobsearchplatformclient.ui.contract.HomePageContract
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

@Composable
fun HomeScreen(viewModel: HomePageViewModel) {
    val state by viewModel.viewState

    LaunchedEffect(key1 = true) {
        viewModel.processIntent(HomePageContract.HomePageIntent.FindMatchingVacancies)
    }

    LazyColumn {
        when (state.isLoading) {
            true -> {
                items(10) {
                    ShimmeringVacancyListItem(modifier = Modifier.fillMaxWidth())
                }
            }

            false -> {
                when (CurrentUser.userInfo.value.role) {
                    UserRole.Worker -> {
                        if (!state.vacancies.isNullOrEmpty()) {
                            items(state.vacancies!!) {
                                ClickableVacancyCard(
                                    vacancy = it,
                                    onClick = {
                                        viewModel.processIntent(
                                            HomePageContract.HomePageIntent.OpenVacancyDetails(
                                                it.id
                                            )
                                        )
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

                    UserRole.Employer -> {
                        if (!state.resumes.isNullOrEmpty()) {
                            items(state.resumes!!) {
                                ResumeCard(resume = it, modifier = Modifier.fillMaxWidth())
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
}