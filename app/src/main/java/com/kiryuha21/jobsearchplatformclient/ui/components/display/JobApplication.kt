package com.kiryuha21.jobsearchplatformclient.ui.components.display

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.JobApplication
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser

@Composable
fun JobApplicationCard(
    jobApplication: JobApplication,
    isReceived: Boolean,
    showResumeDetails: (String) -> Unit,
    showVacancyDetails: (String) -> Unit,
    markChecked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isWorker = CurrentUser.info.role == UserRole.Worker

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            if (isReceived) {
                AnimatedVisibility(visible = !jobApplication.seen) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isWorker) "Новый оффер" else "Новый отклик",
                            style = TextStyle(color = Color(0xFFF39A22))
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Просмотрено: ")

                            Checkbox(
                                checked = jobApplication.seen,
                                onCheckedChange = {
                                    markChecked(jobApplication.id)
                                },
                                enabled = !jobApplication.seen
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(text = "Отправитель:", fontStyle = FontStyle.Italic)
                Text(text = jobApplication.senderInitials)
            }

            Text(text = jobApplication.message)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { showVacancyDetails(jobApplication.referenceVacancyId) }) {
                    Text(text = "Посмотреть вакансию")
                }

                TextButton(onClick = { showResumeDetails(jobApplication.referenceResumeId) }) {
                    Text(text = "Посмотреть резюме")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobOfferCardPreview() {
    JobApplicationCard(
        jobApplication = JobApplication(
            id = "0",
            senderInitials = "Важнич",
            referenceResumeId = "",
            referenceVacancyId = "",
            message = "Мега ждем вас",
            seen = false
        ),
        isReceived = true,
        showResumeDetails = {},
        showVacancyDetails = {},
        markChecked = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    )
}