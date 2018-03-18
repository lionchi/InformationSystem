package ru.gavrilov.jna.platform.windows.COM;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.LongByReference;

public class WbemClassObject extends Unknown {

    public WbemClassObject(Pointer pvInstance) {
        super(pvInstance);
    }

    public HRESULT Get(BSTR wszName, NativeLong lFlags, VARIANT.ByReference pVal, Pointer pvtType,
            LongByReference plFlavor) {
        // Get is 5th method of vtable for WbemClassObject in WbemCli.h
        return (HRESULT) _invokeNativeObject(4, new Object[] { getPointer(), wszName, lFlags, pVal, pvtType, plFlavor },
                HRESULT.class);
    }
}