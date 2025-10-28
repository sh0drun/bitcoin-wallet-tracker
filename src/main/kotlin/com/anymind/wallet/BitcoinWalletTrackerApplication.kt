package com.anymind.wallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BitcoinWalletTrackerApplication

fun main(args: Array<String>) {
	runApplication<BitcoinWalletTrackerApplication>(*args)
}
