package ru.gavrilov.hardware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.platform.windows.WindowsNetworks;
import ru.gavrilov.util.FormatUtil;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NetworkIF implements Serializable {

    private static final long serialVersionUID = 2L;

    private static final Logger LOG = LoggerFactory.getLogger(NetworkIF.class);

    private transient NetworkInterface networkInterface;
    private int mtu;
    private String mac;
    private String[] ipv4;
    private String[] ipv6;
    private long bytesRecv;
    private long bytesSent;
    private long packetsRecv;
    private long packetsSent;
    private long inErrors;
    private long outErrors;
    private long speed;
    private long timeStamp;

    public NetworkInterface getNetworkInterface() {
        return this.networkInterface;
    }

    public void setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
        try {
            // Set MTU
            this.mtu = networkInterface.getMTU();
            // Set MAC
            byte[] hwmac = networkInterface.getHardwareAddress();
            if (hwmac != null) {
                List<String> octets = new ArrayList<>(6);
                for (byte b : hwmac) {
                    octets.add(String.format("%02x", b));
                }
                this.mac = FormatUtil.join(":", octets);
            } else {
                this.mac = "Unknown";
            }
            // Set IP arrays
            ArrayList<String> ipv4list = new ArrayList<>();
            ArrayList<String> ipv6list = new ArrayList<>();
            for (InetAddress address : Collections.list(networkInterface.getInetAddresses())) {
                if (address.getHostAddress().length() == 0) {
                    continue;
                } else if (address.getHostAddress().contains(":")) {
                    ipv6list.add(address.getHostAddress().split("%")[0]);
                } else {
                    ipv4list.add(address.getHostAddress());
                }
            }
            this.ipv4 = ipv4list.toArray(new String[ipv4list.size()]);
            this.ipv6 = ipv6list.toArray(new String[ipv6list.size()]);
        } catch (SocketException e) {
            LOG.error("Socket exception: {}", e);
            return;
        }
    }

    public String getName() {
        return this.networkInterface.getName();
    }

    /**
     * @return Описание сетевого интерфейса. На некоторых платформах это
     * совпадает с именем.
     */
    public String getDisplayName() {
        return this.networkInterface.getDisplayName();
    }

    /**
     * @return максимальный размер пакета, который может быть передан по сети без фрагментации
     * это значение устанавливается, когда
     * {@link NetworkIF} создается и может не обновляться.
     * обновите это значение, выполните
     * {@link #setNetworkInterface (NetworkInterface)} метод
     * /
     */
    public int getMTU() {
        return this.mtu;
    }

    public String getMacaddr() {
        return this.mac;
    }

    public String[] getIPv4addr() {
        return Arrays.copyOf(this.ipv4, this.ipv4.length);
    }

    public String[] getIPv6addr() {
        return Arrays.copyOf(this.ipv6, this.ipv6.length);
    }

    public long getBytesRecv() {
        return this.bytesRecv;
    }

    public void setBytesRecv(long bytesRecv) {
        this.bytesRecv = bytesRecv & 0x7fffffffffffffffL;
    }

    public long getBytesSent() {
        return this.bytesSent;
    }

    public void setBytesSent(long bytesSent) {
        this.bytesSent = bytesSent & 0x7fffffffffffffffL;
    }

    public long getPacketsRecv() {
        return this.packetsRecv;
    }

    public void setPacketsRecv(long packetsRecv) {
        this.packetsRecv = packetsRecv & 0x7fffffffffffffffL;
    }

    public long getPacketsSent() {
        return this.packetsSent;
    }

    public void setPacketsSent(long packetsSent) {
        this.packetsSent = packetsSent & 0x7fffffffffffffffL;
    }

    /**
    * @return Ошибки ввода. Это значение устанавливается, когда {@link NetworkIF}
     * экземпляр и не может быть актуальным. Чтобы обновить это значение,
     * выполнить {@link #updateNetworkStats ()} метод
     */
    public long getInErrors() {
        return this.inErrors;
    }

    public void setInErrors(long inErrors) {
        this.inErrors = inErrors & 0x7fffffffffffffffL;
    }

    /**
     * @return Ошибки вывода. Это значение устанавливается, когда {@link NetworkIF}
     * создается и не может быть актуальным. Чтобы обновить это значение,
     * выполнить {@link #updateNetworkStats ()} метод
     */
    public long getOutErrors() {
        return this.outErrors;
    }

    public void setOutErrors(long outErrors) {
        this.outErrors = outErrors & 0x7fffffffffffffffL;
    }

    public long getSpeed() {
        return this.speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed & 0x7fffffffffffffffL;
    }

    /**
     * @return последовательность символов или закодированной информации, показывающей,
     * когда произошло определённое событие. Обычно показывает дату и время (иногда с точностью до долей секунд).
     */
    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Обновляет интерфейсную сетевую статистику по этому интерфейсу. Статистика
     * включают отправленные и полученные пакеты и байты, а также скорость интерфейса.
     */
    public void updateNetworkStats() {
        switch (SystemInfo.getCurrentPlatformEnum()) {
        case WINDOWS:
            WindowsNetworks.updateNetworkStats(this);
            break;
        default:
            LOG.error("Unsupported platform. No update performed.");
            break;
        }
    }

}
