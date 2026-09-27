package com.example.agrosmart

import android.app.Application
import androidx.room.Room
import com.example.agrosmart.data.local.room.AgroSmartDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AgroSmartApp : Application() {

    companion object {
        lateinit var instance: AgroSmartApp
            private set

        val database: AgroSmartDatabase by lazy {
            Room.databaseBuilder(
                instance,
                AgroSmartDatabase::class.java,
                AgroSmartDatabase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
