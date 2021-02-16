package com.penguodev.memoapp.ui.home

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.penguodev.memoapp.common.SingleLiveEvent
import com.penguodev.memoapp.repository.local.MemoLocalDatabase
import kotlinx.coroutines.launch

class HomeViewModel(private val app: Application) : AndroidViewModel(app) {
    val itemList = MemoLocalDatabase.getInstance(app).memoDataDao.getList()

    val actionOpenEditor = SingleLiveEvent<Void>()

    fun onClickEditor(view: View) {
        actionOpenEditor.call()
    }

    fun removeItem(id: Long) {
        viewModelScope.launch {
            MemoLocalDatabase.getInstance(app).memoDataDao.deleteItem(id)
        }
    }
}