package com.anymind.wallet.service

import com.anymind.wallet.controller.dto.SaveTransactionRequest
import com.anymind.wallet.controller.dto.SaveTransactionResponse
import com.anymind.wallet.domain.Transaction
import com.anymind.wallet.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {

    fun saveTransaction(request: SaveTransactionRequest): SaveTransactionResponse {
        val parsedDatetime = OffsetDateTime.parse(request.datetime)
        val utcDatetime = parsedDatetime.withOffsetSameInstant(ZoneOffset.UTC)

        val transaction = Transaction(
            datetime = utcDatetime,
            amount = request.amount
        )

        val savedTransaction = transactionRepository.save(transaction)

        return SaveTransactionResponse(
            id = savedTransaction.id!!,
            datetime = savedTransaction.datetime,
            amount = savedTransaction.amount
        )
    }
}
