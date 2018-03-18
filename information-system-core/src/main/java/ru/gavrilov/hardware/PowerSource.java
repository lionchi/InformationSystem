package ru.gavrilov.hardware;

import java.io.Serializable;

public interface PowerSource extends Serializable {

    String getName();

    double getRemainingCapacity();

    double getTimeRemaining();
}
