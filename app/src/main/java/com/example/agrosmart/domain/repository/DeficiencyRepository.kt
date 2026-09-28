package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.Deficiency
import java.util.concurrent.CompletableFuture

interface DeficiencyRepository {
    val deficiencies: CompletableFuture<MutableList<Deficiency?>?>?
}
