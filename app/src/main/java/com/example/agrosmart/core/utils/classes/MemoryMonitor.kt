package com.example.agrosmart.core.utils.classes

import android.os.Debug
import lombok.Getter

object MemoryMonitor {
    @JvmStatic
    val memorySnapshot: MemorySnapshot
        get() {
            val memoryInfo = Debug.MemoryInfo()

            Debug.getMemoryInfo(memoryInfo)

            val totalPss = memoryInfo.totalPss
                .toLong()
            val dalvikPss = memoryInfo.dalvikPss.toLong()
            val nativePss = memoryInfo.nativePss.toLong()
            val otherPss = memoryInfo.otherPss.toLong()

            return MemorySnapshot(
                    totalPss,
                    dalvikPss,
                    nativePss,
                    otherPss
            )
        }

    data class MemorySnapshot(// Total PSS en KB
        val totalPss: Long, // Heap de Java en KB
        val dalvikPss: Long, // Heap Nativo en KB
        val nativePss: Long, // Otras asignaciones en KB
        val otherPss: Long
    )
}
