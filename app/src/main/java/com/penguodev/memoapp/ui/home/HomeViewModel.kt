package com.penguodev.memoapp.ui.home

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.penguodev.memoapp.common.SingleLiveEvent
import com.penguodev.memoapp.repository.local.MemoLocalDatabase

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    val itemList = MemoLocalDatabase.getInstance(app).memoDataDao.getList()

    val actionOpenEditor = SingleLiveEvent<Void>()

    fun onClickEditor(view: View) {
        actionOpenEditor.call()
    }
}