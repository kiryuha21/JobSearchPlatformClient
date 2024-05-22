package com.kiryuha21.jobsearchplatformclient.ui.components.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PositionLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.DefaultButton
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.SecuredButton
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.ValidatedTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBox
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBoxItem
import com.kiryuha21.jobsearchplatformclient.util.isNumeric

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
        ValidatedTextField(
            text = vacancy.title,
            placeholder = "Название вакансии",
            isValid = { it.isNotBlank() },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            errorMessage = "Название вакансии не может быть пустым",
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Abc,
            onUpdate = onTitleUpdate
        )
        ValidatedTextField(
            text = vacancy.company.name,
            placeholder = "Название компании",
            isValid = { it.isNotBlank() },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                ValidatedTextField(
                    text = vacancy.minSalary,
                    placeholder = "Минимум, ₽",
                    isValid = { it.isNotBlank() && it.isNumeric() },
                    errorMessage = "Некорректное число",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onUpdate = onMinSalaryUpdate,
                    modifier = Modifier.testTag("vacancy_edit_min_salary")
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
                ValidatedTextField(
                    text = vacancy.maxSalary,
                    placeholder = "Максимум, ₽",
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
    positionLevelText: String,
    positionText: String,
    monthsText: String,
    onSubmit: (WorkExperience) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var workExperience by remember {
        mutableStateOf(WorkExperience(Company(""), "", PositionLevel.Junior, "", ""))
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
            Text(text = positionLevelText)
            ComboBox(items = comboBoxItems)
        }

        ValidatedTextField(
            text = workExperience.position,
            icon = Icons.Default.Abc,
            placeholder = positionText,
            onUpdate = { value, _ -> workExperience = workExperience.copy(position = value) },
            isValid = { it.isNotBlank() },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            errorMessage = "Позиция не может быть пустой",
            modifier = Modifier.fillMaxWidth()
        )

        ValidatedTextField(
            text = workExperience.months,
            placeholder = monthsText,
            onUpdate = { text, _ -> workExperience = workExperience.copy(months = text) },
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
            SecuredButton(
                text = "Сохранить",
                onClick = { onSubmit(workExperience) },
                enabled = workExperience.isValidForVacancy()
            )
            DefaultButton(text = "Отменить", onClick = onCancel)
        }
    }
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