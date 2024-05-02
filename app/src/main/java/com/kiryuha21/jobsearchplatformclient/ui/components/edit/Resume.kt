package com.kiryuha21.jobsearchplatformclient.ui.components.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PositionLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.DefaultButton
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.PhoneField
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.SecuredButton
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.ValidateableTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBox
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBoxItem
import com.kiryuha21.jobsearchplatformclient.util.isNumeric

@Composable
fun ResumeEditForm(
    resume: Resume,
    comboBoxItems: List<ComboBoxItem>,
    onFirstNameUpdate: (String, Boolean) -> Unit,
    onLastNameUpdate: (String, Boolean) -> Unit,
    onPhoneUpdate: (String, Boolean) -> Unit,
    onEmailUpdate: (String, Boolean) -> Unit,
    onPositionUpdate: (String, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Статус резюме")
        ComboBox(items = comboBoxItems)
    }
    ValidateableTextField(
        placeholder = "Имя",
        initString = resume.firstName,
        isValid = { it.isNotBlank() },
        errorMessage = "Имя не может быть пустым",
        modifier = Modifier.fillMaxWidth(),
        icon = Icons.Default.Abc,
        onUpdate = onFirstNameUpdate
    )
    ValidateableTextField(
        placeholder = "Фамилия",
        initString = resume.lastName,
        isValid = { it.isNotBlank() },
        errorMessage = "Фамилия не может быть пустой",
        modifier = Modifier.fillMaxWidth(),
        icon = Icons.Default.Abc,
        onUpdate = onLastNameUpdate
    )
    PhoneField(
        initString = resume.phoneNumber,
        placeholder = "Номер телефона",
        icon = Icons.Default.Phone,
        mask = "0-(000)-000-00-00",
        onUpdate = onPhoneUpdate,
        errorMessage = "Введите корректный телефон",
        isValid = { it.isNotBlank() && it.isNumeric() },
        modifier = Modifier.fillMaxWidth()
    )
    ValidateableTextField(
        placeholder = "E-mail",
        initString = resume.contactEmail,
        isValid = { it.isNotBlank() },
        errorMessage = "E-mail не может быть пустой",
        modifier = Modifier.fillMaxWidth(),
        icon = Icons.Default.Email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        onUpdate = onEmailUpdate
    )
    ValidateableTextField(
        placeholder = "Позиция",
        initString = resume.applyPosition,
        isValid = { it.isNotBlank() },
        errorMessage = "Позиция не может быть пустой",
        modifier = Modifier.fillMaxWidth(),
        icon = Icons.Default.Work,
        onUpdate = onPositionUpdate
    )
}

@Composable
fun ResumeWorkExperienceForm(
    onSubmit: (WorkExperience) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var validSalary by remember { mutableStateOf(false) }
    var validMonths by remember { mutableStateOf(false) }
    var validCompany by remember { mutableStateOf(false) }
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
            Text(text = "Уровень позиции")
            ComboBox(items = comboBoxItems)
        }

        ValidateableTextField(
            icon = Icons.Default.Abc,
            placeholder = "Название компании",
            initString = "",
            onUpdate = { value, valid ->
                if (valid) {
                    workExperience = workExperience.copy(company = Company(value))
                }
                validCompany = valid
            },
            isValid = { it.isNotBlank() },
            errorMessage = "Название не может быть пустым",
            modifier = Modifier.fillMaxWidth()
        )

        ValidateableTextField(
            icon = Icons.Default.Abc,
            placeholder = "Позиция",
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
            placeholder = "Зарплата (₽)",
            initString = "",
            onUpdate = { text, valid ->
                if (valid) {
                    workExperience = workExperience.copy(salary = text.toInt())
                }
                validSalary = valid
            },
            isValid = { it.isNotEmpty() && it.isNumeric() },
            errorMessage = "Введите корректное число",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        ValidateableTextField(
            placeholder = "Месяцев проработано",
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
            val valid = validCompany && validPosition && validMonths && validSalary

            SecuredButton(
                text = "Сохранить",
                onClick = { onSubmit(workExperience) },
                enabled = valid
            )
            DefaultButton(text = "Отменить", onClick = onCancel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkExperienceFormPreview() {
    ResumeWorkExperienceForm({}, {})
}