package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Employer
import com.kiryuha21.jobsearchplatformclient.data.domain.Worker
import com.kiryuha21.jobsearchplatformclient.ui.components.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.components.ResumeCardWrapper
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.ui.components.VacancyCardWrapper

@Composable
fun ProfileScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Title(text = "Мои резюме", fontSize = 30.sp)
        Title(text = "Вы вошли как ${CurrentUser.userInfo.value.login}", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))
        when (CurrentUser.userInfo.value) {
            is Worker -> {
                val resumes = (CurrentUser.userInfo.value as Worker).resumes
                if (resumes != null) {
                    LazyColumn {
                        items(resumes) {
                            ResumeCardWrapper(
                                resume = it,
                                onEdit = {},
                                onDelete = {},
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    NoItemsCard()
                }
            }

            is Employer -> {
                val vacancies = (CurrentUser.userInfo.value as Employer).vacancies
                if (vacancies != null) {
                    LazyColumn {
                        items(vacancies) {
                            VacancyCardWrapper(vacancy = it, modifier = Modifier.fillMaxWidth())
                        }
                    }
                } else {
                    NoItemsCard()
                }
            }
        }
    }
}