package com.example.agrosmart.domain.models

data class Crop(
    var cropName: String = "",
    var description: String = "",
    var harvestTime: String = "",
    var type: String = ""
) {
    // Constructor secundario para compatibilidad regresiva Java: (cropName, harvestTime)
    constructor(cropName: String, harvestTime: String) : this(
        cropName = cropName,
        description = harvestTime,
        harvestTime = "",
        type = ""
    )

    // Constructor secundario para compatibilidad regresiva Java: (cropName, description, harvestTime)
    constructor(cropName: String, description: String, harvestTime: String) : this(
        cropName = cropName,
        description = description,
        harvestTime = harvestTime,
        type = ""
    )
}
