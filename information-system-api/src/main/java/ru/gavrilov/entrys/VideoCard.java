package ru.gavrilov.entrys;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class VideoCard {
    private String cardName;
    private String currentMode;
    private String driverVersion;
    private String deviceProblemCode;
    private String chipType;
    private String deviceKey;

    public VideoCard() {
    }

    public VideoCard(String cardName, String currentMode, String driverVersion,
                     String deviceProblemCode, String chipType, String deviceKey) {
        this.cardName = cardName;
        this.currentMode = currentMode;
        this.driverVersion = driverVersion;
        this.deviceProblemCode = deviceProblemCode;
        this.chipType = chipType;
        this.deviceKey = deviceKey;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getDeviceProblemCode() {
        return deviceProblemCode;
    }

    public void setDeviceProblemCode(String deviceProblemCode) {
        this.deviceProblemCode = deviceProblemCode;
    }

    public String getChipType() {
        return chipType;
    }

    public void setChipType(String chipType) {
        this.chipType = chipType;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    @Override
    public String toString() {
        return "VideoCard{" +
                "cardName='" + cardName + '\'' +
                ", currentMode='" + currentMode + '\'' +
                ", driverVersion='" + driverVersion + '\'' +
                ", deviceProblemCode='" + deviceProblemCode + '\'' +
                ", chipType='" + chipType + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                '}';
    }
}
