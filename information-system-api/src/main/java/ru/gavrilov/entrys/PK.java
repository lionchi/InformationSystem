package ru.gavrilov.entrys;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;

@JsonAutoDetect
public class PK {
    @JsonIgnore
    public static final PK INSTANCE = new PK();
    private String inventoryNumber;
    private String location;
    private String serialNumberPk;
    private String modelPk;
    private String manufacture;
    private boolean isLaptop = false;
    //Motherboard
    private String versionBios;
    private String versionOs;
    private String motherboardManufacturer;
    private String motherboardSerialNumber;
    //Не используются
    private String nameHost;
    private String nameDomain;
    private String dnsServers;
    //Остальные комплектующие
    private CPU cpu;
    private ArrayList<HDD> hardDisks;
    private ArrayList<NetworkInterface> networkInterfaces;
    private ArrayList<Display> displays;
    private ArrayList<VideoCard> videoCards;

    public PK() {
    }

    public boolean canSave() {
        return ObjectUtils.allNotNull(versionBios, motherboardManufacturer, motherboardSerialNumber, cpu, hardDisks)
                && hardDisks.size() > 0;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSerialNumberPk() {
        return serialNumberPk;
    }

    public void setSerialNumberPk(String serialNumberPk) {
        if(!serialNumberPk.equals("Default string")) {
            setLaptop(true);
        }
        this.serialNumberPk = serialNumberPk;
    }

    public String getModelPk() {
        return modelPk;
    }

    public void setModelPk(String modelPk) {
        this.modelPk = modelPk;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getVersionBios() {
        return versionBios;
    }

    public void setVersionBios(String versionBios) {
        this.versionBios = versionBios;
    }

    public String getVersionOs() {
        return versionOs;
    }

    public void setVersionOs(String versionOs) {
        this.versionOs = versionOs;
    }

    public String getMotherboardManufacturer() {
        return motherboardManufacturer;
    }

    public void setMotherboardManufacturer(String motherboardManufacturer) {
        this.motherboardManufacturer = motherboardManufacturer;
    }

    public String getMotherboardSerialNumber() {
        return motherboardSerialNumber;
    }

    public void setMotherboardSerialNumber(String motherboardSerialNumber) {
        this.motherboardSerialNumber = motherboardSerialNumber;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public ArrayList<HDD> getHardDisks() {
        return hardDisks;
    }

    public void setHardDisks(ArrayList<HDD> hardDisks) {
        this.hardDisks = hardDisks;
    }

    public ArrayList<NetworkInterface> getNetworkInterfaces() {
        return networkInterfaces;
    }

    public void setNetworkInterfaces(ArrayList<NetworkInterface> networkInterfaces) {
        this.networkInterfaces = networkInterfaces;
    }

    public ArrayList<Display> getDisplays() {
        return displays;
    }

    public void setDisplays(ArrayList<Display> displays) {
        this.displays = displays;
    }

    public boolean isLaptop() {
        return isLaptop;
    }

    public void setLaptop(boolean laptop) {
        isLaptop = laptop;
    }

    public String getNameHost() {
        return nameHost;
    }

    public void setNameHost(String nameHost) {
        this.nameHost = nameHost;
    }

    public String getNameDomain() {
        return nameDomain;
    }

    public void setNameDomain(String nameDomain) {
        this.nameDomain = nameDomain;
    }

    public String getDnsServers() {
        return dnsServers;
    }

    public void setDnsServers(String dnsServers) {
        this.dnsServers = dnsServers;
    }

    public ArrayList<VideoCard> getVideoCards() {
        return videoCards;
    }

    public void setVideoCards(ArrayList<VideoCard> videoCards) {
        this.videoCards = videoCards;
    }
}
