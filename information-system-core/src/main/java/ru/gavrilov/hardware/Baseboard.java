package ru.gavrilov.hardware;

import java.io.Serializable;


public interface Baseboard extends Serializable {

    String getManufacturer();

    String getModel();

    String getVersion();

    String getSerialNumber();
}
