package ru.gavrilov.jna.platform.windows;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.win32.W32APIOptions;

public interface Cfgmgr32 extends Library {
    Cfgmgr32 INSTANCE = Native.loadLibrary("Cfgmgr32", Cfgmgr32.class, W32APIOptions.DEFAULT_OPTIONS);

    int CM_Get_Parent(IntByReference pdnDevInst, int dnDevInst, int ulFlags);

    int CM_Get_Child(IntByReference pdnDevInst, int dnDevInst, int ulFlags);

    int CM_Get_Sibling(IntByReference pdnDevInst, int dnDevInst, int ulFlags);

    int CM_Get_Device_ID(int devInst, char[] Buffer, int BufferLen, int ulFlags);

    int CM_Get_Device_ID_Size(NativeLongByReference pulLen, int dnDevInst, int ulFlags);
}
