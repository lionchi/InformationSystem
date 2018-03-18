package ru.gavrilov.hardware.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.hardware.NetworkIF;
import ru.gavrilov.hardware.Networks;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public abstract class AbstractNetworks implements Networks {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractNetworks.class);

    @Override
    public NetworkIF[] getNetworks() {
        List<NetworkIF> result = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(interfaces)) {
                if (!netint.isLoopback() && netint.getHardwareAddress() != null) {
                    NetworkIF netIF = new NetworkIF();
                    netIF.setNetworkInterface(netint);
                    netIF.updateNetworkStats();
                    result.add(netIF);
                }
            }
        } catch (SocketException ex) {
            LOG.error("Socket exception when retrieving network interfaces: " + ex);
        }
        return result.toArray(new NetworkIF[result.size()]);
    }
}
