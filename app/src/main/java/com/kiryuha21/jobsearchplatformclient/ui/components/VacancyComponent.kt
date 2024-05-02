package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PositionLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.FilterName
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.FilterSortOption
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.PageRequestFilter
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.SortingDirection
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.VacancyFilters
import com.kiryuha21.jobsearchplatformclient.util.isNumeric
import com.valentinilk.shimmer.shimmer
import java.util.Date

@Composable
fun VacancyDetails(vacancy: Vacancy, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = vacancy.title)
        }
        Text(text = vacancy.company.name)
        Text(text = "${vacancy.minSalary} - ${vacancy.maxSalary}")
        Text(text = vacancy.description)
    }
}

@Composable
fun ClickableVacancyCard(vacancy: Vacancy, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        modifier = modifier.padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = vacancy.title,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                Text(text = vacancy.company.name, modifier = Modifier.padding(end = 20.dp))

                val salaryText =
                    if (vacancy.minSalary == vacancy.maxSalary) "${vacancy.minSalary}"
                    else "От ${vacancy.minSalary} до ${vacancy.maxSalary}"
                Text(text = salaryText, fontFamily = FontFamily.Cursive)
            }

            if (vacancy.imageUrl == null) {
                Icon(
                    imageVector = Icons.Rounded.Work,
                    contentDescription = "work",
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )
            } else {
                AsyncImage(
                    model = vacancy.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

@Composable
fun VacancyEditForm(
    vacancy: Vacancy,
    comboBoxItems: List<ComboBoxItem>,
    onTitleUpdate: (String, Boolean) -> Unit,
    onCompanyNameUpdate: (String, Boolean) -> Unit,
    onMinSalaryUpdate: (String, Boolean) -> Unit,
    onMaxSalaryUpdate: (String, Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Статус Вакансии")
            ComboBox(items = comboBoxItems)
        }
        ValidateableTextField(
            placeholder = "Название вакансии",
            initString = vacancy.title,
            isValid = { it.isNotBlank() },
            errorMessage = "Название вакансии не может быть пустым",
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Abc,
            onUpdate = onTitleUpdate
        )
        ValidateableTextField(
            placeholder = "Название компании",
            initString = vacancy.company.name,
            isValid = { it.isNotBlank() },
            errorMessage = "Название компании не может быть пустым",
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Abc,
            onUpdate = onCompanyNameUpdate
        )

        HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

        Text(
            text = "Ожидаемая зарплата",
            fontSize = 16.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                ValidateableTextField(
                    placeholder = "Минимум, ₽",
                    initString = vacancy.minSalary.toString(),
                    isValid = { it.isNotBlank() && it.isNumeric() },
                    errorMessage = "Некорректное число",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onUpdate = onMinSalaryUpdate
                )
            }

            Text(
                text = Typography.lessOrEqual.toString(),
                fontSize = 28.sp,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                ValidateableTextField(
                    placeholder = "Максимум, ₽",
                    initString = vacancy.maxSalary.toString(),
                    isValid = { it.isNotBlank() && it.isNumeric() },
                    errorMessage = "Некорректное число",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onUpdate = onMaxSalaryUpdate
                )
            }
        }
    }
}

@Composable
fun VacancyWorkExperienceForm(
    onSubmit: (WorkExperience) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var validMonths by remember { mutableStateOf(false) }
    var validPosition by remember { mutableStateOf(false) }

    var workExperience by remember {
        mutableStateOf(WorkExperience(Company(""), "", PositionLevel.Junior, 0, 0))
    }
    val comboBoxItems = listOf(
        ComboBoxItem("Джуниор") {
            workExperience = workExperience.copy(positionLevel = PositionLevel.Junior)
        },
        ComboBoxItem("Мидл") {
            workExperience = workExperience.copy(positionLevel = PositionLevel.Middle)
        },
        ComboBoxItem("Сеньор") {
            workExperience = workExperience.copy(positionLevel = PositionLevel.Senior)
        },
        ComboBoxItem("Лид") {
            workExperience = workExperience.copy(positionLevel = PositionLevel.Lead)
        },
    )

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Минимальный уровень позиции")
            ComboBox(items = comboBoxItems)
        }

        ValidateableTextField(
            icon = Icons.Default.Abc,
            placeholder = "Желаемая Позиция",
            initString = "",
            onUpdate = { value, valid ->
                if (valid) {
                    workExperience = workExperience.copy(position = value)
                }
                validPosition = valid
            },
            isValid = { it.isNotBlank() },
            errorMessage = "Позиция не может быть пустой",
            modifier = Modifier.fillMaxWidth()
        )

        ValidateableTextField(
            placeholder = "Минимум месяцев проработано",
            initString = "",
            onUpdate = { text, valid ->
                if (valid) {
                    workExperience = workExperience.copy(months = text.toInt())
                }
                validMonths = valid
            },
            isValid = { it.isNotEmpty() && it.isNumeric() },
            errorMessage = "Введите корректное число",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            val valid = validPosition && validMonths

            SecuredButton(
                text = "Сохранить",
                onClick = { onSubmit(workExperience) },
                enabled = valid
            )
            DefaultButton(text = "Отменить", onClick = onCancel)
        }
    }
}

