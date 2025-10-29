package com.anymind.wallet.service

import com.anymind.wallet.domain.Transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneOffset

class BalanceCalculatorTest {

    private val balanceCalculator = BalanceCalculator()

    @Test
    fun `should return initial balance when no transactions`() {
        val start = OffsetDateTime.parse("2019-10-05T12:00:00Z")
        val end = OffsetDateTime.parse("2019-10-05T14:00:00Z")

        val result = balanceCalculator.calculateHourlyBalances(start, end, emptyList())

        assertEquals(3, result.size)
        assertEquals(BigDecimal("1000"), result[0].amount)
        assertEquals(BigDecimal("1000"), result[1].amount)
        assertEquals(BigDecimal("1000"), result[2].amount)
    }

    @Test
    fun `should calculate balance with single transaction`() {
        val start = OffsetDateTime.parse("2019-10-05T12:00:00Z")
        val end = OffsetDateTime.parse("2019-10-05T14:00:00Z")

        val transaction = Transaction(
            datetime = OffsetDateTime.parse("2019-10-05T13:30:00Z"),
            amount = BigDecimal("50")
        )

        val result = balanceCalculator.calculateHourlyBalances(start, end, listOf(transaction))

        assertEquals(3, result.size)
        assertEquals(BigDecimal("1000"), result[0].amount)
        assertEquals(BigDecimal("1050"), result[1].amount)
        assertEquals(BigDecimal("1050"), result[2].amount)
    }

    @Test
    fun `should convert timezone to UTC`() {
        val start = OffsetDateTime.parse("2019-10-05T12:00:00+07:00")
        val end = OffsetDateTime.parse("2019-10-05T14:00:00+07:00")

        val result = balanceCalculator.calculateHourlyBalances(start, end, emptyList())

        assertEquals(OffsetDateTime.parse("2019-10-05T05:00:00Z"), result[0].datetime)
        assertEquals(OffsetDateTime.parse("2019-10-05T06:00:00Z"), result[1].datetime)
        assertEquals(OffsetDateTime.parse("2019-10-05T07:00:00Z"), result[2].datetime)
    }
}
