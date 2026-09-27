package com.example.agrosmart.domain.models

import java.util.Date
import java.util.UUID

data class DiagnosisHistory(
    var _id: String = UUID.randomUUID().toString(),
    var crop: Crop? = null,
    var diagnosisDate: Date = Date(),
    var deficiency: String = "",
    var image: ByteArray? = null,
    var recommendation: String? = null,
    var lastUpdate: Long? = null
) {
    // Compatibilidad Java para getter/setter id (getId / setId)
    var id: String
        get() = _id
        set(value) { _id = value }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DiagnosisHistory

        if (_id != other._id) return false
        if (crop != other.crop) return false
        if (diagnosisDate != other.diagnosisDate) return false
        if (deficiency != other.deficiency) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (recommendation != other.recommendation) return false
        if (lastUpdate != other.lastUpdate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + (crop?.hashCode() ?: 0)
        result = 31 * result + diagnosisDate.hashCode()
        result = 31 * result + deficiency.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + (recommendation?.hashCode() ?: 0)
        result = 31 * result + (lastUpdate?.hashCode() ?: 0)
        return result
    }

    class Builder {
        private var id: String = UUID.randomUUID().toString()
        private var crop: Crop? = null
        private var diagnosisDate: Date = Date()
        private var deficiency: String = ""
        private var image: ByteArray? = null
        private var recommendation: String? = null
        private var lastUpdate: Long? = null

        fun _id(id: String) = apply { this.id = id }
        fun id(id: String) = apply { this.id = id }
        fun Crop(crop: Crop?) = apply { this.crop = crop }
        fun crop(crop: Crop?) = apply { this.crop = crop }
        fun diagnosisDate(date: Date) = apply { this.diagnosisDate = date }
        fun deficiency(deficiency: String) = apply { this.deficiency = deficiency }
        fun image(image: ByteArray?) = apply { this.image = image }
        fun recommendation(rec: String?) = apply { this.recommendation = rec }
        fun lastUpdate(lastUpdate: Long?) = apply { this.lastUpdate = lastUpdate }

        fun build() = DiagnosisHistory(
            _id = id,
            crop = crop,
            diagnosisDate = diagnosisDate,
            deficiency = deficiency,
            image = image,
            recommendation = recommendation,
            lastUpdate = lastUpdate
        )
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()
    }
}
