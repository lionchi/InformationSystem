package ru.gavrilov.entrys;

import java.io.Serializable;

public class HDD  implements Serializable {
    private String model;
    private String serialNumber;
    private String size;
    private boolean isFormatted = false;
    private String dateOfFormatting;

    public HDD() {
    }

    public HDD(String model, String serialNumber, String size, boolean isFormatted) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.size = size;
        this.isFormatted = isFormatted;
    }

    public HDD(String model, String serialNumber, String size, String dateOfFormatting) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.size = size;
        this.dateOfFormatting = dateOfFormatting;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isFormatted() {
        return isFormatted;
    }

    public void setFormatted(boolean formatted) {
        isFormatted = formatted;
    }

    public String getDateOfFormatting() {
        return dateOfFormatting;
    }

    public void setDateOfFormatting(String dateOfFormatting) {
        this.dateOfFormatting = dateOfFormatting;
    }
}
