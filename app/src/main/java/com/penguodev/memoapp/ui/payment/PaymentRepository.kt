package com.penguodev.memoapp.ui.payment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class PaymentRepository {
    private var testPoint = 0

    suspend fun getSkuList(): List<String> = withContext(Dispatchers.IO) {
        delay(500) // 테스트용 딜레이
        return@withContext listOf(
                "point_100",
                "point_500",
                "point_1000"
        )
    }

    suspend fun sendPurchasedSku(sku: String): PaymentTestResult = withContext(Dispatchers.IO) {
        delay(500) // 테스트용 딜레이
        val result = when (sku) {
            "point_100" -> {
                testPoint += 100
                true
            }
            "point_500" -> {
                testPoint += 500
                true
            }
            "point_1000" -> {
                testPoint += 1000
                true
            }
            else -> {
                false
            }
        }
        return@withContext PaymentTestResult(result, testPoint)
    }
}

data class PaymentTestResult(
        val result: Boolean,
        val remainPoint: Int
)