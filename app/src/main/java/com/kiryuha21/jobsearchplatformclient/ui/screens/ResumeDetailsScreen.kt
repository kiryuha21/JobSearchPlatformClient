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
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.ResumeDetails
import com.kiryuha21.jobsearchplatformclient.ui.components.StyledDefaultButton
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResumeDetailsContract

@Composable
fun ResumeDetailsScreen(
    editable: Boolean,
    onEdit: (Resume) -> Unit,
    onDelete: (Resume) -> Unit,
    state: ResumeDetailsContract.State
) {
    when (state.openedResume) {
        null -> LoadingComponent(modifier = Modifier.fillMaxSize())
        else -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ResumeDetails(resume = state.openedResume, modifier = Modifier.fillMaxWidth())
                if (editable) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        StyledDefaultButton(text = "Редактировать", onClick = { onEdit(state.openedResume) })
                        StyledDefaultButton(text = "Удалить", onClick = { onDelete(state.openedResume) })
                    }
                }
            }
        }
    }
}