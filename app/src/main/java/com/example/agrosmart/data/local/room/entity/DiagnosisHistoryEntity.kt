package com.example.agrosmart.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "diagnosis_history")
data class DiagnosisHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "crop_name")
    val cropName: String = "",

    @ColumnInfo(name = "crop_description")
    val cropDescription: String = "",

    @ColumnInfo(name = "crop_harvest_time")
    val cropHarvestTime: String = "",

    @ColumnInfo(name = "crop_type")
    val cropType: String = "",

    @ColumnInfo(name = "diagnosis_date")
    val diagnosisDate: Date = Date(),

    @ColumnInfo(name = "deficiency")
    val deficiency: String = "",

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray? = null,

    @ColumnInfo(name = "recommendation")
    val recommendation: String? = null,

    @ColumnInfo(name = "last_update")
    val lastUpdate: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DiagnosisHistoryEntity

        if (id != other.id) return false
        if (cropName != other.cropName) return false
        if (diagnosisDate != other.diagnosisDate) return false
        if (deficiency != other.deficiency) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + cropName.hashCode()
        result = 31 * result + diagnosisDate.hashCode()
        result = 31 * result + deficiency.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}
