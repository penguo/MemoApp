package com.penguodev.memoapp.repository.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.penguodev.memoapp.model.MemoData
import retrofit2.http.DELETE

@Dao
interface MemoDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: MemoData)

    @Query("SELECT * FROM MemoData ORDER BY lastUpdateTime DESC")
    fun getList(): LiveData<List<MemoData>>

    @Query("SELECT * FROM MemoData WHERE id = :id")
    suspend fun getItem(id: Long): MemoData?

    @Query("DELETE FROM MemoData WHERE id = :id")
    suspend fun deleteItem(id: Long)
}