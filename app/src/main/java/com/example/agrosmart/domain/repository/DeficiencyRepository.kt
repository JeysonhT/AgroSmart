package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.Deficiency
import java.util.concurrent.CompletableFuture

interface DeficiencyRepository {
    suspend fun getDeficiencies(): List<Deficiency>
    suspend fun deficiencies(): List<Deficiency> = getDeficiencies()

}
