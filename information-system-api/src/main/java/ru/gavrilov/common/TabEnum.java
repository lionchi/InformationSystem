package ru.gavrilov.common;

public enum TabEnum {
    CUMPUTER_SYSTEM("ПК"),
    FILE_SYSTEM("Файловая система"),
    CPU("CPU"),
    PROCESS("Процессы"),
    HDD("HDD"),
    USB_CONTROLLERS("Контроллеры USB"),
    NETWORK("Интернет"),
    STATUS_PK("Состояние ПК"),
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
