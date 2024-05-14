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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ClickableVacancyCard
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.special.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.EmployerProfileContract

@Composable
fun EmployerProfileScreen(
    state: EmployerProfileContract.State,
    loadVacancies: () -> Unit,
    openVacancyDetails: (String) -> Unit,
    openVacancyEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        loadVacancies()
    }

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

            when {
                state.vacancies == null -> LoadingComponent(description = "Загрузка профиля...")
                state.vacancies.isEmpty() -> NoItemsCard()
                else -> {
                    LazyColumn {
                        items(state.vacancies) {
                            ClickableVacancyCard(
                                vacancy = it,
                                isStatusShown = true,
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
