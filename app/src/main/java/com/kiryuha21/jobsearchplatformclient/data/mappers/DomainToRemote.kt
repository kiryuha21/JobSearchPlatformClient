package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.BaseUser
import com.kiryuha21.jobsearchplatformclient.data.remote.BaseUserDTO

fun BaseUser.toBaseUserDTO() =
    BaseUserDTO(
        email = this.email,
        login = this.login,
        password = this.password,
        role = this.role
    )
