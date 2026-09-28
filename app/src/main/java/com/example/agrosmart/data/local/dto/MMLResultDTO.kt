package com.example.agrosmart.data.local.dto

class MMLResultDTO {
    @JvmField
    var result: String? = null
    @JvmField
    var inferenceTime: Long? = null
    @JvmField
    var memoryUse: Long? = null
    var inferenceData: FloatArray = floatArrayOf()

    constructor()
}
