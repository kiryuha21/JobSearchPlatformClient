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
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.ui.components.special.HintCard

@Composable
fun SelectableResumesList(
    resumes: List<Resume>,
    onSelect: (Resume) -> Unit,
    selectedId: String,
    modifier: Modifier = Modifier,
    noResumesText: String? = null
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = resumes,
            key = { it.id }
        ) { resume ->
            ClickableResumeCard(
                resume = resume,
                isStatusShown = false,
                onClick = { onSelect(resume) },
                isChosen = resume.id == selectedId,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (resumes.isEmpty() && !noResumesText.isNullOrBlank()) {
            item {
                HintCard(
                    text = noResumesText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SelectResumePart(
    onNext: () -> Unit,
    resumes: List<Resume>,
    selectedResume: Resume,
    onSelect: (Resume) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Выберите резюме для отклика",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
        )

        AnimatedVisibility(visible = selectedResume.id.isNotEmpty()) {
            Text(
                text = "Выбранное резюме: ${selectedResume.fullName()}, ${selectedResume.applyPosition}",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        SelectableResumesList(
            resumes = resumes,
            onSelect = onSelect,
            selectedId = selectedResume.id,
            noResumesText = "Чтобы отправить отклик, сначала нужно создать хотя бы одно публичное резюме в профиле",
            modifier = Modifier.fillMaxHeight(0.9f)
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = onNext,
                enabled = selectedResume.id.isNotEmpty()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Далее")
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = "to response message"
                    )
                }
            }
        }
    }
}