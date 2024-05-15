package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.JobApplication
import com.kiryuha21.jobsearchplatformclient.ui.components.display.JobApplicationCard
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.special.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerOffersContract

@Composable
fun WorkerOffersScreen(
    state: WorkerOffersContract.State,
    showVacancyDetails: (String) -> Unit,
    markChecked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Полученные офферы",
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        when {
            state.isLoading -> LoadingComponent(
                modifier = Modifier.fillMaxSize(),
                description = "Загружаем офферы..."
            )

            state.jobApplications.isEmpty() -> NoItemsCard(
                modifier = Modifier.fillMaxWidth()
            )

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(10.dp)
                ) {
                    items(state.jobApplications) {jobApplication ->
                        JobApplicationCard(
                            jobApplication = jobApplication,
                            showVacancyDetails = { showVacancyDetails(it) },
                            markChecked = { markChecked(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerOffersScreenPreview() {
    WorkerOffersScreen(
        state = WorkerOffersContract.State(
            isLoading = false,
            jobApplications = listOf(JobApplication(
                id = "0",
                senderUsername = "Важнич",
                referenceResumeId = "",
                referenceVacancyId = "",
                message = "Мега ждем вас",
                seen = false
            ))
        ),
        showVacancyDetails = {},
        markChecked = {}
    )
}