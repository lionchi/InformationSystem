package ru.gavrilov;

import ru.gavrilov.hardware.Display;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.software.OSFileStore;
import ru.gavrilov.software.OperatingSystem;


public class StartApp {
    public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        System.out.println(hardwareAbstractionLayer.getSensors().getCpuVoltage());

        for (Display display : hardwareAbstractionLayer.getDisplays()) {
            System.out.println(display.getEdid());
        }
    }
}
