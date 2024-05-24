package com.kiryuha21.jobsearchplatformclient.ui.components.display

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.TextBox

@Composable
fun SelectableVacanciesList(
    vacancies: List<Vacancy>,
    onSelect: (Vacancy) -> Unit,
    selectedId: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = vacancies,
            key = { it.id }
        ) { vacancy ->
            ClickableVacancyCard(
                vacancy = vacancy,
                isStatusShown = false,
                onClick = { onSelect(vacancy) },
                isChosen = vacancy.id == selectedId,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SelectVacancyPart(
    onNext: () -> Unit,
    vacancies: List<Vacancy>,
    selectedVacancy: Vacancy,
    onSelect: (Vacancy) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Выберите вакансию для оффера",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
        )

        AnimatedVisibility(visible = selectedVacancy.id.isNotEmpty()) {
            Text(
                text = "Выбранная вакансия: ${selectedVacancy.title}",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        SelectableVacanciesList(
            vacancies = vacancies,
            onSelect = onSelect,
            selectedId = selectedVacancy.id,
            modifier = Modifier.fillMaxHeight(0.9f)
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = onNext,
                enabled = selectedVacancy.id.isNotEmpty()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Далее")
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = "to offer message"
                    )
                }
            }
        }
    }
}

@Composable
fun WriteMessagePart(
    onBack: () -> Unit,
    onSave: () -> Unit,
    text: String,
    updateText: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(0.9f)
    ) {
        Text(
            text = "Напишите текст оффера(необязательно)",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        TextBox(
            placeholder = "Введите текст",
            initString = text,
            onUpdate = updateText,
            minLines = 10,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = onBack,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back to vacancies"
                    )
                    Text(text = "Назад")
                }
            }

            Button(
                onClick = onSave,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Отправить")
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "save offer"
                    )
                }
            }
        }
    }
}