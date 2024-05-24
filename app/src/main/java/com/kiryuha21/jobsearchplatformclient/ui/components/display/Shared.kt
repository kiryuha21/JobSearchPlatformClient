package com.kiryuha21.jobsearchplatformclient.ui.components.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.MoreItemsState
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.TextBox
import com.kiryuha21.jobsearchplatformclient.ui.components.special.TrailingCard

@Composable
fun LastPaginationListItem(state: MoreItemsState, loadMore: () -> Unit) {
    when (state) {
        MoreItemsState.Available -> {
            LaunchedEffect(key1 = true) {
                loadMore()
            }
            LoadingComponent(
                modifier = Modifier.fillMaxWidth(),
                description = "Загружаем вакансии"
            )
        }
        MoreItemsState.Unavailable -> TrailingCard(
            text = "Вы просмотрели все вакансии",
            modifier = Modifier.padding(10.dp)
        )
        MoreItemsState.Unreachable -> TrailingCard(
            text = "Необходимо перезагрузить вакансии",
            modifier = Modifier.padding(10.dp)
        )
        MoreItemsState.Undefined -> TrailingCard(
            text = "Вы не должны это видеть :(",
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun WriteMessagePart(
    title: String,
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
            text = title,
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
                        contentDescription = "back to choosing"
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
                        contentDescription = "save job application"
                    )
                }
            }
        }
    }
}