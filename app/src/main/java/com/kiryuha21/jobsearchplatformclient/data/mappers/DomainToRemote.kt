package com.kiryuha21.jobsearchplatformclient.data.mappers

import com.kiryuha21.jobsearchplatformclient.data.domain.BaseUser
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.BaseUserDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.dto.TokenDTO

fun BaseUser.toBaseUserDTO() =
    BaseUserDTO(
        email = this.email,
        username = this.username,
        password = this.password,
        role = this.role
    )

fun BaseUser.toTokenRequestDTO() =
    TokenDTO.TokenCreateRequestDTO(
        username = this.username,
        password = this.password
    )
