package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PositionLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.SkillLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.ui.theme.interFontFamily
import com.kiryuha21.jobsearchplatformclient.util.isNumeric
import kotlin.math.min

@Composable
fun ResumeDetails(resume: Resume, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = resume.applyPosition)
        }
        Text(text = resume.fullName())
        Text(text = resume.contactEmail)
        Text(text = resume.phoneNumber)
        for (skill in resume.skills) {
            Text(text = skill.toString())
        }
        for (exp in resume.workExperience) {
            Text(text = "${exp.workMonthsFormatted()} как ${exp.positionFormatted()}")
        }
    }
}

@Composable
fun ClickableResumeCard(resume: Resume, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.WorkOutline, contentDescription = "title")
                Text(
                    text = resume.applyPosition,
                    fontSize = 20.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.offset(x = 10.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "full name")
                Text(
                    text = resume.fullName(),
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = "email")
                Text(
                    text = "Навыки",
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                for (i in 0..min(2, resume.skills.size - 1)) {
                    Text(
                        text = "${Typography.bullet} ${resume.skills[i].name}",
                        fontSize = 16.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SkillForm(
    onSubmit: (Skill) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var validName by remember { mutableStateOf(false) }
    var skill by remember { mutableStateOf(Skill("", SkillLevel.AwareOf)) }

    val comboBoxItems = listOf(
        ComboBoxItem("Имею представление") {
            skill = skill.copy(skillLevel = SkillLevel.AwareOf)
        },
        ComboBoxItem("Имел дело") {
            skill = skill.copy(skillLevel = SkillLevel.Tested)
        },
        ComboBoxItem("Есть пет-проекты") {
            skill = skill.copy(skillLevel = SkillLevel.HasPetProjects)
        },
        ComboBoxItem("Есть коммерческие проекты") {
            skill = skill.copy(skillLevel = SkillLevel.HasCommercialProjects)
        },
    )

    Column(
        modifier = modifier
    ) {
        ValidateableTextField(
            icon = Icons.Default.Abc,
            placeholder = "Названия навыка",
            initString = "",
            onUpdate = { value, valid ->
                if (valid) {
                    validName = true
                    skill = skill.copy(name = value)
                } else {
                    validName = false
                }
            },
            isValid = { it.isNotBlank() },
            errorMessage = "Название навыка не может быть пустым",
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Уровень навыка")
            ComboBox(items = comboBoxItems)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            SecuredButton(text = "Сохранить", onClick = { onSubmit(skill) }, enabled = validName)
            DefaultButton(text = "Отменить", onClick = onCancel)
        }
    }
}

@Composable
fun WorkExperienceForm(
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

@Preview(showBackground = true)
@Composable
fun WorkExperienceFormPreview() {
    WorkExperienceForm({}, {})
}

@Preview(showBackground = true)
@Composable
fun SkillFormPreview() {
    SkillForm({}, {})
}

@Preview(showBackground = true)
@Composable
fun ClickableCardPreview() {
    ClickableResumeCard(
        resume = Resume(
            "12khe12nj1nek",
            "John",
            "Smit",
            "12909483",
            "hey@gmail.com",
            "Senior C++ developer",
            listOf(
                Skill(
                    "C++ development",
                    SkillLevel.HasCommercialProjects
                ),
                Skill(
                    "Drinking Tea",
                    SkillLevel.AwareOf
                ),
                Skill(
                    "Sleeping well",
                    SkillLevel.AwareOf
                ),
                Skill(
                    "Dropping prod",
                    SkillLevel.AwareOf
                )
            ),
            listOf(
                WorkExperience(
                    Company(
                        "yandex",
                    ),
                    "C++ developer",
                    PositionLevel.Lead,
                    100500,
                    420
                )
            ),
            PublicationStatus.Published
        ), {}, modifier = Modifier.fillMaxWidth()
    )
}
