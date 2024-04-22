package com.kiryuha21.jobsearchplatformclient.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.PositionLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.SkillLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableAsyncUriImage
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableSkillsList
import com.kiryuha21.jobsearchplatformclient.ui.components.ClickableWorkExperienceList
import com.kiryuha21.jobsearchplatformclient.ui.components.ComboBoxItem
import com.kiryuha21.jobsearchplatformclient.ui.components.DefaultButton
import com.kiryuha21.jobsearchplatformclient.ui.components.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.ResumeEditForm
import com.kiryuha21.jobsearchplatformclient.ui.components.ResumeImageHintCard
import com.kiryuha21.jobsearchplatformclient.ui.components.SkillForm
import com.kiryuha21.jobsearchplatformclient.ui.components.WorkExperienceForm
import com.kiryuha21.jobsearchplatformclient.util.getBitmap
import com.kiryuha21.jobsearchplatformclient.util.isNumeric
import kotlinx.coroutines.launch

@Composable
fun ResumeEditScreen(
    initResume: Resume,
    isLoading: Boolean,
    onUpdateResume: (Resume, Bitmap?) -> Unit
) {
    var validName by remember { mutableStateOf(initResume.firstName.isNotBlank()) }
    var validSurname by remember { mutableStateOf(initResume.lastName.isNotBlank()) }
    var validEmail by remember { mutableStateOf(initResume.contactEmail.isNotBlank()) }
    var validPosition by remember { mutableStateOf(initResume.applyPosition.isNotBlank()) }
    var validPhone by remember { mutableStateOf(initResume.phoneNumber.isNotBlank() && initResume.phoneNumber.isNumeric()) }

    var resume by remember { mutableStateOf(initResume) }
    var skillFormVisible by remember { mutableStateOf(false) }
    var experienceFormVisible by remember { mutableStateOf(false) }

    val comboBoxItems = listOf(
        ComboBoxItem("Опубликовано") {
            resume = resume.copy(publicationStatus = PublicationStatus.Published)
        },
        ComboBoxItem("Черновик") {
            resume = resume.copy(publicationStatus = PublicationStatus.Draft)
        },
        ComboBoxItem("Скрыто") {
            resume = resume.copy(publicationStatus = PublicationStatus.Hidden)
        },
    )

    val context = LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf(if (initResume.imageUrl != null) Uri.parse(initResume.imageUrl) else null)
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
            val enabled = validName && validSurname && validEmail && validPosition && validPhone

            ExtendedFloatingActionButton(
                containerColor = if (enabled) FloatingActionButtonDefaults.containerColor else Color.LightGray,
                onClick = {
                    if (!enabled) {
                        return@ExtendedFloatingActionButton
                    }

                    if (selectedImageUri == null || (initResume.imageUrl != null && selectedImageUri == Uri.parse(initResume.imageUrl))) {
                        onUpdateResume(resume, null)
                    } else {
                        onUpdateResume(resume, selectedImageUri?.getBitmap(context))
                    }
                },
                modifier = Modifier.fillMaxWidth(0.4f)
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
                    description = "Сохранение резюме...",
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
                        defaultResourceId = R.drawable.upload_photo,
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    )

                    ResumeImageHintCard(modifier = Modifier.padding(start = 20.dp))
                }

                ResumeEditForm(
                    resume = resume,
                    comboBoxItems = comboBoxItems,
                    onFirstNameUpdate = { value, valid ->
                        if (valid) {
                            validName = true
                            resume = resume.copy(firstName = value)
                        } else {
                            validName = false
                        }
                    },
                    onLastNameUpdate = { value, valid ->
                        if (valid) {
                            validSurname = true
                            resume = resume.copy(lastName = value)
                        } else {
                            validSurname = false
                        }
                    },
                    onPhoneUpdate = { value, valid ->
                        if (valid) {
                            validPhone = true
                            resume = resume.copy(phoneNumber = value)
                        } else {
                            validPhone = false
                        }
                    },
                    onEmailUpdate = { value, valid ->
                        if (valid) {
                            validEmail = true
                            resume = resume.copy(contactEmail = value)
                        } else {
                            validEmail = false
                        }
                    },
                    onPositionUpdate = { value, valid ->
                        if (valid) {
                            validPosition = true
                            resume = resume.copy(applyPosition = value)
                        } else {
                            validPosition = false
                        }
                    }
                )

                ClickableSkillsList(
                    skills = resume.skills,
                    imageVector = Icons.Default.Delete,
                    onClick = { index ->
                        resume = resume.copy(
                            skills = resume.skills.filterIndexed { ind, _ -> index != ind }
                        )
                    }
                )

                ClickableWorkExperienceList(
                    workExperience = resume.workExperience,
                    imageVector = Icons.Default.Delete,
                    onClick = { index ->
                        resume = resume.copy(
                            workExperience = resume.workExperience.filterIndexed { ind, _ -> index != ind }
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
                        text = "Добавить опыт работы", onClick = {
                            experienceFormVisible = !experienceFormVisible
                            skillFormVisible = false
                            if (experienceFormVisible) {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(Int.MAX_VALUE)
                                }
                            }
                        }
                    )
                    DefaultButton(
                        text = "Добавить навык", onClick = {
                            experienceFormVisible = false
                            skillFormVisible = !skillFormVisible
                            if (skillFormVisible) {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(Int.MAX_VALUE)
                                }
                            }
                        }
                    )
                }

                AnimatedVisibility(visible = experienceFormVisible) {
                    WorkExperienceForm(
                        onSubmit = {
                            resume = resume.copy(workExperience = resume.workExperience + it)
                            experienceFormVisible = false
                        }, onCancel = {
                            experienceFormVisible = false
                        }
                    )
                }

                AnimatedVisibility(visible = skillFormVisible) {
                    SkillForm(
                        onSubmit = {
                            resume = resume.copy(skills = resume.skills + it)
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
fun ResumeEditScreenPreview() {
    ResumeEditScreen(
        initResume = Resume(
            "",
            "first",
            "second",
            "123454678",
            "test@gmail.com",
            "C++ programmer",
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
        ), true
    ) { _, _ -> }
}