package ru.gavrilov.entrys;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class NetworkInterface {
    private String name;
    private String macAddress;
    private String ipv4;
    private String ipv6;
    private String traffic;

    public NetworkInterface() {
    }

    public NetworkInterface(String name, String macAddress, String ipv4, String ipv6, String traffic) {
        this.name = name;
        this.macAddress = macAddress;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
        this.traffic = traffic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String trafic) {
        this.traffic = trafic;
    }
}
