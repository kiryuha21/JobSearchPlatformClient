package com.kiryuha21.jobsearchplatformclient.data.domain

enum class PublicationStatus {
    Draft,
    Published,
    Hidden;

    override fun toString(): String {
        return when (this) {
            Draft -> "Черновик"
            Published -> "Опубликовано"
            Hidden -> "Скрыто"
        }
    }
}

val descriptionToPublicationStatus = mapOf(
    "Опубликовано" to PublicationStatus.Published,
    "Черновик" to PublicationStatus.Draft,
    "Скрыто" to PublicationStatus.Hidden
)