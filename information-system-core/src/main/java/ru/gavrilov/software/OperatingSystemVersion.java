package ru.gavrilov.software;

import java.io.Serializable;

public interface OperatingSystemVersion  extends Serializable {

    String getVersion();

    void setVersion(String version);

    String getCodeName();

    void setCodeName(String codeName);

    String getBuildNumber();

    void setBuildNumber(String buildNumber);
}
