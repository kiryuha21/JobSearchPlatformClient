package com.kiryuha21.jobsearchplatformclient.ui.components.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.Skill
import com.kiryuha21.jobsearchplatformclient.data.domain.SkillLevel
import com.kiryuha21.jobsearchplatformclient.data.domain.WorkExperience
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.ValidatedTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.DefaultButton
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.SecuredButton
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBox
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ComboBoxItem

@Composable
fun ImageHintCard(hintText: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.Gray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null)
            Text(text = hintText)
        }
    }
}

@Composable
fun ClickableSkillsList(
    description: String,
    skills: List<Skill>,
    imageVector: ImageVector,
    onClick: (Int) -> Unit
) {
    if (skills.isNotEmpty()) {
        Text(text = description, fontSize = 18.sp, modifier = Modifier.padding(top = 10.dp))
        skills.forEachIndexed { index, skill ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = skill.toString(), modifier = Modifier.fillMaxWidth(0.8f))
                IconButton(
                    onClick = { onClick(index) }
                ) {
                    Icon(imageVector = imageVector, contentDescription = "delete")
                }
            }
        }
    }
}

@Composable
fun ClickableWorkExperienceList(
    description: String,
    workExperience: List<WorkExperience>,
    imageVector: ImageVector,
    isRequirementView: Boolean,
    onClick: (Int) -> Unit
) {
    if (workExperience.isNotEmpty()) {
        Text(text = description, fontSize = 18.sp, modifier = Modifier.padding(top = 10.dp))
        workExperience.forEachIndexed { index, exp ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isRequirementView) exp.formattedAsRequirement() else exp.toString(),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                IconButton(
                    onClick = { onClick(index) }
                ) {
                    Icon(imageVector = imageVector, contentDescription = "delete")
                }
            }
        }
    }
}

@Composable
fun SkillForm(
    skillLevelText: String,
    onSubmit: (Skill) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = skillLevelText)
            ComboBox(items = comboBoxItems)
        }

        ValidatedTextField(
            text = skill.name,
            icon = Icons.Default.Abc,
            placeholder = "Названия навыка",
            onUpdate = { value, _ -> skill = skill.copy(name = value) },
            isValid = { it.isNotBlank() },
            errorMessage = "Название навыка не может быть пустым",
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            SecuredButton(text = "Сохранить", onClick = { onSubmit(skill) }, enabled = skill.isValid())
            DefaultButton(text = "Отменить", onClick = onCancel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkillFormPreview() {
    SkillForm("Нужный уровень навыка", {}, {}, modifier = Modifier.padding(10.dp))
}