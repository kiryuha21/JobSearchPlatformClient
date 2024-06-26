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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ResumeDetails
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResumeDetailsContract

@Composable
fun ResumeDetailsScreen(
    onEdit: (String) -> Unit,
    onDelete: () -> Unit,
    onCreateOffer: (String) -> Unit,
    state: ResumeDetailsContract.State
) {
    when {
        state.openedResume == null || state.isLoadingResume -> {
            LoadingComponent(modifier = Modifier.fillMaxSize(), description = state.loadingText)
        }
        else -> {
            val isOwner = state.openedResume.workerUsername == CurrentUser.info.username
            val isEmployer = CurrentUser.info.role == UserRole.Employer

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ResumeDetails(
                    resume = state.openedResume,
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
                        Button(onClick = { onEdit(state.openedResume.id) }) {
                            Text(text = "Редактировать")
                        }
                        Button(onClick = { onDelete() }) {
                            Text(text = "Удалить")
                        }
                    }
                } else if (isEmployer) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onCreateOffer(state.openedResume.id) },
                            modifier = Modifier.fillMaxWidth(0.7f),
                            shape = RoundedCornerShape(10)
                        ) {
                            Text(text = "Отправить оффер")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}