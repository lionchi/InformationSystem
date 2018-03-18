package ru.gavrilov.hardware.common;

import ru.gavrilov.hardware.Baseboard;
import ru.gavrilov.hardware.ComputerSystem;
import ru.gavrilov.hardware.Firmware;

public abstract class AbstractComputerSystem implements ComputerSystem {

    private static final long serialVersionUID = 1L;

    private String manufacturer;
    private String model;
    private String serialNumber;
    private Firmware firmware;
    private Baseboard baseboard;

    protected AbstractComputerSystem() {
        this.manufacturer = "unknown";
        this.model = "unknown";
        this.serialNumber = "unknown";
        this.firmware = null;
        this.baseboard = null;
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
    public String getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public Firmware getFirmware() {
        return this.firmware;
    }

    @Override
    public Baseboard getBaseboard() {
        return this.baseboard;
    }

    protected void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    protected void setModel(String model) {
        this.model = model;
    }

    protected void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    protected void setFirmware(Firmware firmware) {
        this.firmware = firmware;
    }

    protected void setBaseboard(Baseboard baseboard) {
        this.baseboard = baseboard;
    }

}
