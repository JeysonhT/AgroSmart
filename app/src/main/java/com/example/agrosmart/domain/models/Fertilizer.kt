package com.example.agrosmart.domain.models

import java.util.UUID

data class Fertilizer(
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val imageResource: ByteArray? = null,
    val applicationMethod: String? = null,
    val recommendedDose: String? = null,
    val description: String? = null,
    val supplier: String? = null,
    val type: String? = null,
    val lastUpdate: Long? = null
) {
    fun get_id(): String? = id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fertilizer

        if (id != other.id) return false
        if (name != other.name) return false
        if (imageResource != null) {
            if (other.imageResource == null) return false
            if (!imageResource.contentEquals(other.imageResource)) return false
        } else if (other.imageResource != null) return false
        if (applicationMethod != other.applicationMethod) return false
        if (recommendedDose != other.recommendedDose) return false
        if (description != other.description) return false
        if (supplier != other.supplier) return false
        if (type != other.type) return false
        if (lastUpdate != other.lastUpdate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (imageResource?.contentHashCode() ?: 0)
        result = 31 * result + (applicationMethod?.hashCode() ?: 0)
        result = 31 * result + (recommendedDose?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (supplier?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (lastUpdate?.hashCode() ?: 0)
        return result
    }
}
