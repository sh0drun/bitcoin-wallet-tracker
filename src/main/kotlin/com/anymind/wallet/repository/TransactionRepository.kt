package com.anymind.wallet.repository

import com.anymind.wallet.domain.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {

    @Query("""
        SELECT t FROM Transaction t
        WHERE t.datetime < :endDatetime
        ORDER BY t.datetime
    """)
    fun findAllBeforeOrderByDatetime(@Param("endDatetime") endDatetime: OffsetDateTime): List<Transaction>
}
