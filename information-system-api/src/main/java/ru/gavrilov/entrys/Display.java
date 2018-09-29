package ru.gavrilov.entrys;

import java.io.Serializable;

public class Display implements Serializable {
    private String manufId;
    private String name;
    private String diagonal;

    public Display() {
    }

    public Display(String manufId, String name, String diagonal) {
        this.manufId = manufId;
        this.name = name;
        this.diagonal = diagonal;
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
}
