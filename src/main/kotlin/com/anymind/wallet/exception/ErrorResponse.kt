package com.anymind.wallet.exception

import java.time.OffsetDateTime
import java.time.ZoneOffset

data class ErrorResponse(
    val status: Int,
    val message: String,
    val errors: List<String> = emptyList(),
    val timestamp: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
)
