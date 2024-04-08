package com.kiryuha21.jobsearchplatformclient.data.domain

data class Resume(
    var id: String,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var contactEmail: String,
    var applyPosition: String,
    var skills: MutableList<Skill> = mutableListOf(),
    var workExperience: MutableList<WorkExperience> = mutableListOf(),
    var publicationStatus: PublicationStatus,
    var imageUrl: String? = null
) {
    fun fullName() = "$lastName $firstName"
}
