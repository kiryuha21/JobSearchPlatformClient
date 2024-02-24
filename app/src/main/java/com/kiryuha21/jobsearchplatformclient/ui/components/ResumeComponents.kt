package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.CompanySize
import com.kiryuha21.jobsearchplatformclient.data.domain.PositionLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.SkillLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import kotlin.text.Typography.bullet

@Composable
fun ResumeCard(resume: Resume, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        onClick = { isExpanded = !isExpanded },
        modifier = modifier.padding(10.dp)
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
                    fontSize = 24.sp,
                    modifier = Modifier.offset(x = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "full name")
                Text(
                    text = resume.fullName(),
                    fontSize = 18.sp,
                    modifier = Modifier.offset(x = 10.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Email, contentDescription = "email")
                Text(
                    text = "email: ${resume.contactEmail}",
                    fontSize = 16.sp,
                    modifier = Modifier.offset(x = 10.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Phone, contentDescription = "contact phone")
                Text(
                    text = "Контактный телефон: ${resume.phoneNumber}",
                    fontSize = 16.sp,
                    modifier = Modifier.offset(x = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (isExpanded) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Build, contentDescription = "skills")
                    Text(text = "Навыки:", fontSize = 16.sp, modifier = Modifier.offset(x = 10.dp))
                }
                for (skill in resume.skills) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = ParagraphStyle(textIndent = TextIndent(firstLine = 19.sp))) {
                                append(bullet)
                                append("\t\t")
                                append(skill.toString())
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
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
                                append(bullet)
                                append("\t\t")
                                append(experience.workMonthsFormatted())
                                append(" в ${experience.company.name} как ${experience.positionFormatted()}")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Нажмите чтобы посмотреть полностью")
                }
            }
        }
    }
}

@Composable
fun ResumeCardWrapper(resume: Resume, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        ResumeCard(resume = resume, modifier = Modifier.fillMaxWidth())
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            OutlinedButton(
                onClick = { /*TODO*/ },
                colors = ButtonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color.White,
                    contentColor = Color.Red,
                    disabledContentColor = Color.Red
                ),
                border = BorderStroke(width = 1.dp, color = Color.Red)
            ) {
                Text(text = "Редактировать")
            }
            OutlinedButton(
                onClick = { /*TODO*/ },
                colors = ButtonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color.White,
                    contentColor = Color.hsl(49.6F, 1F, 0.4412F),
                    disabledContentColor = Color.hsl(49.6F, 1F, 0.4412F)
                ),
                border = BorderStroke(width = 1.dp, color = Color.hsl(49.6F, 1F, 0.4412F))
            ) {
                Text(text = "Удалить")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    ResumeCard(
        resume = Resume(
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
                        CompanySize.Big
                    ),
                    "C++ developer",
                    PositionLevel.Lead,
                    100500,
                    420
                )
            )
        )
    )
}