package com.example.agrosmart.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.agrosmart.data.local.room.entity.DiagnosisHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisHistoryDao {

    @Query("SELECT * FROM diagnosis_history ORDER BY diagnosis_date DESC")
    fun getAllHistoriesFlow(): Flow<List<DiagnosisHistoryEntity>>

    @Query("SELECT * FROM diagnosis_history ORDER BY diagnosis_date DESC")
    suspend fun getAllHistories(): List<DiagnosisHistoryEntity>

    @Query("SELECT * FROM diagnosis_history ORDER BY diagnosis_date DESC LIMIT :limit")
    suspend fun getRecentHistories(limit: Int = 10): List<DiagnosisHistoryEntity>

    @Query("SELECT * FROM diagnosis_history ORDER BY diagnosis_date DESC LIMIT 1")
    fun getLastDiagnosisFlow(): Flow<DiagnosisHistoryEntity?>

    @Query("SELECT * FROM diagnosis_history ORDER BY diagnosis_date DESC LIMIT 1")
    suspend fun getLastDiagnosis(): DiagnosisHistoryEntity?

    @Query("SELECT * FROM diagnosis_history WHERE id = :id")
    suspend fun getDiagnosisById(id: String): DiagnosisHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diagnosis: DiagnosisHistoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(diagnoses: List<DiagnosisHistoryEntity>)

    @Update
    suspend fun update(diagnosis: DiagnosisHistoryEntity): Int

    @Query("UPDATE diagnosis_history SET recommendation = :recommendation WHERE id = :id")
    suspend fun updateRecommendation(id: String, recommendation: String): Int

    @Delete
    suspend fun delete(diagnosis: DiagnosisHistoryEntity): Int

    @Query("DELETE FROM diagnosis_history WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM diagnosis_history")
    suspend fun deleteAll(): Int
}
