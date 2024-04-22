package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableVacancyCard
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.MainAppContract

@Composable
fun EmployerProfileScreen(
    state: MainAppContract.MainAppState,
    loadVacancies: () -> Unit,
    openVacancyDetails: (String) -> Unit,
    openVacancyEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = openVacancyEdit
            ) {
                Text(text = "Новая вакансия")
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Title(text = "Мои вакансии", fontSize = 30.sp)
            Title(text = "Вы вошли как ${CurrentUser.info.username}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(20.dp))

            LaunchedEffect(Unit) {
                loadVacancies()
            }

            when {
                state.vacancies == null -> LoadingComponent()
                state.vacancies.isEmpty() -> NoItemsCard()
                else -> {
                    LazyColumn {
                        items(state.vacancies) {
                            ClickableVacancyCard(
                                vacancy = it,
                                onClick = {
                                    openVacancyDetails(it.id)
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
