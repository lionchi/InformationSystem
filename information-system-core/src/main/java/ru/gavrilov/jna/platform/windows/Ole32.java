package ru.gavrilov.jna.platform.windows;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.win32.W32APIOptions;

public interface Ole32 extends com.sun.jna.platform.win32.Ole32 {
    Ole32 INSTANCE = Native.loadLibrary("Ole32", Ole32.class, W32APIOptions.DEFAULT_OPTIONS);

    int RPC_C_AUTHN_LEVEL_DEFAULT = 0;
    int RPC_C_AUTHN_WINNT = 10;
    int RPC_C_IMP_LEVEL_IMPERSONATE = 3;
    int RPC_C_AUTHZ_NONE = 0;
    int RPC_C_AUTHN_LEVEL_CALL = 3;

    int RPC_E_TOO_LATE = 0x80010119;
    int RPC_E_CHANGED_MODE = 0x80010106;

    int EOAC_NONE = 0;

    HRESULT CoInitializeSecurity(Pointer pSecDesc, NativeLong cAuthSvc, Pointer asAuthSvc, Pointer pReserved1,
                                 int dwAuthnLevel, int dwImpLevel, Pointer pAuthList, int dwCapabilities, Pointer pReserved3);

    HRESULT CoSetProxyBlanket(Pointer pProxy, //
                              int dwAuthnSvc, //
                              int dwAuthzSvc, //
                              BSTR pServerPrincName, // OLECHAR
                              int dwAuthnLevel, //
                              int dwImpLevel, //
                              Pointer pAuthInfo, // RPC_AUTH_IDENTITY_HANDLE
                              int dwCapabilities//
    );

}
