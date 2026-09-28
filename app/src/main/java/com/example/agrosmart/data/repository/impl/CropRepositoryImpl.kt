package com.example.agrosmart.data.repository.impl

import android.util.Log
import com.example.agrosmart.di.IoDispatcher
import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.repository.CropRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
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
class CropRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CropRepository {

    override suspend fun getCrops(): List<Crop> = withContext(ioDispatcher) {
        try {
            val snapshot = db.collection("Crops").get().await()
            val crops = snapshot.documents.mapNotNull { doc -> mapDocumentToCrop(doc) }
            Log.d(TAG, "Cultivos obtenidos exitosamente: ${crops.size}")
            crops
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener cultivos de Firestore: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getCropByName(name: String?): List<Crop> = withContext(ioDispatcher) {
        if (name.isNullOrBlank()) {
            return@withContext emptyList()
        }

        try {
            val querySnapshot = db.collection("Crops")
                .whereEqualTo("name", name)
                .get()
                .await()

            val crops = querySnapshot.documents.mapNotNull { doc -> mapDocumentToCrop(doc) }

            // Si no encontró por el campo "name", intentar por el campo alternativo "cropName"
            crops.ifEmpty {
                val fallbackSnapshot = db.collection("Crops")
                    .whereEqualTo(
                            "cropName",
                            name
                    )
                    .get()
                    .await()

                fallbackSnapshot.documents.mapNotNull { doc -> mapDocumentToCrop(doc) }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener cultivo por nombre '$name': ${e.message}", e)
            emptyList()
        }
    }

    private fun mapDocumentToCrop(doc: DocumentSnapshot): Crop? {
        val name = doc.getString("name") ?: doc.getString("cropName") ?: return null
        return Crop(
            cropName = name,
            description = doc.getString("description").orEmpty(),
            harvestTime = doc.getString("harvestTime") ?: doc.getString("content").orEmpty(),
            type = doc.getString("type").orEmpty()
        )
    }

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
        private const val TAG = "CROP_REPOSITORY_IMPL"
    }
}
