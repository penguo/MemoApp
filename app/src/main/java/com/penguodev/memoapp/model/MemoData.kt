package com.penguodev.memoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class MemoData(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val memo: String,
    val createTime: Long,
    val lastUpdateTime: Long
) {

    fun getCaption(): String {
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(lastUpdateTime)
            ?: "-"
        return "${memo.length} length | $time updated"
    }
}