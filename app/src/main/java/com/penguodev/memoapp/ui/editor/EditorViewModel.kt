package com.penguodev.memoapp.ui.editor

import android.app.Application
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.penguodev.memoapp.common.SingleLiveEvent
import com.penguodev.memoapp.model.MemoData
import com.penguodev.memoapp.repository.local.MemoLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(private val app: Application, private val id: Long?) : AndroidViewModel(app) {
    var prevItem: MemoData? = null
    val memo = MutableLiveData<String>("")
    val createTime = MutableLiveData<Long>(null)
    val lastUpdateTime = MutableLiveData<Long>(null)

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val item = id?.let { MemoLocalDatabase.getInstance(app).memoDataDao.getItem(id) }
            withContext(Dispatchers.Default) {
                prevItem = item
                memo.postValue(item?.memo ?: "")
                createTime.postValue(item?.createTime)
                lastUpdateTime.postValue(item?.lastUpdateTime)
            }
        }
    }

    fun saveData() {
        viewModelScope.launch {
            val item = MemoData(id ?: 0,
                    memo.value?.trim() ?: "",
                    createTime.value ?: System.currentTimeMillis(),
                    System.currentTimeMillis())
            MemoLocalDatabase.getInstance(app).memoDataDao.insert(item)
            actionFinish.call()
        }
    }

    fun onClickSave(view: View?) {
        saveData()
    }

    fun onClickBack(view: View?) {
        actionBack.call()
    }

    val actionBack = SingleLiveEvent<Void>()
    val actionFinish = SingleLiveEvent<Void>()

    class Factory(private val app: Application, private val id: Long?) : ViewModelProvider.AndroidViewModelFactory(app) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditorViewModel(app, id) as T
        }
    }
}