package ru.gavrilov.software;

import java.io.Serializable;

public interface NetworkParams  extends Serializable {

    String getHostName();

    String getDomainName();

    String[] getDnsServers();

    String getIpv4DefaultGateway();

    String getIpv6DefaultGateway();
}
