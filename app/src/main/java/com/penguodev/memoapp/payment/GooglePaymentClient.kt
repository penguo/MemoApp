package com.penguodev.memoapp.payment

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class GooglePaymentClient(
    private val activity: AppCompatActivity
) : PurchasesUpdatedListener {

    // TEMP
    val point = MutableLiveData<Int>()
    private suspend fun addPoint(p: Int) = withContext(Dispatchers.IO) {
        point.postValue((point.value ?: 0) + p)
    }

    private val billingClient by lazy {
        BillingClient.newBuilder(activity)
            .setListener(this)
            .enablePendingPurchases()
            .build()
    }

    fun init(onOk: (list: List<SkuDetails>) -> Unit, onError: (responseCode: Int) -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    activity.lifecycleScope.launch {
                        val skuDetailsResult = getSkuDetails()
                        if (skuDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            // TODO :: empty is error?
                            onOk(skuDetailsResult.skuDetailsList ?: emptyList())
                        } else {
                            onError(billingResult.responseCode)
                        }
                    }
                } else {
                    onError(billingResult.responseCode)
                }

            }

            /**
             * 참고: 자체 연결 재시도 로직을 구현하고 onBillingServiceDisconnected() 메서드를 재정의하는 것이 좋습니다. 메서드를 실행할 때 BillingClient 연결을 유지해야 합니다.
             */
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    fun startPaymentFlow(skuDetails: SkuDetails) {
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        billingClient.launchBillingFlow(activity, flowParams).also {
            Timber.d(it.debugMessage)
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchased: MutableList<Purchase>?
    ) {
        Timber.d("onPurchasesUpdated: ${billingResult.debugMessage}")
        activity.lifecycleScope.launch {
            purchased?.forEach {
                Timber.d("purchased: ${it}")
                val params = ConsumeParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()

                // give point
                sendPurchase(it.skus.first())
                billingClient.consumePurchase(params)
            }
        }
    }

    // To Server
    private suspend fun sendPurchase(sku: String) {
        when (sku) {
            "point_100" -> addPoint(100)
            "point_500" -> addPoint(500)
            "point_1000" -> addPoint(1000)
        }
    }

    // From Server
    private suspend fun getSkuList(): List<String> {
        return withContext(Dispatchers.IO) {
            listOf(
                "point_100",
                "point_500",
                "point_1000"
            )
        }
    }

    // From GooglePlay Server
    private suspend fun getSkuDetails(): SkuDetailsResult {
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(getSkuList())
            .setType(BillingClient.SkuType.INAPP)

        return withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }.also {
            Timber.d("getSkuDetails: ${it.billingResult.debugMessage}")
        }
    }
}