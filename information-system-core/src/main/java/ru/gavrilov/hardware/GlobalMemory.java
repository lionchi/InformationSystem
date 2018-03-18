package ru.gavrilov.hardware;

import java.io.Serializable;

public interface GlobalMemory extends Serializable {
    long getTotal();

    long getAvailable();

    long getSwapTotal();

    long getSwapUsed();
}
