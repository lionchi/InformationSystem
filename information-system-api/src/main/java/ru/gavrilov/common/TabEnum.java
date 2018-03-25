package ru.gavrilov.common;

public enum TabEnum {
    CUMPUTER_SYSTEM("Computer System"),
    FILE_SYSTEM("File System"),
    CPU("CPU"),
    MEMORY("Memory"),
    HARD_DISKS("HardDisks"),
    USB_DEVICES("USB Devices"),
    NETWORK("Network"),
    SENSORS_AND_PS("Sensors and power sources"),
    DISPLAY("Display");

    private String nameTab;

    TabEnum(String nameTab) {
        this.nameTab = nameTab;
    }

    public String getNameTab() {
        return nameTab;
    }

    @Override
    public String toString() {
        return nameTab;
    }
}
