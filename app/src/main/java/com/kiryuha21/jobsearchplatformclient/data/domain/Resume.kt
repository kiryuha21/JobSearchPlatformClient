package com.kiryuha21.jobsearchplatformclient.data.domain

data class Resume(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val contactEmail: String,
    val applyPosition: String,
    val skills: List<Skill> = emptyList(),
    val workExperience: List<WorkExperience> = emptyList()
) {
    fun fullName() = "$lastName $firstName"
}
