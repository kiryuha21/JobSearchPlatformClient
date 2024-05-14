package com.kiryuha21.jobsearchplatformclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.di.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.components.display.ClickableResumeCard
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.LoadingComponent
import com.kiryuha21.jobsearchplatformclient.ui.components.special.NoItemsCard
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.Title
import com.kiryuha21.jobsearchplatformclient.ui.contract.WorkerProfileContract

@Composable
fun WorkerProfileScreen(
    state: WorkerProfileContract.State,
    loadResumes: () -> Unit,
    openResumeDetails: (String) -> Unit,
    openResumeEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        loadResumes()
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = openResumeEdit,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(bottom = 20.dp)
            ) {
                Text(text = "Новое резюме")
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Title(text = "Мои резюме", fontSize = 30.sp)
            Title(
                text = "Вы вошли как ${CurrentUser.info.username}",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            when {
                state.resumes == null -> LoadingComponent(description = "Загрузка профиля...")
                state.resumes.isEmpty() -> NoItemsCard()
                else -> LazyColumn {
                    items(state.resumes) {
                        ClickableResumeCard(
                            resume = it,
                            isStatusShown = true,
                            onClick = {
                                openResumeDetails(it.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

        }
    }
}