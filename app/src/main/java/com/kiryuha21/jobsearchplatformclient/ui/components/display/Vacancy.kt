package com.kiryuha21.jobsearchplatformclient.ui.components.display

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.ui.components.special.DefaultAsyncImageCornered
import com.kiryuha21.jobsearchplatformclient.ui.theme.SelectedCardColor
import com.kiryuha21.jobsearchplatformclient.util.PreviewObjects
import com.kiryuha21.jobsearchplatformclient.util.asFormattedSalary
import com.valentinilk.shimmer.shimmer

@Composable
fun VacancyDetails(
    vacancy: Vacancy,
    isStatusShown: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(10.dp)
    ) {
        if (vacancy.imageUrl != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                DefaultAsyncImageCornered(
                    imageUrl = vacancy.imageUrl,
                    defaultImageId = R.drawable.default_vacancy
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = vacancy.title, fontSize = 28.sp)
            if (isStatusShown) {
                Text(
                    text = vacancy.publicationStatus.toString(),
                    fontStyle = FontStyle.Italic
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column {
                Text(text = "Компания", fontStyle = FontStyle.Italic)
                Text(text = vacancy.company.name)
            }

            Column {
                Text(text = "Зарплатная вилка, ₽", fontStyle = FontStyle.Italic)
                Text(text = "${vacancy.minSalary.asFormattedSalary()} - ${vacancy.maxSalary.asFormattedSalary()}")
            }

            Column {
                Text(text = "Описание от работодателя:", fontStyle = FontStyle.Italic)
                Text(text = vacancy.description)
            }

            if (vacancy.requiredSkills.isNotEmpty()) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.padding(5.dp)
                        )

                        Text(text = "Необходимые навыки:", fontStyle = FontStyle.Italic)
                    }

                    vacancy.requiredSkills.forEach { skill ->
                        Row {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "${Typography.bullet} ${skill.asRequirement()}")
                        }
                    }
                }
            }

            if (vacancy.requiredWorkExperience.isNotEmpty()) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Handyman,
                            contentDescription = null,
                            modifier = Modifier.padding(5.dp)
                        )

                        Text(text = "Необходимый опыт работы:", fontStyle = FontStyle.Italic)
                    }

                    vacancy.requiredWorkExperience.forEach { workExperience ->
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
fun ClickableVacancyCard(
    vacancy: Vacancy,
    isStatusShown: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isChosen: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        modifier = modifier.padding(5.dp),
        border = if (isChosen) BorderStroke(2.dp, SelectedCardColor) else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                if (isStatusShown) {
                    Text(
                        text = vacancy.publicationStatus.toString(),
                        fontStyle = FontStyle.Italic
                    )
                }

                Text(
                    text = vacancy.title,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                Text(text = vacancy.company.name, modifier = Modifier.padding(end = 20.dp))

                val salaryText =
                    if (vacancy.minSalary == vacancy.maxSalary) vacancy.minSalary.asFormattedSalary()
                    else "От ${vacancy.minSalary.asFormattedSalary()} до ${vacancy.maxSalary.asFormattedSalary()}"
                Text(text = salaryText, fontFamily = FontFamily.Cursive)
            }

            if (vacancy.imageUrl != null) {
                DefaultAsyncImageCornered(
                    imageUrl = vacancy.imageUrl,
                    defaultImageId = R.drawable.default_vacancy,
                    imageSize = DpSize(width = 128.dp, height = 128.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmeringVacancyListItem(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = modifier
            .padding(5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .shimmer()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(30.dp)
                    .background(Color.LightGray)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(24.dp)
                    .background(Color.LightGray)
            )

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .background(Color.LightGray)
                )
                Box(
                    modifier = Modifier
                        .padding(start = 50.dp)
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
            }

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClickableVacancyCardPreview() {
    ClickableVacancyCard(
        vacancy = PreviewObjects.previewVacancy1,
        isStatusShown = true,
        onClick = { },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun VacancyDetailsPreview() {
    VacancyDetails(vacancy = PreviewObjects.previewVacancy1, true)
}