package com.anymind.wallet.controller.dto

import java.math.BigDecimal
import java.time.OffsetDateTime

data class BalanceSnapshot(
    val datetime: OffsetDateTime,
    val amount: BigDecimal
)
