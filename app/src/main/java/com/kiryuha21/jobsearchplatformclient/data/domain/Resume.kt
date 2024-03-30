package com.kiryuha21.jobsearchplatformclient.data.domain

data class Resume(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val contactEmail: String,
    val applyPosition: String,
    val skills: List<Skill> = emptyList(),
    val workExperience: List<WorkExperience> = emptyList(),
    val imageUrl: String? = null
) {
    fun fullName() = "$lastName $firstName"
}
