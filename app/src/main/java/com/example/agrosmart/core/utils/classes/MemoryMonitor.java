package com.example.agrosmart.core.utils.classes;

import android.os.Debug;

import lombok.Getter;

public class MemoryMonitor {

    public MemoryMonitor(){}

    public static MemorySnapshot getMemorySnapshot(){
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();

        Debug.getMemoryInfo(memoryInfo);

        long totalPss = (long) memoryInfo.getTotalPss();
        long dalvikPss = (long) memoryInfo.dalvikPss;
        long nativePss = (long) memoryInfo.nativePss;
        long otherPss = (long) memoryInfo.otherPss;

        return new MemorySnapshot(totalPss, dalvikPss, nativePss, otherPss);
    }

    @Getter
    public static class MemorySnapshot {
        private final long totalPss; // Total PSS en KB
        private final long dalvikPss; // Heap de Java en KB
        private final long nativePss; // Heap Nativo en KB
        private final long otherPss;  // Otras asignaciones en KB

        public MemorySnapshot(long totalPss, long dalvikPss, long nativePss, long otherPss) {
            this.totalPss = totalPss;
            this.dalvikPss = dalvikPss;
            this.nativePss = nativePss;
            this.otherPss = otherPss;
        }

    }
}
