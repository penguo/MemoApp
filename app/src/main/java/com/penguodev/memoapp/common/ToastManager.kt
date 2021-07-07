package com.penguodev.memoapp.common

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ToastManager {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun toast(msg: String) {
        if (appContext != null) {
            Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()
        }
    }
}