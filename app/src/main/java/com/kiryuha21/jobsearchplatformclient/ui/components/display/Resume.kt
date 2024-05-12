package com.kiryuha21.jobsearchplatformclient.ui.components.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.PhoneVisualTransformation
import com.kiryuha21.jobsearchplatformclient.ui.theme.interFontFamily
import com.kiryuha21.jobsearchplatformclient.util.PreviewObjects
import kotlin.math.min

@Composable
fun ResumeDetails(resume: Resume, modifier: Modifier = Modifier) {
    val phoneTransformation = PhoneVisualTransformation("0-(000)-000-00-00", '0')

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
        Text(text = phoneTransformation.filter(AnnotatedString(resume.phoneNumber)).text)
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

@Preview(showBackground = true)
@Composable
fun ClickableCardPreview() {
    ClickableResumeCard(
        resume = PreviewObjects.previewResume1, {}, modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ResumeDetailsPreview() {
    ResumeDetails(
        resume = PreviewObjects.previewResume1, modifier = Modifier.fillMaxWidth()
    )
}