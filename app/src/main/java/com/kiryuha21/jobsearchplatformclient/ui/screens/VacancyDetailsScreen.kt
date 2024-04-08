package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.StyledDefaultButton
import com.kiryuha21.jobsearchplatformclient.ui.components.VacancyDetails
import com.kiryuha21.jobsearchplatformclient.ui.contract.MainAppContract

@Composable
fun VacancyDetailsScreen(
    editable: Boolean,
    vacancyId: String?,
    state: MainAppContract.MainAppState
) {
    if (vacancyId == null) {
        throw Exception("vacancyId shouldn't be null!")
    }

    when (state.isLoading) {
        true -> LoadingComponent(modifier = Modifier.fillMaxSize())
        false -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                VacancyDetails(vacancy = state.openedVacancy!!, modifier = Modifier.fillMaxWidth())
                if (editable) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        StyledDefaultButton(text = "Редактировать", onClick = { /*TODO*/ })
                        StyledDefaultButton(text = "Удалить", onClick = { /*TODO*/ })
                    }
                }
            }
        }
    }
}