package com.kiryuha21.jobsearchplatformclient.util

fun String.isNumeric() = this.all { it.isDigit() }