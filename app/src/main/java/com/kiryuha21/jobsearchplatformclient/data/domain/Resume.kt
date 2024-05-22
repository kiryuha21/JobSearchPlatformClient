package com.kiryuha21.jobsearchplatformclient.data.domain

import com.kiryuha21.jobsearchplatformclient.util.isNumeric

data class Resume(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: Long? = null,
    val phoneNumber: String = "",
    val contactEmail: String = "",
    val applyPosition: String = "",
    val skills: List<Skill> = emptyList(),
    val workExperience: List<WorkExperience> = emptyList(),
    val publicationStatus: PublicationStatus = PublicationStatus.Published,
    val imageUrl: String? = null
) {
    fun fullName() = "$lastName $firstName"

    fun isValid(): Boolean =
        firstName.isNotBlank() &&
        lastName.isNotBlank() &&
        birthDate != null &&
        phoneNumber.isNotBlank() && phoneNumber.isNumeric() &&
        contactEmail.isNotBlank() &&
        applyPosition.isNotBlank()
}
