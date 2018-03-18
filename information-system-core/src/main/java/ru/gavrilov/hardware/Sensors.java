package ru.gavrilov.hardware;

import java.io.Serializable;

public interface Sensors extends Serializable {

    double getCpuTemperature();

    int[] getFanSpeeds();

    double getCpuVoltage();
}
