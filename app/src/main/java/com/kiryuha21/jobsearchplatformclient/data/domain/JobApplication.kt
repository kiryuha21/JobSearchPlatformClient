package com.kiryuha21.jobsearchplatformclient.data.domain

data class JobApplication(
    val id: String,
    val senderInitials: String,
    val referenceResumeId: String,
    val referenceVacancyId: String,
    val message: String,
    val seen: Boolean
)