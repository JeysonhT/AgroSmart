package com.example.agrosmart.di

import android.content.Context
import androidx.room.Room
import com.example.agrosmart.AgroSmartApp
import com.example.agrosmart.data.local.room.AgroSmartDatabase
import com.example.agrosmart.data.local.room.dao.DiagnosisHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAgroSmartDatabase(
        @ApplicationContext context: Context
    ): AgroSmartDatabase {
        return AgroSmartApp.database
    }

    @Provides
    fun provideDiagnosisHistoryDao(database: AgroSmartDatabase): DiagnosisHistoryDao {
        return database.diagnosisHistoryDao()
    }
}
