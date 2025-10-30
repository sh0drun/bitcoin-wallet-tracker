package com.anymind.wallet.controller

import com.anymind.wallet.controller.dto.SaveTransactionRequest
import com.anymind.wallet.domain.Transaction
import com.anymind.wallet.repository.TransactionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.OffsetDateTime

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @BeforeEach
    fun setup() {
        transactionRepository.deleteAll()
    }

    @Test
    fun `should save transaction successfully`() {
        val request = SaveTransactionRequest(
            datetime = "2019-10-05T14:48:01+07:00",
            amount = BigDecimal("1.1")
        )

        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.datetime").value("2019-10-05T07:48:01Z"))
            .andExpect(jsonPath("$.amount").value(1.1))
    }

    @Test
    fun `should reject transaction with invalid datetime`() {
        val request = mapOf(
            "datetime" to "invalid-datetime",
            "amount" to 1.1
        )

        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject transaction with missing datetime`() {
        val request = mapOf(
            "amount" to 1.1
        )

        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject transaction with missing amount`() {
        val request = mapOf(
            "datetime" to "2019-10-05T14:48:01+07:00"
        )

        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should get balance history with no transactions`() {
        mockMvc.perform(
            get("/api/transactions/history")
                .param("startDatetime", "2019-10-05T12:00:00Z")
                .param("endDatetime", "2019-10-05T14:00:00Z")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].datetime").value("2019-10-05T12:00:00Z"))
            .andExpect(jsonPath("$[0].amount").value(1000))
            .andExpect(jsonPath("$[1].datetime").value("2019-10-05T13:00:00Z"))
            .andExpect(jsonPath("$[1].amount").value(1000))
            .andExpect(jsonPath("$[2].datetime").value("2019-10-05T14:00:00Z"))
            .andExpect(jsonPath("$[2].amount").value(1000))
    }

    @Test
    fun `should get balance history with transactions`() {
        transactionRepository.save(
            Transaction(
                datetime = OffsetDateTime.parse("2019-10-05T13:30:00Z"),
                amount = BigDecimal("50")
            )
        )

        transactionRepository.save(
            Transaction(
                datetime = OffsetDateTime.parse("2019-10-05T15:45:00Z"),
                amount = BigDecimal("25.5")
            )
        )

        mockMvc.perform(
            get("/api/transactions/history")
                .param("startDatetime", "2019-10-05T12:00:00Z")
                .param("endDatetime", "2019-10-05T16:00:00Z")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].datetime").value("2019-10-05T12:00:00Z"))
            .andExpect(jsonPath("$[0].amount").value(1000))
            .andExpect(jsonPath("$[1].datetime").value("2019-10-05T13:00:00Z"))
            .andExpect(jsonPath("$[1].amount").value(1050))
            .andExpect(jsonPath("$[2].datetime").value("2019-10-05T14:00:00Z"))
            .andExpect(jsonPath("$[2].amount").value(1050))
            .andExpect(jsonPath("$[3].datetime").value("2019-10-05T15:00:00Z"))
            .andExpect(jsonPath("$[3].amount").value(1075.5))
            .andExpect(jsonPath("$[4].datetime").value("2019-10-05T16:00:00Z"))
            .andExpect(jsonPath("$[4].amount").value(1075.5))
    }

    @Test
    fun `should reject history request with start after end`() {
        mockMvc.perform(
            get("/api/transactions/history")
                .param("startDatetime", "2019-10-05T16:00:00Z")
                .param("endDatetime", "2019-10-05T12:00:00Z")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should reject history request with invalid datetime format`() {
        mockMvc.perform(
            get("/api/transactions/history")
                .param("startDatetime", "invalid-date")
                .param("endDatetime", "2019-10-05T12:00:00Z")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should handle timezone conversion correctly`() {
        transactionRepository.save(
            Transaction(
                datetime = OffsetDateTime.parse("2019-10-05T14:48:01+07:00"),
                amount = BigDecimal("1.1")
            )
        )

        mockMvc.perform(
            get("/api/transactions/history")
                .param("startDatetime", "2019-10-05T07:00:00Z")
                .param("endDatetime", "2019-10-05T08:00:00Z")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].datetime").value("2019-10-05T07:00:00Z"))
            .andExpect(jsonPath("$[0].amount").value(1001.1))
            .andExpect(jsonPath("$[1].datetime").value("2019-10-05T08:00:00Z"))
            .andExpect(jsonPath("$[1].amount").value(1001.1))
    }
}
