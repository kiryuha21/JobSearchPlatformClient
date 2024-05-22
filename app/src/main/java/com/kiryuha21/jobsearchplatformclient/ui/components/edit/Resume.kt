package com.kiryuha21.jobsearchplatformclient.ui.components.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
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
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.ValidatedTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBox
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBoxItem
import com.kiryuha21.jobsearchplatformclient.ui.components.special.DefaultDatePickerDialog
import com.kiryuha21.jobsearchplatformclient.util.isNumeric
import com.kiryuha21.jobsearchplatformclient.util.toFormattedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ResumeEditForm(
    resume: Resume,
    comboBoxItems: List<ComboBoxItem>,
    onFirstNameUpdate: (String, Boolean) -> Unit,
    onLastNameUpdate: (String, Boolean) -> Unit,
    onBirthDateUpdate: (Long?) -> Unit,
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

    HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

    var datePickerOpened by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (datePickerOpened) {
            DefaultDatePickerDialog(
                initTime = resume.birthDate,
                onDismiss = { datePickerOpened = !datePickerOpened },
                onConfirm = {
                    datePickerOpened = !datePickerOpened
                    onBirthDateUpdate(it)
                }
            )
        }

        if (resume.birthDate != null) {
            Text(
                text = "Дата рождения - ${resume.birthDate.toFormattedDateTime(DateTimeFormatter.ISO_LOCAL_DATE)}",
                modifier = Modifier.padding(end = 10.dp)
            )
        } else {
            Text(
                text = "Нужно выбрать дату рождения",
                color = Color.Red,
                modifier = Modifier.padding(end = 10.dp)
            )
        }

        OutlinedButton(
            onClick = { datePickerOpened = !datePickerOpened },
            shape = RoundedCornerShape(10),
            modifier = Modifier.width(120.dp)
        ) {
            Text(text = if (resume.birthDate == null) "Выбрать" else "Поменять")
        }
    }

    HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

    ValidatedTextField(
        text = resume.firstName,
        placeholder = "Имя",
        isValid = { it.isNotBlank() },
        errorMessage = "Имя не может быть пустым",
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        icon = Icons.Default.Abc,
        onUpdate = onFirstNameUpdate
    )
    ValidatedTextField(
        text = resume.lastName,
        placeholder = "Фамилия",
        isValid = { it.isNotBlank() },
        errorMessage = "Фамилия не может быть пустой",
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        icon = Icons.Default.Abc,
        onUpdate = onLastNameUpdate
    )
    PhoneField(
        text = resume.phoneNumber,
        placeholder = "Номер телефона",
        icon = Icons.Default.Phone,
        mask = "0-(000)-000-00-00",
        onUpdate = onPhoneUpdate,
        errorMessage = "Введите корректный телефон",
        isValid = { it.isNotBlank() && it.isNumeric() },
        modifier = Modifier.fillMaxWidth()
    )
    ValidatedTextField(
        text = resume.contactEmail,
        placeholder = "E-mail",
        isValid = { it.isNotBlank() },
        errorMessage = "E-mail не может быть пустой",
        modifier = Modifier.fillMaxWidth(),
        icon = Icons.Default.Email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        onUpdate = onEmailUpdate
    )
    ValidatedTextField(
        text = resume.applyPosition,
        placeholder = "Позиция",
        isValid = { it.isNotBlank() },
        errorMessage = "Позиция не может быть пустой",
        modifier = Modifier.fillMaxWidth(),
        icon = Icons.Default.Work,
        onUpdate = onPositionUpdate
    )

    HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
}

@Composable
fun ResumeWorkExperienceForm(
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
            Text(text = "Уровень позиции")
            ComboBox(items = comboBoxItems)
        }

        ValidatedTextField(
            text = workExperience.company.name,
            icon = Icons.Default.Abc,
            placeholder = "Название компании",
            onUpdate = { value, _ -> workExperience = workExperience.copy(company = Company(value)) },
            isValid = { it.isNotBlank() },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            errorMessage = "Название не может быть пустым",
            modifier = Modifier.fillMaxWidth()
        )

        ValidatedTextField(
            text = workExperience.position,
            icon = Icons.Default.Abc,
            placeholder = "Позиция",
            onUpdate = { value, _ -> workExperience = workExperience.copy(position = value) },
            isValid = { it.isNotBlank() },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            errorMessage = "Позиция не может быть пустой",
            modifier = Modifier.fillMaxWidth()
        )

        ValidatedTextField(
            text = workExperience.salary,
            placeholder = "Зарплата (₽)",
            onUpdate = { text, _ -> workExperience = workExperience.copy(salary = text) },
            isValid = { it.isNotEmpty() && it.isNumeric() },
            errorMessage = "Введите корректное число",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        ValidatedTextField(
            text = workExperience.months,
            placeholder = "Месяцев проработано",
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
            val valid = workExperience.isValidForResume()

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