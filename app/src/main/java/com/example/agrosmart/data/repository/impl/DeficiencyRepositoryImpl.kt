package com.example.agrosmart.data.repository.impl

import android.util.Log
import com.example.agrosmart.data.local.mappers.DeficiencyMapper
import com.example.agrosmart.di.IoDispatcher
import com.example.agrosmart.domain.models.Deficiency
import com.example.agrosmart.domain.repository.DeficiencyRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class DeficiencyRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DeficiencyRepository {

    override suspend fun getDeficiencies(): List<Deficiency> = withContext(ioDispatcher) {
        try {
            // Intenta obtener los documentos de la colección "Deficiencies"
            val querySnapshot = db.collection(COLLECTION_NAME)
                .get()
                .await()

            // Fallback por si la colección en Firestore fue creada con minúscula ("deficiencies")
            val snapshot = if (querySnapshot.isEmpty) {
                val fallbackSnapshot = db.collection(COLLECTION_NAME_FALLBACK)
                    .get()
                    .await()
                if (!fallbackSnapshot.isEmpty) fallbackSnapshot else querySnapshot
            } else {
                querySnapshot
            }

            val deficiencies = snapshot.documents.mapNotNull { doc ->
                DeficiencyMapper.fromDocument(doc)
            }
            Log.d(TAG, "Deficiencias obtenidas exitosamente: ${deficiencies.size}")
            deficiencies
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener deficiencias de Firestore: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun deficiencies(): List<Deficiency> = getDeficiencies()

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
        private const val TAG = "DEFICIENCIES_REPO_IMPL"
        private const val COLLECTION_NAME = "Deficiencies"
        private const val COLLECTION_NAME_FALLBACK = "deficiencies"
    }
}
