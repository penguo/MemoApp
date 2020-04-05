package com.penguodev.memoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemoData(
        @PrimaryKey(autoGenerate = true) val id: Long?,
        val memo: String,
        val createTime: Long,
        val lastUpdateTime: Long
)