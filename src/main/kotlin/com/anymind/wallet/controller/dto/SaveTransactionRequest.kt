package com.anymind.wallet.controller.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class SaveTransactionRequest(
    @field:NotNull(message = "Datetime is required")
    val datetime: String?,

    @field:NotNull(message = "Amount is required")
    @field:DecimalMin(value = "0.00000001", message = "Amount must be positive")
    @field:Digits(integer = 12, fraction = 8, message = "Amount must have maximum 8 decimal places")
    val amount: BigDecimal?
)
