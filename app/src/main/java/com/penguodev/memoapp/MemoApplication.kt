package com.penguodev.memoapp

import androidx.multidex.MultiDexApplication
import com.penguodev.memoapp.common.ToastManager
import timber.log.Timber

class MemoApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ToastManager.init(this)
    }
}