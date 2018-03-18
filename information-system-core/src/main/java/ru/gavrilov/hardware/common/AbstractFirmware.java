package ru.gavrilov.hardware.common;

import org.threeten.bp.LocalDate;
import ru.gavrilov.hardware.Firmware;


public abstract class AbstractFirmware implements Firmware {

    private static final long serialVersionUID = 1L;

    private String manufacturer;
    private String name;
    private String description;
    private String version;
    private LocalDate releaseDate;

    public AbstractFirmware() {
        this.manufacturer = "unknown";
        this.name = "unknown";
        this.description = "unknown";
        this.version = "unknown";
        this.releaseDate = null;
    }

    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }


    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

}
