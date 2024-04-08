package com.kiryuha21.jobsearchplatformclient.data.domain

data class Resume(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var contactEmail: String = "",
    var applyPosition: String = "",
    var skills: List<Skill> = listOf(),
    var workExperience: List<WorkExperience> = listOf(),
    var publicationStatus: PublicationStatus = PublicationStatus.Published,
    var imageUrl: String? = null
) {
    fun fullName() = "$lastName $firstName"
}
