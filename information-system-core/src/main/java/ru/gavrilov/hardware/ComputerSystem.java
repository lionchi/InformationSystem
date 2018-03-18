package ru.gavrilov.hardware;

import java.io.Serializable;

public interface ComputerSystem extends Serializable {

    String getManufacturer();

    String getModel();

    String getSerialNumber();

    Firmware getFirmware();

    Baseboard getBaseboard();

}
