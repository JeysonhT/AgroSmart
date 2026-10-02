package com.example.agrosmart.di

import android.content.Context
import com.example.agrosmart.ml.ModelAgrosmart
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MlModule {

    @Provides
    @Singleton
    fun provideMachineLearningModel(
        @ApplicationContext context: Context
    ): ModelAgrosmart {
        return ModelAgrosmart.newInstance(context)
    }

    @Provides
    @Singleton
    fun provideModelLabels(
        @ApplicationContext context: Context
    ): List<String> {
        return try {
            context.assets.open("labels.txt").bufferedReader().useLines { lines ->
                lines.filter { it.isNotBlank() }.toList()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}