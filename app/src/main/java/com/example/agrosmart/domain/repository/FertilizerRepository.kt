package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.Fertilizer
import java.util.concurrent.CompletableFuture

interface FertilizerRepository {
    val fertilizers: CompletableFuture<MutableList<Fertilizer?>?>?
}
