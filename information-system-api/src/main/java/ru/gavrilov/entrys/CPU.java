package ru.gavrilov.entrys;

import java.io.Serializable;

public class CPU implements Serializable {
    private String name;
    private int logicalProcessorCount;
    private int physicalProcessorCount;
    private String identifier;
    private String processorID;

    public CPU() {
    }

    public CPU(String name, int logicalProcessorCount, int physicalProcessorCount, String identifier, String processorID) {
        this.name = name;
        this.logicalProcessorCount = logicalProcessorCount;
        this.physicalProcessorCount = physicalProcessorCount;
        this.identifier = identifier;
        this.processorID = processorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogicalProcessorCount() {
        return logicalProcessorCount;
    }

    public void setLogicalProcessorCount(int logicalProcessorCount) {
        this.logicalProcessorCount = logicalProcessorCount;
    }

    public int getPhysicalProcessorCount() {
        return physicalProcessorCount;
    }

    public void setPhysicalProcessorCount(int physicalProcessorCount) {
        this.physicalProcessorCount = physicalProcessorCount;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getProcessorID() {
        return processorID;
    }

    public void setProcessorID(String processorID) {
        this.processorID = processorID;
    }
}
