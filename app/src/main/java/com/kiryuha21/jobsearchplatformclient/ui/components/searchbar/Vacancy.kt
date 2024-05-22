package com.kiryuha21.jobsearchplatformclient.ui.components.searchbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.ValidatedTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.special.DefaultDatePickerDialog
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
            Text(text = "Фильтры", fontSize = 18.sp, fontWeight = FontWeight(700), modifier = Modifier.padding(bottom = 10.dp))

            var minSalaryText by remember { mutableStateOf(vacancyFilters.minSalary?.toString() ?: "") }
            ValidatedTextField(
                text = minSalaryText,
                placeholder = "Минимальная зарплата",
                onUpdate = { text, valid ->
                    minSalaryText = text
                    if (valid) {
                        vacancyFilters = vacancyFilters.copy(minSalary = if (text.isEmpty()) null else text.toInt())
                    }
                },
                isValid = { it.isValidNullableNum() },
                errorMessage = "Введите корректное число",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            )

            var maxSalaryText by remember { mutableStateOf(vacancyFilters.maxSalary?.toString() ?: "") }
            ValidatedTextField(
                text = maxSalaryText,
                placeholder = "Максимальная зарплата",
                onUpdate = { text, valid ->
                    maxSalaryText = text
                    if (valid) {
                        vacancyFilters = vacancyFilters.copy(maxSalary = if (text.isEmpty()) null else text.toInt())
                    }
                },
                isValid = { it.isValidNullableNum() },
                errorMessage = "Введите корректное число",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

            TextButton(onClick = { visibleSkillForm = !visibleSkillForm }) {
                Text(text = "+ нужный навык", fontSize = 20.sp)
            }

            AnimatedVisibility(visible = visibleSkillForm) {
                SkillForm(
                    skillLevelText = "Уровень навыка:",
                    onSubmit = {
                        vacancyFilters = vacancyFilters.copy(requiredSkills =
                        vacancyFilters.requiredSkills?.let { skills -> skills + it } ?: listOf(it)
                        )
                        visibleSkillForm = !visibleSkillForm
                    },
                    onCancel = { visibleSkillForm = !visibleSkillForm }
                )
            }

            TextButton(onClick = { visibleWorkExperienceForm = !visibleWorkExperienceForm }) {
                Text(text = "+ нужный опыт работы", fontSize = 20.sp)
            }

            AnimatedVisibility(visible = visibleWorkExperienceForm) {
                VacancyWorkExperienceForm(
                    positionLevelText = "Нужный уровень позиции",
                    positionText = "Нужная позиция",
                    monthsText = "Нужное количество месяцев",
                    onSubmit = {
                        vacancyFilters = vacancyFilters.copy(
                            requiredWorkExperience = vacancyFilters.requiredWorkExperience?.let { exp -> exp + it } ?: listOf(it)
                        )
                        visibleWorkExperienceForm = !visibleWorkExperienceForm
                    },
                    onCancel = { visibleWorkExperienceForm = !visibleWorkExperienceForm }
                )
            }

            var datePickerShown by remember { mutableStateOf(false) }
            TextButton(onClick = { datePickerShown = !datePickerShown }) {
                Text(text = "+ Самый поздний срок публикации", fontSize = 20.sp)
            }

            if (datePickerShown) {
                DefaultDatePickerDialog(
                    initTime = vacancyFilters.placedAt,
                    onDismiss = { datePickerShown = !datePickerShown },
                    onConfirm = {
                        vacancyFilters = vacancyFilters.copy(placedAt = it)
                        datePickerShown = !datePickerShown
                    }
                )
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