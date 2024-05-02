package com.kiryuha21.jobsearchplatformclient.ui.components.searchbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.FilterSortOption
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.PageRequestFilter
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.SortingDirection

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSortChooser(
    initPageRequestFilter: PageRequestFilter,
    options: List<FilterSortOption>,
    onChoose: (PageRequestFilter) -> Unit
) {
    var selectedItemIndex by remember {
        mutableIntStateOf(VacancyFilterSortOptions.indexOfFirst { it.description == initPageRequestFilter.sortProperty })
    }
    var ascSortingDirection by remember {
        mutableStateOf(initPageRequestFilter.sortingDirection == SortingDirection.ASC)
    }

    Column {
        FlowRow {
            options.forEachIndexed { index, option ->
                val selected = index == selectedItemIndex

                Button(
                    onClick = {
                        ascSortingDirection = if (selected) {
                            !ascSortingDirection
                        } else {
                            true
                        }
                        selectedItemIndex = index
                    },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = if (selected) Color.Unspecified else Color.White,
                        contentColor = if (selected) Color.Unspecified else Color.Black
                    ),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(text = option.visibleName)
                    if (selected) {
                        Icon(
                            imageVector = Icons.Default.ArrowOutward,
                            contentDescription = "sort direction",
                            modifier = Modifier.rotate(if (ascSortingDirection) 0f else 90f)
                        )
                    }
                }
            }
        }

        TextButton(
            onClick = {
                onChoose(
                    PageRequestFilter(
                    sortingDirection = if (ascSortingDirection) SortingDirection.ASC else SortingDirection.DESC,
                    sortProperty = VacancyFilterSortOptions[selectedItemIndex].description
                )
                )
            },
            modifier = Modifier.padding(top = 5.dp)
        ) {
            Text(text = "Применить сортировку", fontSize = 18.sp)
        }
    }
}