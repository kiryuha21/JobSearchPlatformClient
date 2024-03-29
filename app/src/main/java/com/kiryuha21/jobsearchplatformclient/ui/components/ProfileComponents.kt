package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy


@Composable
fun ResumeCardWrapper(
    resume: Resume,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                onClick = onEdit,
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
                onClick = onDelete,
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

@Composable
fun VacancyCardWrapper(vacancy: Vacancy, modifier: Modifier = Modifier) {

}

@Composable
fun NoItemsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text("No content available")
        }
    }
}

@Composable
fun ProfileTitle(

) {

}