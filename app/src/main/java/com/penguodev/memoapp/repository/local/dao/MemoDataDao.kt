package com.penguodev.memoapp.repository.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.penguodev.memoapp.model.MemoData

@Dao
interface MemoDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: MemoData)

    @Query("SELECT * FROM MemoData")
    fun getList(): LiveData<List<MemoData>>

    @Query("SELECT * FROM MemoData WHERE id = :id")
    suspend fun getItem(id: Long): MemoData?
}