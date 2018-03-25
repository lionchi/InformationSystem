package ru.gavrilov.entrys;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProcessEntry {
    private StringProperty pid = new SimpleStringProperty();
    private StringProperty cpu = new SimpleStringProperty();
    private StringProperty memory = new SimpleStringProperty();
    private StringProperty vsz = new SimpleStringProperty();
    private StringProperty rss = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();

    public ProcessEntry(String pid, String cpu, String memory, String vsz, String rss, String name) {
        this.pid.setValue(pid);
        this.cpu.setValue(cpu);
        this.memory.setValue(memory);
        this.vsz.setValue(vsz);
        this.rss.setValue(rss);
        this.name.setValue(name);
    }

    public String getPid() {
        return pid.get();
    }

    public StringProperty pidProperty() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid.set(pid);
    }

    public String getCpu() {
        return cpu.get();
    }

    public StringProperty cpuProperty() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu.set(cpu);
    }

    public String getMemory() {
        return memory.get();
    }

    public StringProperty memoryProperty() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory.set(memory);
    }

    public String getVsz() {
        return vsz.get();
    }

    public StringProperty vszProperty() {
        return vsz;
    }

    public void setVsz(String vsz) {
        this.vsz.set(vsz);
    }

    public String getRss() {
        return rss.get();
    }

    public StringProperty rssProperty() {
        return rss;
    }

    public void setRss(String rss) {
        this.rss.set(rss);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
