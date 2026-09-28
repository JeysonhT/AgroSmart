package com.example.agrosmart.data.local.dto

import com.google.firebase.firestore.Exclude

class CropDTO {
    @JvmField
    var cropName: String? = null
    @JvmField
    var description: String? = null
    @JvmField
    var content: String? = null

    @JvmField
    @Exclude
    var type: String? = null

    // 🔑 Firestore necesita un constructor vacío
    constructor()

    constructor(
        cropName: String?,
        description: String?,
        content: String?,
        type: String?
    ) {
        this.cropName = cropName
        this.description = description
        this.content = content
        this.type = type
    }

}
