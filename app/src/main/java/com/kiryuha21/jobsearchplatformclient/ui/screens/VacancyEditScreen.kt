package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ClickableAsyncUriImage
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.ClickableSkillsList
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.ClickableWorkExperienceList
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBoxItem
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.DefaultButton
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.ImageHintCard
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.SkillForm
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.VacancyEditForm
import com.kiryuha21.jobsearchplatformclient.ui.components.edit.VacancyWorkExperienceForm
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.TextBox
import com.kiryuha21.jobsearchplatformclient.util.getBitmap
import kotlinx.coroutines.launch

@Composable
fun VacancyEditScreen(
    initVacancy: Vacancy,
    isLoading: Boolean,
    loadingText: String,
    onUpdateVacancy: (Vacancy, Bitmap?) -> Unit
) {
    var validTitle by remember { mutableStateOf(initVacancy.title.isNotBlank()) }
    var validCompanyName by remember { mutableStateOf(initVacancy.company.name.isNotBlank()) }
    var validMinSalary by remember { mutableStateOf(true) }
    var validMaxSalary by remember { mutableStateOf(true) }

    var vacancy by remember { mutableStateOf(initVacancy) }
    var skillFormVisible by remember { mutableStateOf(false) }
    var experienceFormVisible by remember { mutableStateOf(false) }

    val comboBoxItems = listOf(
        ComboBoxItem("Опубликовано") {
            vacancy = vacancy.copy(publicationStatus = PublicationStatus.Published)
        },
        ComboBoxItem("Черновик") {
            vacancy = vacancy.copy(publicationStatus = PublicationStatus.Draft)
        },
        ComboBoxItem("Скрыто") {
            vacancy = vacancy.copy(publicationStatus = PublicationStatus.Hidden)
        },
    )

    val context = LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf(if (initVacancy.imageUrl != null) Uri.parse(initVacancy.imageUrl) else null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            selectedImageUri = it
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            val enabled = validTitle && validCompanyName && validMinSalary && validMaxSalary

            ExtendedFloatingActionButton(
                containerColor = if (enabled) FloatingActionButtonDefaults.containerColor else Color.LightGray,
                onClick = {
                    if (enabled) {
                        if (selectedImageUri == null || (initVacancy.imageUrl != null && selectedImageUri == Uri.parse(initVacancy.imageUrl))) {
                            onUpdateVacancy(vacancy, null)
                        } else {
                            onUpdateVacancy(vacancy, selectedImageUri?.getBitmap(context))
                        }
                    }
                },
                modifier = if (enabled) {
                    Modifier.fillMaxWidth(0.4f).testTag("enabled")
                } else {
                    Modifier.fillMaxWidth(0.4f).testTag("disabled")
                }
            ) {
                Text(text = "Сохранить")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp)
                .verticalScroll(scrollState)
        ) {
            if (isLoading) {
                LoadingComponent(
                    description = loadingText,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1F)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClickableAsyncUriImage(
                        selectedImageUri = selectedImageUri,
                        defaultResourceId = R.drawable.upload_logo,
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    )

                    ImageHintCard(
                        hintText = "Логотип компании повышает статус компании",
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }

                VacancyEditForm(
                    vacancy = vacancy,
                    comboBoxItems = comboBoxItems,
                    onTitleUpdate = { value, valid ->
                        if (valid) {
                            vacancy = vacancy.copy(title = value)
                        }
                        validTitle = valid
                    },
                    onCompanyNameUpdate = { value, valid ->
                        if (valid) {
                            vacancy = vacancy.copy(company = Company(value))
                        }
                        validCompanyName = valid
                    },
                    onMinSalaryUpdate = { value, valid ->
                        if (valid) {
                            vacancy = vacancy.copy(minSalary = value.toInt())
                        }
                        validMinSalary = valid
                    },
                    onMaxSalaryUpdate = { value, valid ->
                        if (valid) {
                            vacancy = vacancy.copy(maxSalary = value.toInt())
                        }
                        validMaxSalary = valid
                    },
                )

                HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

                TextBox(
                    placeholder = "Описание вакансии",
                    initString = vacancy.description,
                    onUpdate = { vacancy = vacancy.copy(description = it) },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                ClickableSkillsList(
                    description = "Необходимые навыки:",
                    skills = vacancy.requiredSkills,
                    imageVector = Icons.Default.Delete,
                    onClick = { index ->
                        vacancy = vacancy.copy(
                            requiredSkills = vacancy.requiredSkills.filterIndexed { ind, _ -> index != ind }
                        )
                    }
                )

                ClickableWorkExperienceList(
                    description = "Необходимый опыт работы",
                    workExperience = vacancy.requiredWorkExperience,
                    imageVector = Icons.Default.Delete,
                    isRequirementView = true,
                    onClick = { index ->
                        vacancy = vacancy.copy(
                            requiredWorkExperience = vacancy.requiredWorkExperience.filterIndexed { ind, _ -> index != ind }
                        )
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    DefaultButton(
                        text = "Добавить нужный опыт работы",
                        onClick = {
                            experienceFormVisible = !experienceFormVisible
                            skillFormVisible = false
                            if (experienceFormVisible) {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(Int.MAX_VALUE)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    DefaultButton(
                        text = "Добавить нужный навык",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            experienceFormVisible = false
                            skillFormVisible = !skillFormVisible
                            if (skillFormVisible) {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(Int.MAX_VALUE)
                                }
                            }
                        },
                        shape = RoundedCornerShape(20)
                    )
                }

                AnimatedVisibility(visible = experienceFormVisible) {
                    VacancyWorkExperienceForm(
                        positionLevelText = "Минимальный уровень позиции",
                        positionText = "Позиция",
                        monthsText = "Минимальное количество месяцев",
                        onSubmit = {
                            vacancy = vacancy.copy(requiredWorkExperience = vacancy.requiredWorkExperience + it)
                            experienceFormVisible = false
                        }, onCancel = {
                            experienceFormVisible = false
                        }
                    )
                }

                AnimatedVisibility(visible = skillFormVisible) {
                    SkillForm(
                        skillLevelText = "Нужный уровень навыка",
                        onSubmit = {
                            vacancy = vacancy.copy(requiredSkills = vacancy.requiredSkills + it)
                            skillFormVisible = false
                        },
                        onCancel = {
                            skillFormVisible = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun VacancyEditScreenPreview() {
    VacancyEditScreen(
        initVacancy = Vacancy(),
        isLoading = false,
        loadingText = "loading",
        onUpdateVacancy = { _, _ ->  })
}