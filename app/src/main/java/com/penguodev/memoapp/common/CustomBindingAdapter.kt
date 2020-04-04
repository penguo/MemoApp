package com.penguodev.memoapp.common

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("date_format", "date_format_time", "date_format_default", requireAll = false)
fun setDateFormat(view: TextView, format: String?, time: Long?, default: String?) {
    if (time != null) {
        view.text = SimpleDateFormat(format ?: "", Locale.getDefault()).format(time)
    } else {
        view.text = default ?: ""
    }
}