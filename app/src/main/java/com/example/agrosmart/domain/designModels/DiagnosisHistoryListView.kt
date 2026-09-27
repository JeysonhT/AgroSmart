package com.example.agrosmart.domain.designModels

data class DiagnosisHistoryListView(
    var cropIcon: Int = 0,
    var deficiencyIcon: Int = 0,
    var id: String = "",
    var txtDate: String = "",
    var deficiency: String = "",
    var image: ByteArray? = null,
    private var _recommendation: String = "Recomendación no generada aun"
) {
    var recommendation: String?
        get() = _recommendation
        set(value) {
            _recommendation = if (value.isNullOrBlank()) {
                "Recomendación no generada aun"
            } else {
                value
            }
        }

    // Secondary constructor for parameterless instantiation in Java
    constructor() : this(0, 0, "", "", "", null, "Recomendación no generada aun")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DiagnosisHistoryListView

        if (cropIcon != other.cropIcon) return false
        if (deficiencyIcon != other.deficiencyIcon) return false
        if (id != other.id) return false
        if (txtDate != other.txtDate) return false
        if (deficiency != other.deficiency) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (_recommendation != other._recommendation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cropIcon
        result = 31 * result + deficiencyIcon
        result = 31 * result + id.hashCode()
        result = 31 * result + txtDate.hashCode()
        result = 31 * result + deficiency.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + _recommendation.hashCode()
        return result
    }
}
