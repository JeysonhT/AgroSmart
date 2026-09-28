package com.example.agrosmart.data.local.dto

data class FertilizerDTO(val name: String,
                         val imageFertilizers: String,
                         val applicationMethod: String,
                         val recommendedDose: String,
                         val description: String,
                         val supplier: String,
                         val type: String)