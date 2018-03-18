package ru.gavrilov.hardware.common;

import ru.gavrilov.hardware.Baseboard;

public abstract class AbstractBaseboard implements Baseboard {

    private static final long serialVersionUID = 1L;

    private String manufacturer;
    private String model;
    private String version;
    private String serialNumber;

    public AbstractBaseboard() {
        this.manufacturer = "unknown";
        this.model = "unknown";
        this.version = "unknown";
        this.serialNumber = "";
    }

    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }

    @Override
    public String getModel() {
        return this.model;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
