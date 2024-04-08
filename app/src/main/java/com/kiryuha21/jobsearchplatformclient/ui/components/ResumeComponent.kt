package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
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
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
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
import kotlin.math.min

@Composable
fun ResumeCard(
    resume: Resume,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        onClick = { isExpanded = !isExpanded },
        modifier = modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .animateContentSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Work, contentDescription = "title")
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
                Icon(imageVector = Icons.Filled.Email, contentDescription = "email")
                Text(
                    text = "E-mail: ${resume.contactEmail}",
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.offset(x = 10.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(imageVector = Icons.Filled.Phone, contentDescription = "contact phone")
                Text(
                    text = "Контактный телефон: ${resume.phoneNumber}",
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.offset(x = 10.dp)
                )
            }

            if (isExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Build, contentDescription = "skills")
                    Text(text = "Навыки:", fontSize = 16.sp, modifier = Modifier.offset(x = 10.dp))
                }
                for (skill in resume.skills) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = ParagraphStyle(textIndent = TextIndent(firstLine = 19.sp))) {
                                append(Typography.bullet)
                                append("\t\t")
                                append(skill.toString())
                            }
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Build, contentDescription = "work experience")
                    Text(
                        text = "Стаж работы:",
                        fontSize = 16.sp,
                        modifier = Modifier.offset(x = 10.dp)
                    )
                }
                for (experience in resume.workExperience) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = ParagraphStyle(textIndent = TextIndent(firstLine = 19.sp))) {
                                append(Typography.bullet)
                                append("\t\t")
                                append(experience.workMonthsFormatted())
                                append(" в ${experience.company.name} как ${experience.positionFormatted()}")
                            }
                        }
                    )
                }
            } else {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp)
                ) {
                    Text(
                        text = "Нажмите чтобы раскрыть",
                        fontSize = 12.sp,
                        fontFamily = interFontFamily
                    )
                }
            }
        }
    }
}

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
    val skill by remember { mutableStateOf(Skill("", SkillLevel.AwareOf)) }
    val comboBoxItems = listOf(
        ComboBoxItem("Имею представление") {
            skill.skillLevel = SkillLevel.AwareOf
        },
        ComboBoxItem("Имел дело") {
            skill.skillLevel = SkillLevel.Tested
        },
        ComboBoxItem("Есть пет-проекты") {
            skill.skillLevel = SkillLevel.HasPetProjects
        },
        ComboBoxItem("Есть коммерческие проекты") {
            skill.skillLevel = SkillLevel.HasCommercialProjects
        },
    )

    Column(
        modifier = modifier
    ) {
        DefaultTextField(
            icon = Icons.Default.Abc,
            placeholder = "Названия навыка",
            initString = "",
            onUpdate = { skill.name = it },
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
            DefaultButton(text = "Сохранить", onClick = { onSubmit(skill) })
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
    val workExperience by remember {
        mutableStateOf(WorkExperience(Company(""), "", PositionLevel.Junior, 0, 0))
    }
    val comboBoxItems = listOf(
        ComboBoxItem("Джуниор") {
            workExperience.positionLevel = PositionLevel.Junior
        },
        ComboBoxItem("Мидл") {
            workExperience.positionLevel = PositionLevel.Middle
        },
        ComboBoxItem("Сеньор") {
            workExperience.positionLevel = PositionLevel.Senior
        },
        ComboBoxItem("Лид") {
            workExperience.positionLevel = PositionLevel.Lead
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

        DefaultTextField(
            icon = Icons.Default.Abc,
            placeholder = "Позиция",
            initString = "",
            onUpdate = { workExperience.position = it },
            modifier = Modifier.fillMaxWidth()
        )

        NumericTextField(
            placeholder = "Зарплата (₽)",
            initString = "0",
            onUpdate = { workExperience.salary = it.toInt() },
            modifier = Modifier.fillMaxWidth()
        )

        NumericTextField(
            placeholder = "Месяцев проработано",
            initString = "0",
            onUpdate = { workExperience.months = it.toInt() },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            DefaultButton(text = "Сохранить", onClick = { onSubmit(workExperience) })
            DefaultButton(text = "Отменить", onClick = onCancel)
        }
    }
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
fun CardPreview() {
    ResumeCard(
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
        ), modifier = Modifier.fillMaxWidth()
    )
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
