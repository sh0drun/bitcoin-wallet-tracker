package com.anymind.wallet.service

import com.anymind.wallet.controller.dto.BalanceSnapshot
import com.anymind.wallet.domain.Transaction
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Service
class BalanceCalculator {

    companion object {
        private val INITIAL_BALANCE = BigDecimal("1000")
    }

    fun calculateHourlyBalances(
        startDatetime: OffsetDateTime,
        endDatetime: OffsetDateTime,
        transactions: List<Transaction>
    ): List<BalanceSnapshot> {
        val startHour = startDatetime.withOffsetSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.HOURS)
        val endHour = endDatetime.withOffsetSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.HOURS)

        val balances = mutableListOf<BalanceSnapshot>()
        var currentHour = startHour

        while (currentHour <= endHour) {
            val hourEnd = currentHour.plusHours(1)
            val transactionsBeforeHourEnd = transactions.filter { it.datetime < hourEnd }
            val totalAmount = transactionsBeforeHourEnd.sumOf { it.amount }

            balances.add(
                BalanceSnapshot(
                    datetime = currentHour,
                    amount = INITIAL_BALANCE + totalAmount
                )
            )

            currentHour = currentHour.plusHours(1)
        }

        return balances
    }
}
