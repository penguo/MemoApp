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
        private val activity: AppCompatActivity,
        private val listener: PaymentConnectListener
) : PurchasesUpdatedListener {

    private val billingClient by lazy {
        BillingClient.newBuilder(activity)
                .setListener(this)
                .enablePendingPurchases()
                .build()
    }

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(setupResult: BillingResult) {
                if (setupResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    activity.lifecycleScope.launch {
                        val skuDetailsResult = getSkuDetails()
                        if (skuDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            // TODO :: empty is error?
                            checkAlreadyPurchases() // 이미 구매한 것 체크
                            listener.onConnected(skuDetailsResult.skuDetailsList ?: emptyList())
                        } else {
                            listener.onError(skuDetailsResult.billingResult.responseCode, skuDetailsResult.billingResult.debugMessage)
                        }
                    }
                } else {
                    listener.onError(setupResult.responseCode, setupResult.debugMessage)
                }
            }

            /**
             * 참고: 자체 연결 재시도 로직을 구현하고 onBillingServiceDisconnected() 메서드를 재정의하는 것이 좋습니다. 메서드를 실행할 때 BillingClient 연결을 유지해야 합니다.
             */
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                listener.onDisconnected()
            }
        })
    }

    interface PaymentConnectListener {
        fun onConnected(list: List<SkuDetails>)

        fun onError(responseCode: Int, debugMessage: String)

        fun onDisconnected()

        suspend fun requestSkuListFromServer(): List<String>

        suspend fun sendPurchasedSku(sku: String): Boolean
    }

    fun startPaymentFlow(skuDetails: SkuDetails) {
        val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build()
        billingClient.launchBillingFlow(activity, flowParams).also {
            Timber.d(it.debugMessage)
        }
    }

    private fun checkAlreadyPurchases() {
        billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, ::onPurchasesUpdated)
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

                // TODO:: check - 충전 후 아이템 소비 처리? 아이템 소비 처리 후 충전?
                // 현재는 충전 후 아이템 소비 처리. purchaseToken을 서버에서 저장하고 있으면 만약의 중복 충전을 막을 수 있을듯.
                listener.sendPurchasedSku(it.skus.first()).also { result ->
                    if (result) billingClient.consumePurchase(params)
                }
            }
        }
    }

    // From GooglePlay Server
    private suspend fun getSkuDetails(): SkuDetailsResult {
        val params = SkuDetailsParams.newBuilder()
                .setSkusList(listener.requestSkuListFromServer())
                .setType(BillingClient.SkuType.INAPP)

        return withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }.also {
            Timber.d("getSkuDetails: ${it.billingResult.debugMessage}")
        }
    }
}