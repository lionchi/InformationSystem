package ru.gavrilov.common;

public enum TabEnum {
    CUMPUTER_SYSTEM("ПК"),
    CPU("CPU"),
    HDD("HDD"),
    NETWORK("Интернет"),
    VIDEO_CARD("Видеокарта"),
    DISPLAY("Дисплей");

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
