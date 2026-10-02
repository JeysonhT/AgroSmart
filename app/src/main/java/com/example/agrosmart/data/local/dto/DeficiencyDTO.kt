package com.example.agrosmart.data.local.dto

data class DeficiencyDTO(
    val id: String? = null,
    val imageDeficiencies: String? = null,
    val title: String = "",
    val description: String = "",
    val symptoms: String = "",
    val solutions: String = ""
)
