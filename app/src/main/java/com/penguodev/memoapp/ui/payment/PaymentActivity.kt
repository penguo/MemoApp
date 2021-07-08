package com.penguodev.memoapp.ui.payment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.android.billingclient.api.SkuDetails
import com.penguodev.memoapp.R
import com.penguodev.memoapp.databinding.ActivityPaymentBinding
import com.penguodev.memoapp.payment.GooglePaymentClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PaymentActivity : AppCompatActivity(), PaymentListAdapter.ItemClickListener {
    private lateinit var binding: ActivityPaymentBinding
    private var adapter: PaymentListAdapter? = null
    private var client: GooglePaymentClient? = null
    private val viewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        initPayment()

        adapter = PaymentListAdapter(this).also {
            binding.recyclerviewPayment.adapter = it
        }
    }

    private fun initPayment() {
        val listener = object : GooglePaymentClient.PaymentConnectListener {
            override fun onConnected(list: List<SkuDetails>) {
                adapter?.submitList(list.map {
                    PaymentItem(it.sku, it.sku, it.price, it)
                })
            }

            override fun onError(responseCode: Int, debugMessage: String) {
                Timber.e("error occurred: [$responseCode] $debugMessage")
            }

            override fun onDisconnected() {
            }

            override suspend fun requestSkuListFromServer(): List<String> {
                return viewModel.requestSkuList()
            }

            override suspend fun sendPurchasedSku(sku: String): Boolean {
                return viewModel.sendPurchasedSku(sku)
            }
        }
        client = GooglePaymentClient(this, listener)
    }

    override fun onClickBuy(item: PaymentItem) {
        lifecycleScope.launch {
            client?.startPaymentFlow(item.skuDetails)
        }
    }
}