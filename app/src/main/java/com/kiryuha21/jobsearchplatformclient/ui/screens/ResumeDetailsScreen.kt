package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.ResumeDetails
import com.kiryuha21.jobsearchplatformclient.ui.components.StyledDefaultButton
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.HomePageViewModel

@Composable
fun ResumeDetailsScreen(
    editable: Boolean,
    resumeId: String?,
    viewModel: HomePageViewModel
) {
    if (resumeId == null) {
        throw Exception("vacancyId shouldn't be null!")
    }

    val state by viewModel.viewState

    when (state.isLoading) {
        true -> LoadingComponent()
        false -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ResumeDetails(resume = state.openedResume!!, modifier = Modifier.fillMaxWidth())
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