package com.kiryuha21.jobsearchplatformclient.ui.components.display

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.PhoneVisualTransformation
import com.kiryuha21.jobsearchplatformclient.ui.components.special.DefaultAsyncImageCornered
import com.kiryuha21.jobsearchplatformclient.ui.theme.interFontFamily
import com.kiryuha21.jobsearchplatformclient.util.PreviewObjects
import com.kiryuha21.jobsearchplatformclient.util.toYears
import kotlin.math.min

@Composable
fun ResumeDetails(
    resume: Resume,
    isStatusShown: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(10.dp)
    ) {
        if (resume.imageUrl != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                DefaultAsyncImageCornered(
                    imageUrl = resume.imageUrl,
                    defaultImageId = R.drawable.default_vacancy
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = resume.fullName(), fontSize = 28.sp)
            if (isStatusShown) {
                Text(
                    text = resume.publicationStatus.toString(),
                    fontStyle = FontStyle.Italic
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column {
                Text(text = "Позиция", fontStyle = FontStyle.Italic)
                Text(text = resume.applyPosition)
            }

            Column {
                val phoneTransformation = PhoneVisualTransformation("0-(000)-000-00-00", '0')

                Text(text = "Контактные данные", fontStyle = FontStyle.Italic)
                Text(text = "Телефон: ${phoneTransformation.filter(AnnotatedString(resume.phoneNumber)).text}")
                Text(text = "E-mail: ${resume.contactEmail}")
            }

            Column {
                Text(text = "Возраст в годах", fontStyle = FontStyle.Italic)
                Text(text = resume.birthDate!!.toYears().toString())
            }

            if (resume.skills.isNotEmpty()) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.padding(5.dp)
                        )

                        Text(text = "Навыки:", fontStyle = FontStyle.Italic)
                    }

                    resume.skills.forEach { skill ->
                        Row {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "${Typography.bullet} $skill")
                        }
                    }
                }
            }

            if (resume.workExperience.isNotEmpty()) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Handyman,
                            contentDescription = null,
                            modifier = Modifier.padding(5.dp)
                        )

                        Text(text = "Опыт работы:", fontStyle = FontStyle.Italic)
                    }

                    resume.workExperience.forEach { workExperience ->
                        Row {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "${Typography.bullet} $workExperience")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClickableResumeCard(
    resume: Resume,
    isStatusShown: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isChosen: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        border = if (isChosen) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(10.dp)
            ) {
                if (isStatusShown) {
                    Text(
                        text = resume.publicationStatus.toString(),
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

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

            if (resume.imageUrl != null) {
                DefaultAsyncImageCornered(
                    imageUrl = resume.imageUrl,
                    defaultImageId = R.drawable.default_avatar,
                    imageSize = DpSize(width = 128.dp, height = 128.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClickableCardPreview() {
    ClickableResumeCard(
        resume = PreviewObjects.previewResume1, true, {}, modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ResumeDetailsPreview() {
    ResumeDetails(
        resume = PreviewObjects.previewResume1, true, modifier = Modifier.fillMaxWidth()
    )
}