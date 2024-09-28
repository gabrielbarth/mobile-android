package com.gabrielbarth.contacts.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toFormattedPhone(): String = mapIndexed { index, char ->
    when {
        index == 0 -> "($char"
        index == 2 -> ") $char"
        (index == 6 && length < 11) ||
                (index == 7 && length == 11) -> "-$char"

        else -> char
    }
}.joinToString("")

fun BigDecimal.format(): String {
    val formatter = DecimalFormat("R$#,##0.00")
    return formatter.format(this)
}

fun LocalDate.format(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return format(formatter)
}