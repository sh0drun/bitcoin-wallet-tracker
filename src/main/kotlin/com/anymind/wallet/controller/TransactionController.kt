package com.anymind.wallet.controller

import com.anymind.wallet.controller.dto.BalanceHistoryRequest
import com.anymind.wallet.controller.dto.BalanceSnapshot
import com.anymind.wallet.controller.dto.SaveTransactionRequest
import com.anymind.wallet.controller.dto.SaveTransactionResponse
import com.anymind.wallet.service.TransactionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping
    fun saveTransaction(
        @Valid @RequestBody request: SaveTransactionRequest
    ): ResponseEntity<SaveTransactionResponse> {
        val response = transactionService.saveTransaction(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/history")
    fun getBalanceHistory(
        @RequestParam startDatetime: String,
        @RequestParam endDatetime: String
    ): ResponseEntity<List<BalanceSnapshot>> {
        val request = BalanceHistoryRequest(startDatetime, endDatetime)
        val response = transactionService.getBalanceHistory(request)
        return ResponseEntity.ok(response)
    }
}
