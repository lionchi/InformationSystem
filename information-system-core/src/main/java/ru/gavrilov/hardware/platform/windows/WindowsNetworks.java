package ru.gavrilov.hardware.platform.windows;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef.ULONG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.hardware.NetworkIF;
import ru.gavrilov.hardware.common.AbstractNetworks;
import ru.gavrilov.jna.platform.windows.IPHlpAPI;
import ru.gavrilov.util.ParseUtil;

public class WindowsNetworks extends AbstractNetworks {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(WindowsNetworks.class);

    // Save Windows version info for 32 bit/64 bit branch later
    private static final byte majorVersion = Kernel32.INSTANCE.GetVersion().getLow().byteValue();

    /**
     * Обновляет статистику сетевой сети по данному интерфейсу. Статистика
     * включают отправленные и полученные пакеты и байты, а также скорость интерфейса.
     *
     * @param netIF
     * Интерфейс для обновления статистики
     */
    public static void updateNetworkStats(NetworkIF netIF) {
        // MIB_IFROW2 requires Vista (6.0) or later.
        if (majorVersion >= 6) {
            // Create new MIB_IFROW2 and set index to this interface index
            IPHlpAPI.MIB_IFROW2 ifRow = new IPHlpAPI.MIB_IFROW2();
            ifRow.InterfaceIndex = new ULONG(netIF.getNetworkInterface().getIndex());
            if (0 != IPHlpAPI.INSTANCE.GetIfEntry2(ifRow)) {
                // Error, abort
                LOG.error("Failed to retrieve data for interface {}, {}", netIF.getNetworkInterface().getIndex(),
                        netIF.getName());
                return;
            }
            // These are unsigned longs. netIF setter will mask sign bit.
            netIF.setBytesSent(ifRow.OutOctets);
            netIF.setBytesRecv(ifRow.InOctets);
            netIF.setPacketsSent(ifRow.OutUcastPkts);
            netIF.setPacketsRecv(ifRow.InUcastPkts);
            netIF.setOutErrors(ifRow.OutErrors);
            netIF.setInErrors(ifRow.InErrors);
            netIF.setSpeed(ifRow.ReceiveLinkSpeed);
        } else {
            // Create new MIB_IFROW and set index to this interface index
            IPHlpAPI.MIB_IFROW ifRow = new IPHlpAPI.MIB_IFROW();
            ifRow.dwIndex = netIF.getNetworkInterface().getIndex();
            if (0 != IPHlpAPI.INSTANCE.GetIfEntry(ifRow)) {
                // Error, abort
                LOG.error("Failed to retrieve data for interface {}, {}", netIF.getNetworkInterface().getIndex(),
                        netIF.getName());
                return;
            }
            // These are unsigned ints. Widen them to longs.
            netIF.setBytesSent(ParseUtil.unsignedIntToLong(ifRow.dwOutOctets));
            netIF.setBytesRecv(ParseUtil.unsignedIntToLong(ifRow.dwInOctets));
            netIF.setPacketsSent(ParseUtil.unsignedIntToLong(ifRow.dwOutUcastPkts));
            netIF.setPacketsRecv(ParseUtil.unsignedIntToLong(ifRow.dwInUcastPkts));
            netIF.setOutErrors(ParseUtil.unsignedIntToLong(ifRow.dwOutErrors));
            netIF.setInErrors(ParseUtil.unsignedIntToLong(ifRow.dwInErrors));
            netIF.setSpeed(ParseUtil.unsignedIntToLong(ifRow.dwSpeed));
        }
        netIF.setTimeStamp(System.currentTimeMillis());
    }
}
