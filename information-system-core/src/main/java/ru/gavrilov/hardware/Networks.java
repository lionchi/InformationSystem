package ru.gavrilov.hardware;

import java.io.Serializable;

public interface Networks extends Serializable {

    NetworkIF[] getNetworks();
}
