package com.kiryuha21.jobsearchplatformclient.util

import java.util.Locale

fun String.isNumeric() = this.all { it.isDigit() }

fun String.isValidNullableNum() = this.isEmpty() || this.isNumeric()

fun String.isValidNullableAge() = this.isEmpty() || (this.isNumeric() && this.toInt() > 0)

fun Int.asFormattedSalary() = String.format(Locale.US, "%,d", this)

fun String.asFormattedSalary() = this.toInt().asFormattedSalary()