package com.kiryuha21.jobsearchplatformclient.data.domain.pagination

fun resolveMoreItemsState(vacanciesSize: Int, isOnlineLoad: Boolean) =
    if (vacanciesSize < DEFAULT_PAGE_SIZE) {
        if (isOnlineLoad) {
            MoreItemsState.Unavailable
        } else {
            MoreItemsState.Unreachable
        }
    } else {
        MoreItemsState.Available
    }