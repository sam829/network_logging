package com.sam.network_logger.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sam.network_logger.data.source.local.dao.NetworkCallDao
import com.sam.network_logger.data.source.local.entity.NetworkCall

@Database(
    entities = [NetworkCall::class],
    version = 1,
    exportSchema = false
)
abstract class LoggerDatabase : RoomDatabase() {
    abstract fun networkCallDao(): NetworkCallDao

    companion object {
        private var INSTANCE: LoggerDatabase? = null

        fun getDatabase(context: Context): LoggerDatabase = INSTANCE ?: synchronized(this) {
            return INSTANCE ?: Room.databaseBuilder(context, LoggerDatabase::class.java, "logs_db")
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}