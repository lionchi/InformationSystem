package ru.gavrilov.software.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.software.NetworkParams;
import ru.gavrilov.util.FileUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNetworkParams implements NetworkParams {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractNetworkParams.class);
    private static final String NAMESERVER = "nameserver";

    @Override
    public String getDomainName() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            LOG.error("Unknown host exception when getting address of local host: " + e);
            return "";
        }
    }


    @Override
    public String getHostName() {
        try {
            String hn = InetAddress.getLocalHost().getHostName();
            int dot = hn.indexOf('.');
            if (dot == -1) {
                return hn;
            } else {
                return hn.substring(0, dot);
            }
        } catch (UnknownHostException e) {
            LOG.error("Unknown host exception when getting address of local host: " + e);
            return "";
        }
    }

    @Override
    public String[] getDnsServers() {
        List<String> resolv = FileUtil.readFile("/etc/resolv.conf");
        String key = NAMESERVER;
        int maxNameServer = 3;
        List<String> servers = new ArrayList<>();
        for (int i = 0; i < resolv.size() && servers.size() < maxNameServer; i++) {
            String line = resolv.get(i);
            if (line.startsWith(key)) {
                String value = line.substring(key.length()).replaceFirst("^[ \t]+", "");
                if (value.length() != 0 && value.charAt(0) != '#' && value.charAt(0) != ';') {
                    String val = value.split("[ \t#;]", 2)[0];
                    servers.add(val);
                }
            }
        }
        return servers.toArray(new String[servers.size()]);
    }

    /**
     * Convenience method to parse the output of the `route` command. While the
     * command arguments vary between OS's the output is consistently parsable.
     *
     * @param lines
     *            output of OS-specific route command
     * @return default gateway
     *//*
    protected static String searchGateway(List<String> lines) {
        for (String line : lines) {
            String leftTrimmed = line.replaceFirst("^\\s+", "");
            if (leftTrimmed.startsWith("gateway:")) {
                String[] split = ParseUtil.whitespaces.split(leftTrimmed);
                if (split.length < 2) {
                    return "";
                }
                return split[1].split("%")[0];
            }
        }
        return "";
    }*/
}
