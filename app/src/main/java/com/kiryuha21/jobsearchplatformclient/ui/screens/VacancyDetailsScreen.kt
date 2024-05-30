package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.components.display.VacancyDetails
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.contract.VacancyDetailsContract

@Composable
fun VacancyDetailsScreen(
    onEdit: (Vacancy) -> Unit,
    onDelete: (Vacancy) -> Unit,
    onCreateResponse: (String) -> Unit,
    state: VacancyDetailsContract.State
) {
    when {
        state.openedVacancy == null || state.isLoadingVacancy -> {
            LoadingComponent(modifier = Modifier.fillMaxSize(), description = state.loadingText)
        }
        else -> {
            val isOwner = state.openedVacancy.employerUsername == CurrentUser.info.username
            val isWorker = CurrentUser.info.role == UserRole.Worker

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                VacancyDetails(
                    vacancy = state.openedVacancy,
                    isStatusShown = isOwner,
                    modifier = Modifier.fillMaxWidth()
                )

                if (isOwner) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        OutlinedButton(onClick = { onEdit(state.openedVacancy) }) {
                            Text(text = "Редактировать")
                        }
                        OutlinedButton(onClick = { onDelete(state.openedVacancy) }) {
                            Text(text = "Удалить")
                        }
                    }
                } else if (isWorker) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onCreateResponse(state.openedVacancy.id) },
                            modifier = Modifier.fillMaxWidth(0.7f),
                            shape = RoundedCornerShape(10)
                        ) {
                            Text(text = "Откликнуться на вакансию")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}