val filterSortOptions = listOf(
    FilterSortOption(FilterName.Vacancy.PLACED_AT, "Дата публикации"),
    FilterSortOption(FilterName.Vacancy.TITLE, "Название"),
    FilterSortOption(FilterName.Vacancy.DESCRIPTION, "Описание"),
    FilterSortOption(FilterName.Vacancy.MAX_SALARY, "Минимальная зарплата"),
    FilterSortOption(FilterName.Vacancy.MIN_SALARY, "Максимальная зарплата")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSortChooser(
    initPageRequestFilter: PageRequestFilter,
    onChoose: (PageRequestFilter) -> Unit
) {
    var selectedItemIndex by remember {
        mutableIntStateOf(filterSortOptions.indexOfFirst { it.description == initPageRequestFilter.sortProperty })
    }
    var ascSortingDirection by remember {
        mutableStateOf(initPageRequestFilter.sortingDirection == SortingDirection.ASC)
    }

    Column {
        FlowRow {
            filterSortOptions.forEachIndexed { index, option ->
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
                onChoose(PageRequestFilter(
                    sortingDirection = if (ascSortingDirection) SortingDirection.ASC else SortingDirection.DESC,
                    sortProperty = filterSortOptions[selectedItemIndex].description
                ))
            },
            modifier = Modifier.padding(top = 5.dp)
        ) {
            Text(text = "Применить сортировку", fontSize = 18.sp)
        }
    }

}

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
            modifier = Modifier.padding(10.dp)
        ) {
            TextButton(
                onClick = { sortingVisible = !sortingVisible },
                modifier = Modifier.fillMaxWidth()
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
                FilterSortChooser(vacancyFilters.pageRequestFilter) {
                    vacancyFilters = vacancyFilters.copy(pageRequestFilter = it)
                    sortingVisible = false
                }
            }

            HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Text(text = "Фильтры", fontSize = 18.sp, fontWeight = FontWeight(700))

            ValidateableTextField(
                placeholder = "Минимальная зарплата (₽)",
                initString = vacancyFilters.minSalary?.toString() ?: "",
                onUpdate = { text, valid ->
                    if (valid) {
                        vacancyFilters = vacancyFilters.copy(minSalary = if (text.isEmpty()) null else text.toInt())
                    }
                },
                isValid = { it.isEmpty() || it.isNumeric() },
                errorMessage = "Введите корректное число",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            ValidateableTextField(
                placeholder = "Максимальная зарплата (₽)",
                initString = vacancyFilters.maxSalary?.toString() ?: "",
                onUpdate = { text, valid ->
                    if (valid) {
                        vacancyFilters = vacancyFilters.copy(maxSalary = if (text.isEmpty()) null else text.toInt())
                    }
                },
                isValid = { it.isEmpty() || it.isNumeric() },
                errorMessage = "Введите корректное число",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

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
                        vacancyFilters = vacancyFilters.copy(requiredWorkExperience =
                            vacancyFilters.requiredWorkExperience?.let { exp -> exp + it } ?: listOf(it)
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
                        bodyVisible = !bodyVisible
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

@Preview(showBackground = true)
@Composable
fun VacancyFormPreview() {
    VacancyEditForm(
        vacancy = Vacancy(),
        comboBoxItems = listOf(ComboBoxItem("preview", {})),
        onTitleUpdate = {_, _ ->},
        onCompanyNameUpdate = {_, _ ->},
        onMinSalaryUpdate = {_, _ ->},
        onMaxSalaryUpdate = {_, _ ->}
    )
}

@Preview(showBackground = true)
@Composable
fun ShimmeringVacancyListItem(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = modifier
            .padding(5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .shimmer()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(30.dp)
                    .background(Color.LightGray)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(24.dp)
                    .background(Color.LightGray)
            )

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .background(Color.LightGray)
                )
                Box(
                    modifier = Modifier
                        .padding(start = 50.dp)
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
            }

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClickableVacancyCardPreview() {
    ClickableVacancyCard(
        vacancy = Vacancy(
            id = "3",
            title = "Cave Digger",
            description = "In this good company you will have everything you want and even money",
            company = Company("Gold rocks"),
            minSalary = 15000,
            maxSalary = 20000,
            publicationStatus = PublicationStatus.Published,
            imageUrl = "https://fakeimg.pl/350x200/?text=World&font=lobster",
        ),
        onClick = { })
}
