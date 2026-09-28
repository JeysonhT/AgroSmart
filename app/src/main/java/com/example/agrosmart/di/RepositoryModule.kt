package com.example.agrosmart.di

import com.example.agrosmart.data.repository.impl.DiagnosisHistoryLocalRepositoryImpl
import com.example.agrosmart.data.repository.impl.RecommendationServiceImpl
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository
import com.example.agrosmart.domain.repository.RecommendationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDiagnosisHistoryRepository(
        impl: DiagnosisHistoryLocalRepositoryImpl
    ): DiagnosisHistoryRepository

    @Binds
    @Singleton
    abstract fun bindRecommendationRepository(
        impl: RecommendationServiceImpl
    ): RecommendationRepository

    @Binds
    @Singleton
    abstract fun bindUserDtlRepository(
        impl: com.example.agrosmart.data.repository.impl.UserDtlimpl
    ): com.example.agrosmart.domain.repository.UserDtlRepository
}
