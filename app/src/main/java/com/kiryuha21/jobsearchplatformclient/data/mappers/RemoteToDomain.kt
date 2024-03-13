package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.BaseUser
import com.kiryuha21.jobsearchplatformclient.data.remote.BaseUserDTO

fun BaseUserDTO.toDomainUser() =
    BaseUser(
        email = this.email,
        username = this.username,
        password = this.password,
        role = this.role
    )
