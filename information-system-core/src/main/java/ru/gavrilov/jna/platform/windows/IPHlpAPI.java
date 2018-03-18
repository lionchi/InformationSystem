package ru.gavrilov.jna.platform.windows;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.WinDef.UCHAR;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinDef.ULONGByReference;

import java.util.Arrays;
import java.util.List;

public interface IPHlpAPI extends Library {
    IPHlpAPI INSTANCE = Native.loadLibrary("IPHlpAPI", IPHlpAPI.class);

    int IF_MAX_STRING_SIZE = 256;
    int IF_MAX_PHYS_ADDRESS_LENGTH = 32;
    int MAX_INTERFACE_NAME_LEN = 256;
    int MAXLEN_IFDESCR = 256;
    int MAXLEN_PHYSADDR = 8;
    int MAX_HOSTNAME_LEN = 128;
    int MAX_DOMAIN_NAME_LEN = 128;
    int MAX_SCOPE_ID_LEN = 256;

    int ERROR_BUFFER_OVERFLOW = 0x6f;

    class MIB_IFROW extends Structure {
        public char[] wszName = new char[MAX_INTERFACE_NAME_LEN];
        public int dwIndex;
        public int dwType;
        public int dwMtu;
        public int dwSpeed;
        public int dwPhysAddrLen;
        public byte[] bPhysAddr = new byte[MAXLEN_PHYSADDR];
        public int dwAdminStatus;
        public int dwOperStatus;
        public int dwLastChange;
        public int dwInOctets;
        public int dwInUcastPkts;
        public int dwInNUcastPkts;
        public int dwInDiscards;
        public int dwInErrors;
        public int dwInUnknownProtos;
        public int dwOutOctets;
        public int dwOutUcastPkts;
        public int dwOutNUcastPkts;
        public int dwOutDiscards;
        public int dwOutErrors;
        public int dwOutQLen;
        public int dwDescrLen;
        public byte[] bDescr = new byte[MAXLEN_IFDESCR];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] { "wszName", "dwIndex", "dwType", "dwMtu", "dwSpeed", "dwPhysAddrLen",
                    "bPhysAddr", "dwAdminStatus", "dwOperStatus", "dwLastChange", "dwInOctets", "dwInUcastPkts",
                    "dwInNUcastPkts", "dwInDiscards", "dwInErrors", "dwInUnknownProtos", "dwOutOctets",
                    "dwOutUcastPkts", "dwOutNUcastPkts", "dwOutDiscards", "dwOutErrors", "dwOutQLen", "dwDescrLen",
                    "bDescr" });
        }
    }

    class MIB_IFROW2 extends Structure {
        public long InterfaceLuid; // 64-bit union
        public ULONG InterfaceIndex;
        public GUID InterfaceGuid;
        public char[] Alias = new char[IF_MAX_STRING_SIZE + 1];
        public char[] Description = new char[IF_MAX_STRING_SIZE + 1];
        public ULONG PhysicalAddressLength;
        public UCHAR[] PhysicalAddress = new UCHAR[IF_MAX_PHYS_ADDRESS_LENGTH];
        public UCHAR[] PermanentPhysicalAddress = new UCHAR[IF_MAX_PHYS_ADDRESS_LENGTH];
        public ULONG Mtu;
        public ULONG Type;
        // enums
        public int TunnelType;
        public int MediaType;
        public int PhysicalMediumType;
        public int AccessType;
        public int DirectionType;
        // 8-bit structure
        public byte InterfaceAndOperStatusFlags;
        // enums
        public int OperStatus;
        public int AdminStatus;
        public int MediaConnectState;
        public GUID NetworkGuid;
        public int ConnectionType;
        public long TransmitLinkSpeed;
        public long ReceiveLinkSpeed;
        public long InOctets;
        public long InUcastPkts;
        public long InNUcastPkts;
        public long InDiscards;
        public long InErrors;
        public long InUnknownProtos;
        public long InUcastOctets;
        public long InMulticastOctets;
        public long InBroadcastOctets;
        public long OutOctets;
        public long OutUcastPkts;
        public long OutNUcastPkts;
        public long OutDiscards;
        public long OutErrors;
        public long OutUcastOctets;
        public long OutMulticastOctets;
        public long OutBroadcastOctets;
        public long OutQLen;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] { "InterfaceLuid", "InterfaceIndex", "InterfaceGuid", "Alias",
                    "Description", "PhysicalAddressLength", "PhysicalAddress", "PermanentPhysicalAddress", "Mtu",
                    "Type", "TunnelType", "MediaType", "PhysicalMediumType", "AccessType", "DirectionType",
                    "InterfaceAndOperStatusFlags", "OperStatus", "AdminStatus", "MediaConnectState", "NetworkGuid",
                    "ConnectionType", "TransmitLinkSpeed", "ReceiveLinkSpeed", "InOctets", "InUcastPkts",
                    "InNUcastPkts", "InDiscards", "InErrors", "InUnknownProtos", "InUcastOctets", "InMulticastOctets",
                    "InBroadcastOctets", "OutOctets", "OutUcastPkts", "OutNUcastPkts", "OutDiscards", "OutErrors",
                    "OutUcastOctets", "OutMulticastOctets", "OutBroadcastOctets", "OutQLen" });
        }
    }

    class IP_ADDRESS_STRING extends Structure {
        public byte[] String = new byte[16];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] { "String" });
        }
    }

    class IP_ADDR_STRING extends Structure {
        public ByReference Next;
        public IP_ADDRESS_STRING IpAddress;
        public IP_ADDRESS_STRING IpMask;
        public int Context;

        public static class ByReference extends IP_ADDR_STRING implements Structure.ByReference {
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] { "Next", "IpAddress", "IpMask", "Context" });
        }
    }

    class FIXED_INFO extends Structure {
        public byte[] HostName = new byte[MAX_HOSTNAME_LEN + 4];
        public byte[] DomainName = new byte[MAX_DOMAIN_NAME_LEN + 4];
        public IP_ADDR_STRING.ByReference CurrentDnsServer; // IP_ADDR_STRING
        public IP_ADDR_STRING DnsServerList;
        public UINT NodeType;
        public byte[] ScopeId = new byte[MAX_SCOPE_ID_LEN + 4];
        public UINT EnableRouting;
        public UINT EnableProxy;
        public UINT EnableDns;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] { "HostName", "DomainName", "CurrentDnsServer", "DnsServerList",
                    "NodeType", "ScopeId", "EnableRouting", "EnableProxy", "EnableDns" });
        }

        public FIXED_INFO(Pointer p) {
            super(p);
        }
    }

    int GetIfEntry(MIB_IFROW pIfRow);

    int GetIfEntry2(MIB_IFROW2 pIfRow2);

    int GetNetworkParams(FIXED_INFO pFixedInfo, ULONGByReference pOutBufLen);
}
