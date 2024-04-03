package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kiryuha21.jobsearchplatformclient.data.domain.Company
import com.kiryuha21.jobsearchplatformclient.data.domain.CompanySize
import com.kiryuha21.jobsearchplatformclient.data.domain.PublicationStatus
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadedVacancyItem(vacancy: Vacancy, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = modifier.padding(5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = vacancy.title,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            val salaryText =
                if (vacancy.minSalary == vacancy.maxSalary) "${vacancy.minSalary}"
                else "От ${vacancy.minSalary} до ${vacancy.maxSalary}"
            Text(text = salaryText, fontFamily = FontFamily.Cursive)

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Work,
                    contentDescription = "work",
                )
                Text(text = vacancy.company.name, modifier = Modifier.padding(start = 20.dp))
            }

            TextButton(onClick = { isExpanded = !isExpanded }) {
                Text(
                    text = vacancy.description,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    modifier = Modifier.animateContentSize()
                )
            }
        }
    }
}

@Composable
fun VacancyDetails(vacancy: Vacancy, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = vacancy.title)
        }
        Text(text = vacancy.company.name)
        Text(text = "${vacancy.minSalary} - ${vacancy.maxSalary}")
        Text(text = vacancy.description)
    }
}

@Composable
fun ClickableVacancyCard(vacancy: Vacancy, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        modifier = modifier.padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = vacancy.title,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                Text(text = vacancy.company.name, modifier = Modifier.padding(end = 20.dp))

                val salaryText =
                    if (vacancy.minSalary == vacancy.maxSalary) "${vacancy.minSalary}"
                    else "От ${vacancy.minSalary} до ${vacancy.maxSalary}"
                Text(text = salaryText, fontFamily = FontFamily.Cursive)
            }

            if (vacancy.imageUrl == null) {
                Icon(
                    imageVector = Icons.Rounded.Work,
                    contentDescription = "work",
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )
            } else {
                AsyncImage(
                    model = vacancy.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

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
        vacancy = Vacancy(
            id = "3",
            title = "Cave Digger",
            description = "In this good company you will have everything you want and even money",
            company = Company("Gold rocks", CompanySize.Big),
            minSalary = 15000,
            maxSalary = 20000,
            publicationStatus = PublicationStatus.Published,
            imageUrl = "https://fakeimg.pl/350x200/?text=World&font=lobster",
        ),
        onClick = { })
}
