package com.example.agrosmart.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.agrosmart.data.local.room.converters.DateConverters
import com.example.agrosmart.data.local.room.dao.DiagnosisHistoryDao
import com.example.agrosmart.data.local.room.entity.DiagnosisHistoryEntity

@Database(
    entities = [DiagnosisHistoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AgroSmartDatabase : RoomDatabase() {
    abstract fun diagnosisHistoryDao(): DiagnosisHistoryDao

    companion object {
        const val DATABASE_NAME = "agrosmart_room.db"
    }
}
