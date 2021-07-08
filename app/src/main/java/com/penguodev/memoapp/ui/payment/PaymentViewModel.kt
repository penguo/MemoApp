package com.penguodev.memoapp.ui.payment

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PaymentViewModel : ViewModel() {
    private val _point = MutableLiveData(0)
    val point: LiveData<Int> = _point

//    private val _loading = MutableLiveData(false)
//    val loading: LiveData<Boolean> = _loading

    // loading이 겹치는 경우 이전 loading 종료 시 오류상황 발생하여 수정
    private val _loading = MutableLiveData<Set<String>>(setOf())
    val loading: LiveData<Boolean> = Transformations.map(_loading) {
        it.isNotEmpty()
    }

    private fun addLoading(key: String) {
        _loading.value = (_loading.value ?: mutableSetOf()).toMutableSet().apply { add(key) }
    }

    private fun consumeLoading(key: String) {
        _loading.value = (_loading.value ?: mutableSetOf()).toMutableSet().apply { remove(key) }
    }

    private val repository = PaymentRepository()

    suspend fun requestSkuList(): List<String> = suspendCoroutine {
        viewModelScope.launch {
            addLoading("get")
            val list = repository.getSkuList()
            it.resume(list)
            consumeLoading("get")
        }
    }

    suspend fun sendPurchasedSku(sku: String): Boolean = suspendCoroutine {
        viewModelScope.launch {
            addLoading("send_$sku")
            val response = repository.sendPurchasedSku(sku)
            _point.value = response.remainPoint
            it.resume(response.result)
            consumeLoading("send_$sku")
        }
    }
}
