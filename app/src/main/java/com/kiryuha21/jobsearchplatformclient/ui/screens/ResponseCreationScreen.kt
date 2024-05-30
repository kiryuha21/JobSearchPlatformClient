package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.ui.components.display.SelectResumePart
import com.kiryuha21.jobsearchplatformclient.ui.components.display.WriteMessagePart
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.contract.RESUMES_LOADING_TEXT
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResponseContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResponseStages

@Composable
fun ResponseCreationScreen(
    selectResume: (Resume) -> Unit,
    updateText: (String) -> Unit,
    setStage: (ResponseStages) -> Unit,
    saveResponse: () -> Unit,
    state: ResponseContract.State
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = "Создание отклика",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        if (state.isLoading) {
            LoadingComponent(
                modifier = Modifier.fillMaxSize(),
                description = state.loadingText
            )
        } else {
            AnimatedContent(
                targetState = state.stage,
                label = "Animated response creation",
                modifier = Modifier.fillMaxSize()
            ) {
                if (it == ResponseStages.ChooseResume) {
                    SelectResumePart(
                        onNext = { setStage(ResponseStages.WriteMessage) },
                        resumes = state.resumes,
                        selectedResume = state.selectedResume,
                        onSelect = selectResume
                    )
                } else {
                    WriteMessagePart(
                        title = "Напишите текст отклика(необязательно)",
                        onBack = { setStage(ResponseStages.ChooseResume) },
                        onSave = saveResponse,
                        text = state.message,
                        updateText = updateText
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResponseCreationScreenPreview() {
    ResponseCreationScreen(
        selectResume = {},
        updateText = {},
        saveResponse = {},
        setStage = {},
        state = ResponseContract.State(
            isLoading = false,
            loadingText = RESUMES_LOADING_TEXT,
            resumes = listOf(),
            selectedResume = Resume(),
            stage = ResponseStages.ChooseResume,
            message = ""
        )
    )
}