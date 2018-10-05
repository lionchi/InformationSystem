package ru.gavrilov.entrys;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Display {
    private String manufId;
    private String name;
    private String diagonal;
    private String displayId;

    public Display() {
    }

    public Display(String manufId, String name, String diagonal, String displayId) {
        this.manufId = manufId;
        this.name = name;
        this.diagonal = diagonal;
        this.displayId = displayId;
    }

    public String getManufId() {
        return manufId;
    }

    public void setManufId(String manufId) {
        this.manufId = manufId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(String diagonal) {
        this.diagonal = diagonal;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }
}
