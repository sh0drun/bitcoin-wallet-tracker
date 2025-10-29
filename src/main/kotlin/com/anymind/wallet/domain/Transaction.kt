package com.anymind.wallet.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val datetime: OffsetDateTime,

    @Column(nullable = false, precision = 20, scale = 8)
    val amount: BigDecimal,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
)
