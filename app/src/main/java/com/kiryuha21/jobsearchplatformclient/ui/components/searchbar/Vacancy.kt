package com.kiryuha21.jobsearchplatformclient.ui.components.searchbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.FilterName
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.FilterSortOption
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.ClickableSkillsList
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.ClickableWorkExperienceList
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.SkillForm
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.VacancyWorkExperienceForm
import com.kiryuha21.jobsearchplatformclient.util.isValidNullableNum

val VacancyFilterSortOptions = listOf(
    FilterSortOption(FilterName.Vacancy.PLACED_AT, "Дата публикации"),
    FilterSortOption(FilterName.Vacancy.TITLE, "Название"),
    FilterSortOption(FilterName.Vacancy.DESCRIPTION, "Описание"),
    FilterSortOption(FilterName.Vacancy.MAX_SALARY, "Минимальная зарплата"),
    FilterSortOption(FilterName.Vacancy.MIN_SALARY, "Максимальная зарплата")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancySearchBar(
    initFilters: VacancyFilters,
    onSearch: (VacancyFilters) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentQuery by remember { mutableStateOf("") }
    var bodyVisible by remember { mutableStateOf(false) }
    var vacancyFilters by remember { mutableStateOf(initFilters) }

    SearchBar(
        query = currentQuery,
        onQueryChange = {
            currentQuery = it
            vacancyFilters = vacancyFilters.copy(query = it)
        },
        onSearch = {
            onSearch(vacancyFilters)
            bodyVisible = !bodyVisible
        },
        active = bodyVisible,
        onActiveChange = { bodyVisible = !bodyVisible },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search")
        },
        trailingIcon = {
            if (bodyVisible) {
                IconButton(
                    onClick = {
                        if (currentQuery.isEmpty()) {
                            bodyVisible = false
                        } else {
                            currentQuery = ""
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
                }
            }
        },
        modifier = modifier
    ) {
        var sortingVisible by remember { mutableStateOf(false) }
        var visibleWorkExperienceForm by remember { mutableStateOf(false) }
        var visibleSkillForm by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TextButton(
                onClick = { sortingVisible = !sortingVisible },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.FilterAlt, contentDescription = "filtering")
                    Text(
                        text = "Отсортировать по параметру",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(700),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }

            AnimatedVisibility(visible = sortingVisible) {
                FilterSortChooser(vacancyFilters.pageRequestFilter, VacancyFilterSortOptions) {
                    vacancyFilters = vacancyFilters.copy(pageRequestFilter = it)
                    sortingVisible = false
                }
            }

            HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Text(text = "Фильтры", fontSize = 18.sp, fontWeight = FontWeight(700))

            val focusManager = LocalFocusManager.current

            var minSalaryText by remember { mutableStateOf(vacancyFilters.minSalary?.toString() ?: "") }
            var minSalarySupportingText by remember {
                mutableStateOf(if ((vacancyFilters.minSalary?.toString() ?: "").isValidNullableNum()) "" else "Введите корректное число")
            }

            OutlinedTextField(
                value = minSalaryText,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Минимальная зарплата") },
                placeholder = { Text(text = "Минимальная зарплата") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = minSalarySupportingText.isNotEmpty(),
                supportingText = { Text(text = minSalarySupportingText, color = Color.Red) },
                onValueChange = {
                    minSalaryText = it

                    if (it.isValidNullableNum()) {
                        minSalarySupportingText = ""
                        vacancyFilters = vacancyFilters.copy(minSalary = if (it.isEmpty()) null else it.toInt())
                    } else {
                        minSalarySupportingText = "Введите корректное число"
                    }
                }
            )

            var maxSalaryText by remember { mutableStateOf(vacancyFilters.maxSalary?.toString() ?: "") }
            var maxSalarySupportingText by remember {
                mutableStateOf(if ((vacancyFilters.maxSalary?.toString() ?: "").isValidNullableNum()) "" else "Введите корректное число")
            }

            OutlinedTextField(
                value = maxSalaryText,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Максимальная зарплата") },
                placeholder = { Text(text = "Максимальная зарплата") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = maxSalarySupportingText.isNotEmpty(),
                supportingText = { Text(text = maxSalarySupportingText, color = Color.Red) },
                onValueChange = {
                    maxSalaryText = it

                    if (it.isValidNullableNum()) {
                        maxSalarySupportingText = ""
                        vacancyFilters = vacancyFilters.copy(maxSalary = if (it.isEmpty()) null else it.toInt())
                    } else {
                        maxSalarySupportingText = "Введите корректное число"
                    }
                }
            )

            HorizontalDivider()

            vacancyFilters.requiredSkills?.let {
                ClickableSkillsList(
                    description = "Нужные навыки:",
                    skills = it,
                    imageVector = Icons.Default.Delete
                ) {
                    vacancyFilters = vacancyFilters.copy(requiredSkills =
                    vacancyFilters.requiredSkills?.filterIndexed { index, _ -> it != index }
                    )
                }
            }

            AnimatedVisibility(visible = visibleSkillForm) {
                SkillForm(
                    skillLevelText = "Нужный навык",
                    onSubmit = {
                        vacancyFilters = vacancyFilters.copy(requiredSkills =
                        vacancyFilters.requiredSkills?.let { skills -> skills + it } ?: listOf(it)
                        )
                        visibleSkillForm = !visibleSkillForm
                    },
                    onCancel = { visibleSkillForm = !visibleSkillForm }
                )
            }

            TextButton(onClick = { visibleSkillForm = !visibleSkillForm }) {
                Text(text = "+ нужный навык", fontSize = 20.sp)
            }

            vacancyFilters.requiredWorkExperience?.let {
                ClickableWorkExperienceList(
                    description = "Нужный опыт работы:",
                    workExperience = it,
                    imageVector = Icons.Default.Delete,
                    isRequirementView = true
                ) {
                    vacancyFilters = vacancyFilters.copy(requiredWorkExperience =
                    vacancyFilters.requiredWorkExperience?.filterIndexed { index, _ -> it != index }
                    )
                }
            }

            AnimatedVisibility(visible = visibleWorkExperienceForm) {
                VacancyWorkExperienceForm(
                    onSubmit = {
                        vacancyFilters = vacancyFilters.copy(
                            requiredWorkExperience = vacancyFilters.requiredWorkExperience?.let { exp -> exp + it } ?: listOf(it)
                        )
                        visibleWorkExperienceForm = !visibleWorkExperienceForm
                    },
                    onCancel = { visibleWorkExperienceForm = !visibleWorkExperienceForm }
                )
            }

            TextButton(onClick = { visibleWorkExperienceForm = !visibleWorkExperienceForm }) {
                Text(text = "+ нужный опыт работы", fontSize = 20.sp)
            }

            var datePickerShown by remember { mutableStateOf(false) }
            TextButton(onClick = { datePickerShown = !datePickerShown }) {
                Text(text = "+ минимальный срок публикации", fontSize = 20.sp)
            }

            if (datePickerShown) {
                val state = rememberDatePickerState(initialSelectedDateMillis = vacancyFilters.placedAt)

                DatePickerDialog(
                    onDismissRequest = { datePickerShown = !datePickerShown },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    vacancyFilters = vacancyFilters.copy(placedAt = state.selectedDateMillis)
                                    datePickerShown = !datePickerShown
                                }
                            ) {
                                Text(text = "Выбрать")
                            }
                        }
                    }
                ) {
                    DatePicker(state = state)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        vacancyFilters = VacancyFilters()
                        minSalaryText = ""
                        maxSalaryText = ""
                    }
                ) {
                    Text(text = "Сбросить фильтры")
                }

                Button(
                    onClick = {
                        onSearch(vacancyFilters)
                        bodyVisible = !bodyVisible
                    }
                ) {
                    Text(text = "Искать с фильтрами")
                }
            }
        }
    }
}

@Preview
@Composable
fun VacancySearchBarPreview() {
    VacancySearchBar(initFilters = VacancyFilters(), onSearch = {})
}