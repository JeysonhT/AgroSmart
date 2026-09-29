package com.example.agrosmart.data.repository.impl

import android.util.Log
import com.example.agrosmart.data.local.mappers.FertilizerMapper
import com.example.agrosmart.di.IoDispatcher
import com.example.agrosmart.domain.models.Fertilizer
import com.example.agrosmart.domain.repository.FertilizerRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FertilizerRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FertilizerRepository {

    override suspend fun getFertilizers(): List<Fertilizer> = withContext(ioDispatcher) {
        try {
            // Intenta obtener los documentos de la colección "Fertilizers"
            val querySnapshot = db.collection(COLLECTION_NAME)
                .get()
                .await()

            // Fallback por si la colección en Firestore fue creada con minúscula ("fertilizers")
            val snapshot = if (querySnapshot.isEmpty) {
                val fallbackSnapshot = db.collection(COLLECTION_NAME_FALLBACK)
                    .get()
                    .await()
                if (!fallbackSnapshot.isEmpty) fallbackSnapshot else querySnapshot
            } else {
                querySnapshot
            }

            val fertilizers = snapshot.documents.mapNotNull { doc ->
                FertilizerMapper.fromDocument(doc)
            }
            Log.d(TAG, "Fertilizantes obtenidos exitosamente: ${fertilizers.size}")
            fertilizers
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener fertilizantes de Firestore: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun fertilizers(): List<Fertilizer> = getFertilizers()

    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            if (continuation.isActive) {
                continuation.resume(result)
            }
        }
        addOnFailureListener { exception ->
            if (continuation.isActive) {
                continuation.resumeWithException(exception)
            }
        }
        addOnCanceledListener {
            continuation.cancel()
        }
    }

    companion object {
        private const val TAG = "FERTILIZER_REPO_IMPL"
        private const val COLLECTION_NAME = "Fertilizers"
        private const val COLLECTION_NAME_FALLBACK = "fertilizers"
    }
}
