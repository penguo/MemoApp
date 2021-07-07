package com.penguodev.memoapp.ui.payment

import com.android.billingclient.api.SkuDetails

data class PaymentItem(
    val sku: String,
    val title: String,
    val price: String,
    val skuDetails: SkuDetails
)