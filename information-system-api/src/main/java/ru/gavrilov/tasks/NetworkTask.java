package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.hardware.NetworkIF;
import ru.gavrilov.software.NetworkParams;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.util.FormatUtil;

import java.util.Arrays;

public class NetworkTask extends Task<String> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final OperatingSystem operatingSystem = SystemInfo.INSTANCE.getOperatingSystem();
    private static final Logger LOG = LoggerFactory.getLogger(NetworkTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking Network interfaces...");
        NetworkIF[] networkIFs = hardwareAbstractionLayer.getNetworkIFs();
        StringBuilder stringBuilder = new StringBuilder("Сетевые интерфейсы:");

        for (NetworkIF net : networkIFs) {
            stringBuilder.append(String.format("Имя: %s (%s)%n", net.getName(), net.getDisplayName()));
            stringBuilder.append(String.format("MAC Address: %s %n", net.getMacaddr()));
            stringBuilder.append(String.format("MTU: %s, Speed: %s %n", net.getMTU(), FormatUtil.formatValue(net.getSpeed(), "bps")));
            stringBuilder.append(String.format("IPv4: %s %n", Arrays.toString(net.getIPv4addr())));
            stringBuilder.append(String.format("IPv6: %s %n", Arrays.toString(net.getIPv6addr())));
            boolean hasData = net.getBytesRecv() > 0 || net.getBytesSent() > 0 || net.getPacketsRecv() > 0
                    || net.getPacketsSent() > 0;
            stringBuilder.append(String.format("Трафик: получено %s/%s%s; отправлено %s/%s%s %n",
                    hasData ? net.getPacketsRecv() + " пакетов" : "?",
                    hasData ? FormatUtil.formatBytes(net.getBytesRecv()) : "?",
                    hasData ? " (" + net.getInErrors() + " ошибка)" : "",
                    hasData ? net.getPacketsSent() + " пакетов" : "?",
                    hasData ? FormatUtil.formatBytes(net.getBytesSent()) : "?",
                    hasData ? " (" + net.getOutErrors() + " ошибка)" : ""));

        }

        setNetworkParameters(stringBuilder);

        return stringBuilder.toString();
    }

    private void setNetworkParameters (StringBuilder stringBuilder){
        LOG.info("Checking Network parameterss...");
        NetworkParams networkParams = operatingSystem.getNetworkParams();
        stringBuilder.append("Параметры сети:");
        stringBuilder.append(String.format("Имя хоста: %s%n", networkParams.getHostName()));
        stringBuilder.append(String.format("Имя домена: %s%n", networkParams.getDomainName()));
        stringBuilder.append(String.format("DNS серверы: %s%n", Arrays.toString(networkParams.getDnsServers())));
        stringBuilder.append(String.format("IPv4 шлюз: %s%n", networkParams.getIpv4DefaultGateway()));
        stringBuilder.append(String.format("IPv6 шлюз: %s%n", networkParams.getIpv6DefaultGateway()));
    }
}
