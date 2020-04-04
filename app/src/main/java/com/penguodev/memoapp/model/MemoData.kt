package com.penguodev.memoapp.model

import androidx.room.Entity

@Entity
data class MemoData(
    val id: Long,
    val memo: String,
    val createTime: Long
)