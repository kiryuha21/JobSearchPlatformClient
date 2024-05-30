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
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.components.display.SelectVacancyPart
import com.kiryuha21.jobsearchplatformclient.ui.components.display.WriteMessagePart
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.contract.OfferContract
import com.kiryuha21.jobsearchplatformclient.ui.contract.OfferStages
import com.kiryuha21.jobsearchplatformclient.ui.contract.VACANCIES_LOADING_TEXT

@Composable
fun OfferCreationScreen(
    selectVacancy: (Vacancy) -> Unit,
    updateText: (String) -> Unit,
    setStage: (OfferStages) -> Unit,
    saveOffer: () -> Unit,
    state: OfferContract.State
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = "Создание оффера",
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
                label = "Animated offer creation",
                modifier = Modifier.fillMaxSize()
            ) {
                if (it == OfferStages.ChooseVacancy) {
                    SelectVacancyPart(
                        onNext = { setStage(OfferStages.WriteMessage) },
                        vacancies = state.vacancies,
                        selectedVacancy = state.selectedVacancy,
                        onSelect = selectVacancy
                    )
                } else {
                    WriteMessagePart(
                        title = "Напишите текст оффера(необязательно)",
                        onBack = { setStage(OfferStages.ChooseVacancy) },
                        onSave = saveOffer,
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
fun OfferCreationScreenPreview() {
    OfferCreationScreen(
        selectVacancy = {},
        updateText = {},
        saveOffer = {},
        setStage = {},
        state = OfferContract.State(
            isLoading = false,
            loadingText = VACANCIES_LOADING_TEXT,
            vacancies = listOf(),
            selectedVacancy = Vacancy(),
            stage = OfferStages.ChooseVacancy,
            message = ""
        )
    )
}