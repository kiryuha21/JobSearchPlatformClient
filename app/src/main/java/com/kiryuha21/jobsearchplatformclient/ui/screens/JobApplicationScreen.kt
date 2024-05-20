package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.components.display.JobApplicationCard
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.special.HintCard
import com.kiryuha21.jobsearchplatformclient.ui.components.special.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.contract.JobApplicationContract
import com.kiryuha21.jobsearchplatformclient.util.PreviewObjects
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobApplicationScreen(
    state: JobApplicationContract.State,
    showVacancyDetails: (String) -> Unit,
    showResumeDetails: (String) -> Unit,
    markChecked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Предложения работы",
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        if (state.isLoading) {
            LoadingComponent(
                modifier = Modifier.fillMaxSize(),
                description = "Загружаем офферы..."
            )
        } else {
            val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
            val coroutineScope = rememberCoroutineScope()

            val receivedIndex = 0
            val sentIndex = 1

            Row(modifier = Modifier.fillMaxWidth()) {
                TabRow(selectedTabIndex = pagerState.currentPage) {
                    Tab(
                        selected = pagerState.currentPage == receivedIndex,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(receivedIndex)
                            }
                        },
                        text = { Text(text = "Полученные предложения") },
                    )
                    Tab(
                        selected = pagerState.currentPage == sentIndex,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(sentIndex)
                            }
                        },
                        text = { Text(text = "Отправленные предложения") },
                    )
                }
            }

            HorizontalPager(state = pagerState, userScrollEnabled = false) {
                val showedItems = if (it == receivedIndex) state.receivedApplications else state.sentApplications

                LazyColumn(
                    modifier = Modifier.padding(10.dp)
                ) {
                    items(
                        items = showedItems,
                        key = { jobApplication -> jobApplication.id }
                    ) { jobApplication ->
                        JobApplicationCard(
                            jobApplication = jobApplication,
                            isReceived = it == receivedIndex,
                            showResumeDetails = { id -> showResumeDetails(id) },
                            showVacancyDetails = { id -> showVacancyDetails(id) },
                            markChecked = { id -> markChecked(id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }

                    if (showedItems.isEmpty()) {
                        item {
                            NoItemsCard(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                            )
                        }
                    }

                    if (it == sentIndex) {
                        item {
                            HintCard(text = if (CurrentUser.info.role == UserRole.Worker) {
                                "Откликнуться на интересующую вакансию можно нажав на нее на главной странице"
                            } else {
                                "Отправить оффер на понравившееся резюме можно нажав на него на главной странице"
                            })
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerOffersScreenPreview() {
    JobApplicationScreen(
        state = JobApplicationContract.State(
            isLoading = false,
            receivedApplications = (1..10).map { PreviewObjects.randomJobApplication() },
            sentApplications = listOf(PreviewObjects.jobApplication2)
        ),
        showVacancyDetails = {},
        showResumeDetails = {},
        markChecked = {}
    )
}