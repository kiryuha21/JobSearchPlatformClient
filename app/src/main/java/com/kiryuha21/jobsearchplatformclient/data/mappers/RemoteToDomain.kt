package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.Worker
import com.kiryuha21.jobsearchplatformclient.data.remote.UserDTO

// TODO: rework
fun UserDTO.toDomainUser(): Worker {
    return Worker(
        this.email,
        this.login,
        emptyList()
    )
}