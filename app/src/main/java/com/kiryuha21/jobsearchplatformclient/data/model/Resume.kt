package com.kiryuha21.jobsearchplatformclient.data.model

data class Resume(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val contactEmail: String,
    val skills: List<Skill>,
    val workExperience: List<WorkExperience>
)
