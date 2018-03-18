package ru.gavrilov.jna.platform.windows;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD.SIZE_T;
import com.sun.jna.platform.win32.WinDef.DWORD;

import java.util.Arrays;
import java.util.List;

public interface Psapi extends com.sun.jna.platform.win32.Psapi {
    Psapi INSTANCE = Native.loadLibrary("Psapi", Psapi.class);

    // TODO: Submit this change to JNA Psapi class
    class PERFORMANCE_INFORMATION extends Structure {
        public DWORD cb;
        public SIZE_T CommitTotal;
        public SIZE_T CommitLimit;
        public SIZE_T CommitPeak;
        public SIZE_T PhysicalTotal;
        public SIZE_T PhysicalAvailable;
        public SIZE_T SystemCache;
        public SIZE_T KernelTotal;
        public SIZE_T KernelPaged;
        public SIZE_T KernelNonpaged;
        public SIZE_T PageSize;
        public DWORD HandleCount;
        public DWORD ProcessCount;
        public DWORD ThreadCount;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] { "cb", "CommitTotal", "CommitLimit", "CommitPeak", "PhysicalTotal",
                    "PhysicalAvailable", "SystemCache", "KernelTotal", "KernelPaged", "KernelNonpaged", "PageSize",
                    "HandleCount", "ProcessCount", "ThreadCount" });
        }
    }
    boolean GetPerformanceInfo(PERFORMANCE_INFORMATION pPerformanceInformation, int cb);
}
