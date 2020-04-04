package com.penguodev.memoapp.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.penguodev.memoapp.model.MemoData
import com.penguodev.memoapp.repository.local.dao.MemoDataDao

@Database(
    entities = [MemoData::class],
    version = 1
)
abstract class MemoLocalDatabase : RoomDatabase() {
    abstract val memoDataDao: MemoDataDao

    companion object {
        private const val DB_NAME = "MemoDB.db"
        private var INSTANCE: MemoLocalDatabase? = null
        fun getInstance(context: Context): MemoLocalDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MemoLocalDatabase::class.java,
                    DB_NAME
                ).build()
            }
            return INSTANCE!!
        }
    }
}