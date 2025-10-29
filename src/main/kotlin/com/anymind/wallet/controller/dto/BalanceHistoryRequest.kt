package com.anymind.wallet.controller.dto

import jakarta.validation.constraints.NotNull

data class BalanceHistoryRequest(
    @field:NotNull(message = "Start datetime is required")
    val startDatetime: String,

    @field:NotNull(message = "End datetime is required")
    val endDatetime: String
)
