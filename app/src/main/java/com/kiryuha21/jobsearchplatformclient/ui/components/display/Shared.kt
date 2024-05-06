package com.kiryuha21.jobsearchplatformclient.ui.components.display

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.pagination.MoreItemsState
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
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