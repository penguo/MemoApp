package com.penguodev.memoapp.ui.payment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.penguodev.memoapp.R
import com.penguodev.memoapp.databinding.ActivityPaymentBinding
import com.penguodev.memoapp.payment.GooglePaymentClient
import kotlinx.coroutines.launch
import timber.log.Timber

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
        client = GooglePaymentClient(this).also { client ->
            client.init({ list ->
                adapter?.submitList(list.map {
                    PaymentItem(it.sku, it.sku, it.price, it)
                })
            }, {
                Timber.e("error occurred: [$it]")
            })
            client.point.observe(this, Observer {
                viewModel.point.value = it
            })
        }
    }

    override fun onClickBuy(item: PaymentItem) {
        lifecycleScope.launch {
            client?.startPaymentFlow(item.skuDetails)
        }
    }
}

class PaymentViewModel : ViewModel() {
    val point = MutableLiveData<Int>(0)
}