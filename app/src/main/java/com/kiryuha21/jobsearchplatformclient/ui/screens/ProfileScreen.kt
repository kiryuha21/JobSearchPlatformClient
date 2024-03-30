package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableResumeCard
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableVacancyCard
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.HomePageContract
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

@Composable
fun ProfileScreen(viewModel: HomePageViewModel) {
    val state by viewModel.viewState
    val user by CurrentUser.info

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Title(text = "Мои резюме", fontSize = 30.sp)
        Title(text = "Вы вошли как ${user.username}", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))
        when (user.role) {
            UserRole.Worker -> {
                LaunchedEffect(Unit) {
                    viewModel.processIntent(HomePageContract.HomePageIntent.LoadProfileResumes)
                }

                when {
                    state.resumes == null -> LoadingComponent()
                    state.resumes!!.isEmpty() -> NoItemsCard()
                    else -> {
                        LazyColumn {
                            items(state.resumes!!) {
                                ClickableResumeCard(
                                    resume = it,
                                    onClick = {
                                        viewModel.processIntent(
                                            HomePageContract.HomePageIntent.OpenResumeDetails(it.id)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            UserRole.Employer -> {
                LaunchedEffect(Unit) {
                    viewModel.processIntent(HomePageContract.HomePageIntent.LoadProfileVacancies)
                }

                when {
                    state.vacancies == null -> LoadingComponent()
                    state.vacancies!!.isEmpty() -> NoItemsCard()
                    else -> {
                        LazyColumn {
                            items(state.vacancies!!) {
                                ClickableVacancyCard(
                                    vacancy = it,
                                    onClick = {
                                        viewModel.processIntent(
                                            HomePageContract.HomePageIntent.OpenVacancyDetails(it.id)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}