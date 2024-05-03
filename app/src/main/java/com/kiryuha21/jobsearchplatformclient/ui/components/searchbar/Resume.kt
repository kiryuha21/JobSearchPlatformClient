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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.ClickableSkillsList
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.ClickableWorkExperienceList
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.SkillForm
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.VacancyWorkExperienceForm
import com.kiryuha21.jobsearchplatformclient.ui.components.special.DefaultDatePickerDialog
import com.kiryuha21.jobsearchplatformclient.util.isValidNullableAge

val resumeFilterSortOptions = listOf(
    FilterSortOption(FilterName.Resume.PLACED_AT, "Дата публикации"),
    FilterSortOption(FilterName.Resume.AGE, "Возраст"),
    FilterSortOption(FilterName.Resume.APPLY_POSITION, "Позиция"),
    FilterSortOption(FilterName.Resume.IS_IMAGE_SET, "Есть фото")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeSearchBar(
    initFilters: ResumeFilters,
    onSearch: (ResumeFilters) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentQuery by remember { mutableStateOf("") }
    var bodyVisible by remember { mutableStateOf(false) }
    var resumeFilters by remember { mutableStateOf(initFilters) }

    SearchBar(
        query = currentQuery,
        onQueryChange = {
            currentQuery = it
            resumeFilters = resumeFilters.copy(applyPosition = it)
        },
        onSearch = {
            onSearch(resumeFilters)
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
                FilterSortChooser(resumeFilters.pageRequestFilter, resumeFilterSortOptions) {
                    resumeFilters = resumeFilters.copy(pageRequestFilter = it)
                    sortingVisible = false
                }
            }

            HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Text(text = "Фильтры", fontSize = 18.sp, fontWeight = FontWeight(700), modifier = Modifier.padding(bottom = 10.dp))

            val focusManager = LocalFocusManager.current

            var minAgeText by remember { mutableStateOf(resumeFilters.olderThan?.toString() ?: "") }
            var minAgeSupportingText by remember {
                mutableStateOf(if ((resumeFilters.olderThan?.toString() ?: "").isValidNullableAge()) "" else "Введите корректный возраст")
            }

            OutlinedTextField(
                value = minAgeText,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Минимальный возраст") },
                placeholder = { Text(text = "Минимальный возраст") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = minAgeSupportingText.isNotEmpty(),
                supportingText = { Text(text = minAgeSupportingText, color = Color.Red) },
                onValueChange = {
                    minAgeText = it

                    if (it.isValidNullableAge()) {
                        minAgeSupportingText = ""
                        resumeFilters = resumeFilters.copy(olderThan = if (it.isEmpty()) null else it.toInt())
                    } else {
                        minAgeSupportingText = "Введите корректный возраст"
                    }
                }
            )

            var maxAgeText by remember { mutableStateOf(resumeFilters.youngerThan?.toString() ?: "") }
            var maxAgeSupportingText by remember {
                mutableStateOf(if ((resumeFilters.youngerThan?.toString() ?: "").isValidNullableAge()) "" else "Введите корректный возраст")
            }

            OutlinedTextField(
                value = maxAgeText,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Максимальный возраст") },
                placeholder = { Text(text = "Максимальный возраст") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = maxAgeSupportingText.isNotEmpty(),
                supportingText = { Text(text = maxAgeSupportingText, color = Color.Red) },
                onValueChange = {
                    maxAgeText = it

                    if (it.isValidNullableAge()) {
                        maxAgeSupportingText = ""
                        resumeFilters = resumeFilters.copy(youngerThan = if (it.isEmpty()) null else it.toInt())
                    } else {
                        maxAgeSupportingText = "Введите корректный возраст"
                    }
                }
            )

            HorizontalDivider()

            resumeFilters.skills?.let {
                ClickableSkillsList(
                    description = "Навыки:",
                    skills = it,
                    imageVector = Icons.Default.Delete
                ) {
                    resumeFilters = resumeFilters.copy(skills = resumeFilters.skills?.filterIndexed { index, _ -> it != index })
                }
            }

            AnimatedVisibility(visible = visibleSkillForm) {
                SkillForm(
                    skillLevelText = "Нужный навык",
                    onSubmit = {
                        resumeFilters = resumeFilters.copy(skills = resumeFilters.skills?.let { skills -> skills + it } ?: listOf(it))
                        visibleSkillForm = !visibleSkillForm
                    },
                    onCancel = { visibleSkillForm = !visibleSkillForm }
                )
            }

            TextButton(onClick = { visibleSkillForm = !visibleSkillForm }) {
                Text(text = "+ нужный навык", fontSize = 20.sp)
            }

            resumeFilters.workExperience?.let {
                ClickableWorkExperienceList(
                    description = "Нужный опыт работы:",
                    workExperience = it,
                    imageVector = Icons.Default.Delete,
                    isRequirementView = true
                ) {
                    resumeFilters = resumeFilters.copy(workExperience = resumeFilters.workExperience?.filterIndexed { index, _ -> it != index })
                }
            }

            AnimatedVisibility(visible = visibleWorkExperienceForm) {
                VacancyWorkExperienceForm(
                    onSubmit = {
                        resumeFilters = resumeFilters.copy(
                            workExperience = resumeFilters.workExperience?.let { exp -> exp + it } ?: listOf(it)
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
                DefaultDatePickerDialog(
                    initTime = resumeFilters.placedAt,
                    onDismiss = { datePickerShown = !datePickerShown },
                    onConfirm = {
                        resumeFilters = resumeFilters.copy(placedAt = it)
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
                        resumeFilters = ResumeFilters()
                        minAgeText = ""
                        maxAgeText = ""
                    }
                ) {
                    Text(text = "Сбросить фильтры")
                }

                Button(
                    onClick = {
                        onSearch(resumeFilters)
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
fun ResumeSearchBarPreview() {
    ResumeSearchBar(initFilters = ResumeFilters(), onSearch = {})
}