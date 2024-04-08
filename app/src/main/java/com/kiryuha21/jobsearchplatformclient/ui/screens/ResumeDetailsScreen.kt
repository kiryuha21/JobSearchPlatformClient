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
import com.kiryuha21.jobsearchplatformclient.ui.contract.MainAppContract

@Composable
fun ResumeDetailsScreen(
    editable: Boolean,
    resumeId: String?,
    onEdit: (Resume) -> Unit,
    onDelete: (Resume) -> Unit,
    state: MainAppContract.MainAppState
) {
    if (resumeId == null) {
        throw Exception("resumeId shouldn't be null!")
    }

    when (state.isLoading) {
        true -> LoadingComponent(modifier = Modifier.fillMaxSize())
        false -> {
            if (state.openedResume == null) {
                throw Exception("opened resume shouldn't be null by now")
            }

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