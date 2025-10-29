package com.anymind.wallet.controller.dto

import java.math.BigDecimal
import java.time.OffsetDateTime

data class SaveTransactionResponse(
    val id: Long,
    val datetime: OffsetDateTime,
    val amount: BigDecimal,
    val message: String = "Transaction saved successfully"
)
