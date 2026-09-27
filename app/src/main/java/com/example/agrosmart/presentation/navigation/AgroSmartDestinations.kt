package com.example.agrosmart.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class CropInfoRoute(
    val image: Int,
    val title: String,
    val description: String,
    val harvestTime: String,
    val type: String
)

@Serializable
object DeficienciesRoute

@Serializable
data class DeficiencyInfoRoute(
    val imageuri: String,
    val deficiencyName: String,
    val description: String,
    val symptoms: String,
    val solutions: String
)

@Serializable
object FertilizersRoute

@Serializable
data class FertilizerInfoRoute(
    val imageuri: String,
    val name: String,
    val description: String,
    val type: String,
    val provider: String,
    val applicationMethod: String,
    val recommendedDose: String
)

@Serializable
data class DetectionRoute(
    val result: String? = null,
    val imgPath: String? = null
)

@Serializable
object CameraRoute

@Serializable
data class DiagnosisInfoRoute(
    val idDiagnosis: String,
    val cropImage: String,
    val cropName: String,
    val diagnosisDate: String,
    val diagnosisName: String,
    val recommendation: String
)

@Serializable
object ProfileRoute

@Serializable
data class EditProfileRoute(
    val username: String = "invitado"
)

@Serializable
object ConfigRoute

@Serializable
object PersonalDataRoute
